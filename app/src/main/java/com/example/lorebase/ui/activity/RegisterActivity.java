package com.example.lorebase.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lorebase.R;
import com.example.lorebase.contain_const.UrlContainer;
import com.example.lorebase.util.L;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Request;

public class RegisterActivity extends Activity implements View.OnClickListener {
    private EditText passWord;
    private EditText userCount;
    private EditText re_input_pass;
    private TextView submit,have_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userCount = findViewById(R.id.et_username);
        passWord = findViewById(R.id.et_password);
        re_input_pass = findViewById(R.id.et_password2);
        submit = findViewById(R.id.btn_register);
        have_account = findViewById(R.id.have_account);
        submit.setOnClickListener(this);
        have_account.setOnClickListener(v -> {
                    startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                    overridePendingTransition(R.animator.go_in,R.animator.go_out);
                });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_register) { //用户登录的按钮被单击
            final String user_name = userCount.getText().toString().trim();
            final String pass = passWord.getText().toString().trim();
            final String re_pass = re_input_pass.getText().toString().trim();
            if (user_name.equals("") || pass.equals("")) { //用户名或者密码未填写
                Toast.makeText(getApplicationContext(), "Complete the info,Please.", Toast.LENGTH_LONG).show();
            } else {
                postData(user_name, pass, re_pass);
            }
        }
    }

    private void postData(String user_name, String pass, String re_pass) {
        String url = UrlContainer.baseUrl + UrlContainer.REGISTER;
        OkHttpUtils
                .post()
                .url(url)
                .addParams("username", user_name)
                .addParams("password", pass)
                .addParams("repassword", re_pass)
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
                        //{"status":0,"msg":"注册成功"}
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getInt("errorCode") == 0) {
                                Toast.makeText(RegisterActivity.this,"Sign up Successful", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(i);
                                overridePendingTransition(R.animator.go_in, R.animator.go_out);
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, jsonObject.getString("errorMsg"), Toast.LENGTH_LONG).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });
    }
}


