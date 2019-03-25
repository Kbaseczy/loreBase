package com.example.lorebase.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lorebase.BaseActivity;
import com.example.lorebase.MyApplication;
import com.example.lorebase.R;
import com.example.lorebase.bean.User;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.http.RetrofitApi;
import com.example.lorebase.util.ActivityCollector;
import com.example.lorebase.util.ToastUtil;

import retrofit2.Callback;
import retrofit2.Response;


/*
   这里采用持久化存储实现记住密码功能，理论上可行，实际中应增加密码加密算法，防止用户密码泄露
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText etUsername;
    private EditText etPassword;

    static SharedPreferences pref;
    static SharedPreferences.Editor editor;
    SharedPreferences.Editor clear;

    CheckBox remember_pass;
    CheckBox autoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityCollector.addActivtity(this);
        initView();
    }

    private void initView() {
        TextView btnLogin = findViewById(R.id.btn_login);
        TextView toRegister = findViewById(R.id.have_no_account);

        etUsername = findViewById(R.id.username_login);
        etPassword = findViewById(R.id.password_login);
        remember_pass = findViewById(R.id.rememberPWD);
        autoLogin = findViewById(R.id.autoLogin);

        /*
        法一：PreferenceManager.getDefaultSharedPreferences(this) 建立以包名前缀为文件名的文件
        法二：getSharedPreferences("login_data",MODE_PRIVATE) 建立以data为文件名的文件
         */
        pref = getSharedPreferences(ConstName.LOGIN_DATA, MODE_PRIVATE); //法二
        boolean isRemember = pref.getBoolean(ConstName.IS_REMEMBER, false);//checkBox 是否记住密码的boolean，设置默认值
        if (isRemember) {//默认为false，这段第一次运行时候也是isRemember第一次为true的时候
            String userName = pref.getString(ConstName.USER_NAME, "");//将存储的数据返回到输入框
            String password = pref.getString(ConstName.PASS_WORD, "");
            etUsername.setText(userName);
            etPassword.setText(password);
            remember_pass.setChecked(true);/*/
            如果进行这一步必然有isRemember为true，而它为true来源是登陆时checkBox被勾选
            既然checkBox勾选了，还要设置它勾选？->保存的是数据，并没有保存“勾选”这个状态，数据设置了体现出状态
            TEST:去掉setChecked(true)，二次启动为勾选状态？ --> 不是勾選狀態
            */
        } else {
            clear = pref.edit();
            clear.clear();
            clear.apply();
        }

        if (pref.getBoolean(ConstName.IS_AUTO_LOGIN, false)) {
            autoLogin.setChecked(true); //同记住密码，如果自动登陆为true，应该设置该按钮为勾选状态
        }
        btnLogin.setOnClickListener(this);
        toRegister.setOnClickListener(this);
        autoLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.have_no_account:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.go_in, R.animator.go_out);
                LoginActivity.this.finish();
                break;

            case R.id.btn_login:
                String userName = etUsername.getText().toString().trim(); //trim() 舍去首尾空白，
                String password = etPassword.getText().toString().trim();//getText().toString() 获取文本内容并转换为String类型
                Log.v("btn_login username", userName + " pass:" + password);
                if (userName.equals("") || password.equals("")) {
                    Toast.makeText(this, "userInfo isn't completed.", Toast.LENGTH_SHORT).show();
                } else if (!checkNetwork()) {
                    Toast.makeText(this, "Internet Unconnected.", Toast.LENGTH_SHORT).show();
                } else {
                    login(userName, password);
                }
                break;

            case R.id.autoLogin:
                if (autoLogin.isChecked()) {
                    remember_pass.setChecked(true);
                    pref.edit().putBoolean(ConstName.IS_AUTO_LOGIN, true).apply();
                } else {
                    pref.edit().putBoolean(ConstName.IS_AUTO_LOGIN, false).apply();
                }
        }
    }

    private void login(String userName, String password) {
        RetrofitApi api = MyApplication.retrofit.create(RetrofitApi.class);
        retrofit2.Call<User> loginCall = api.login(userName, password);
        loginCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(retrofit2.Call<User> call, Response<User> response) {
                if (response.body() != null) {
                    if (response.body().getErrorCode() == 0) {
                        editor = pref.edit();
                        editor.putBoolean(ConstName.IS_LOGIN, true); //存储登陆状态的Boolean
                        editor.putString(ConstName.USER_NAME, userName);
                        //根据CheckBox判断  存储 checkBox boolean/账号/密码
                        if (remember_pass.isChecked()) {
                            editor.putBoolean(ConstName.IS_REMEMBER, true);
                            editor.putString(ConstName.PASS_WORD, password);
                        } else {
                            editor.clear(); //用于第二次及以后登陆时  如果取消勾选则清除数据
                        }
                        editor.apply(); //提交保存数据

                        Toast.makeText(LoginActivity.this, "sign in Successful", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.animator.go_in, R.animator.go_out);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, response.body().getErrorMsg(), Toast.LENGTH_LONG).show();

                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<User> call, Throwable t) {
                ToastUtil.showShortToastCenter(t.getMessage(),LoginActivity.this);
            }
        });

    }

    private boolean checkNetwork() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connManager != null;
        return connManager.getActiveNetworkInfo().isAvailable();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        return super.onKeyDown(keyCode, event);
    }
}

