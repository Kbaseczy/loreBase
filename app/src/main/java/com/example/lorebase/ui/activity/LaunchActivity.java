package com.example.lorebase.ui.activity;

import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lorebase.BaseActivity;
import com.example.lorebase.R;
import com.example.lorebase.http.RetrofitUtil;

public class LaunchActivity extends BaseActivity {
    LaunchActivity launchActivity;
    ImageView image_launch;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launch);
        launchActivity = this;
        mHandler = new Handler();
        image_launch = findViewById(R.id.image_launch);
        RetrofitUtil.getBiYing(this,image_launch);
        initStartAnim();
    }

    private void initStartAnim() {

        ValueAnimator animator = ValueAnimator.ofObject(new FloatEvaluator(), 1.0f, 1.2f);
        animator.setDuration(3000);
        animator.addUpdateListener(animation -> {
            Float value = (Float) animation.getAnimatedValue();
            if (value != 1.2f) {
                image_launch.setScaleX(value);
                image_launch.setScaleY(value);
            } else {
                jump();
            }
        });
        animator.start();
    }

    private void jump() {
        //判断是否第一次进入app
        mHandler.postDelayed(new Runnable() {
            SharedPreferences sp = getPreferences(MODE_PRIVATE);
            Boolean isFirst = sp.getBoolean("isFirst", true);
            Intent intent = new Intent();

            @Override
            public void run() {
                if (isFirst) {
                    //第一次进入,跳转引导页
                    sp.edit().putBoolean("isFirst", false).apply();
                    intent.setClass(LaunchActivity.this, GuideActivity.class);
                } else {
                    //否则进入主界面
                    intent.setClass(LaunchActivity.this, MainActivity.class);
                    Toast.makeText(LaunchActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                }
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        }, 2000);
    }

}
