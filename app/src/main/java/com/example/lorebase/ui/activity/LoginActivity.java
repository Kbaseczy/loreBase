package com.example.lorebase.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lorebase.R;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.contain_const.UrlContainer;
import com.example.lorebase.util.L;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Request;

/*
   这里采用持久化存储实现记住密码功能，理论上可行，实际中应增加密码加密算法，防止用户密码泄露
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    private TextView toRegister;
    private TextView btnLogin;
    private EditText etUsername;
    private EditText etPassword;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    CheckBox remember_pass, autoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = findViewById(R.id.btn_login);
        toRegister = findViewById(R.id.have_no_account);
        etUsername = findViewById(R.id.username_login);
        etPassword = findViewById(R.id.password_login);
        remember_pass = findViewById(R.id.rememberPWD);
        autoLogin = findViewById(R.id.autoLogin);

        /*

        法一：PreferenceManager.getDefaultSharedPreferences(this) 建立以包名前缀为文件名的文件
        法二：getSharedPreferences("login_data",MODE_PRIVATE) 建立以data为文件名的文件
         */
        pref = getSharedPreferences(ConstName.LOGIN_DATA, MODE_PRIVATE); //法二
        boolean isRemember = pref.getBoolean("remember_pass", false);//checkBox 是否记住密码的boolean，设置默认值
        if (isRemember) {//默认为false，这段第一次运行时候也是isRemember第一次为true的时候
            String userName = pref.getString("username", "");//将存储的数据返回到输入框
            String password = pref.getString("password", "");
            etUsername.setText(userName);
            etPassword.setText(password);
            remember_pass.setChecked(true);/*
            如果进行这一步必然有isRemember为true，而它为true来源是登陆时checkBox被勾选
            既然checkBox勾选了，还要设置它勾选？->保存的是数据，并没有保存“勾选”这个状态，数据设置了体现出状态
            TEST:去掉setChecked(true)，二次启动为勾选状态？
            */
        }

        btnLogin.setOnClickListener(this);
        toRegister.setOnClickListener(this);
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
//                testDemoPost();
//                testDemoGet();
                break;

        }
    }

    private void login(String userName, String password) {
        String url = UrlContainer.baseUrl + UrlContainer.LOGIN;
        OkHttpUtils
                .post()
                .url(url)
                .addParams("username", userName)
                .addParams("password", password)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        L.e(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getInt("errorCode") == 0) {
//                                L.v("errorCode is correct");
                                editor = pref.edit();
                                //存储登陆状态
                                editor.putBoolean(ConstName.IS_LOGIN, true); //存储登陆状态的Boolean

                                //根据CheckBox判断  存储 checkBox boolean/账号/密码
                                if (remember_pass.isChecked()) {
                                    editor.putString(ConstName.USER_NAME, userName);
                                    editor.putString("password", password);
                                } else {
                                    editor.clear(); //用于第二次及以后登陆时  如果取消勾选则清除数据
                                }
                                editor.putBoolean("remember_pass", true); //用户名的记录不和 是否勾选CheckBox联系
                                editor.apply(); //提交数据

                                Toast.makeText(LoginActivity.this, "sign in Successful", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                                overridePendingTransition(R.animator.go_in, R.animator.go_out);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, jsonObject.getString("errorMsg"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private boolean checkNetwork() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connManager != null;
        return connManager.getActiveNetworkInfo().isAvailable();
    }


    public void testDemoGet() {
        String url = "http://www.wanandroid.com/banner/json";
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "登陆异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        L.e(response);
                    }
                });

    }

    public void testDemoPost() {
        String url = "http://www.wanandroid.com/user/login";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("username", "15023474083")
                .addParams("password", "123456")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "登陆异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        L.e(response);
                    }
                });

    }

}

