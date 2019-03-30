package com.example.lorebase.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.example.lorebase.BaseActivity;
import com.example.lorebase.MyApplication;
import com.example.lorebase.R;
import com.example.lorebase.bean.User;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.http.RetrofitApi;
import com.example.lorebase.util.ActivityCollector;
import com.example.lorebase.util.L;
import com.example.lorebase.util.ToastUtil;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import retrofit2.Callback;
import retrofit2.Response;

public class MySettingActivity extends BaseActivity {
    SharedPreferences.Editor editor;
    SharedPreferences sp;
    TextView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_setting);
        ActivityCollector.addActivtity(this);

        sp = getSharedPreferences(ConstName.LOGIN_DATA, MODE_PRIVATE);
        Toolbar toolbar = findViewById(R.id.toolbar_setting);
        logout = findViewById(R.id.tv_logout);
        toolbar.setTitle(R.string.setting);
        toolbar.setNavigationOnClickListener((view) -> {
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(R.animator.go_in, R.animator.go_out);
        });
    }

    @Override
    protected void onResume() {
        logout.setVisibility(sp.getBoolean(ConstName.IS_LOGIN, false) ? View.VISIBLE : View.GONE);
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //main进入设置界面时已经设置销毁，再次进入main会走OnCreate方法-实现日夜间模式切换   onRestore...
        startActivity(new Intent(this,MainActivity.class));
        L.v("onKeyDown setting");
        return super.onKeyDown(keyCode, event);
    }

    public void tv_about_us(View view) {
        startActivity(new Intent(this, AboutUsActivity.class));
        overridePendingTransition(R.animator.go_in, R.animator.go_out);
    }

    public void tv_logoutClick(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.tip)
                .setIcon(R.mipmap.ic_launcher_round)
                .setMessage(R.string.tip_content_logout)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    logout();
                    ToastUtil.showShortToastCenter("已注销", this);
                })
                .setNegativeButton(R.string.cancel, (dialog, which) ->
                        dialog.dismiss());
        alertDialog.create().show();
    }

    public void tv_exit(View view) {
        //todo 需要添加管理activity的類，統一關閉所有activity
        ActivityCollector.finishAll();
    }

    private void logout() {

        RetrofitApi api = MyApplication.retrofit.create(RetrofitApi.class);
        retrofit2.Call<User> logoutCall = api.logout();
        logoutCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(retrofit2.Call<User> call, Response<User> response) {
                L.v(response.body() != null ? response.body().getErrorCode() + "<--ErrorCode " : " -1");
                //發送請求，獲得響應，為true則在服務器清除成功 --> 更新isLogin的值
                editor = sp.edit();
                //界面内點擊注銷 ， 清除用戶信息，無保留
                editor.clear();
                editor.apply();
                logout.setVisibility(sp.getBoolean(ConstName.IS_LOGIN, false) ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onFailure(retrofit2.Call<User> call, Throwable t) {

            }
        });
    }

    public void tv_app_info(View view) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }
}
