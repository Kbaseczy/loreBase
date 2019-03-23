package com.example.lorebase.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lorebase.BaseActivity;
import com.example.lorebase.MyApplication;
import com.example.lorebase.R;
import com.example.lorebase.bean.User;
import com.example.lorebase.http.RetrofitApi;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private EditText passWord;
    private EditText userCount;
    private EditText re_input_pass;
    private TextView submit, have_account;

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
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            overridePendingTransition(R.animator.go_in, R.animator.go_out);
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_register) {
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
        RetrofitApi api = MyApplication.retrofit.create(RetrofitApi.class);
        Map<String,String> params = new HashMap<>();
        params.put("username",user_name);
        params.put("password",pass);
        params.put("repassword",re_pass);
        retrofit2.Call<User> userCall = api.register(params);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(retrofit2.Call<User> call, Response<User> response) {
                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
                overridePendingTransition(R.animator.go_in, R.animator.go_out);
                finish();
            }

            @Override
            public void onFailure(retrofit2.Call<User> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        return super.onKeyDown(keyCode, event);
    }
}


