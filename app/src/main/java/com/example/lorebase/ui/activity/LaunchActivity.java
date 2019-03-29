package com.example.lorebase.ui.activity;

import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.lorebase.BaseActivity;
import com.example.lorebase.R;
import com.example.lorebase.http.RetrofitUtil;
import com.example.lorebase.util.ActivityCollector;
import com.example.lorebase.util.FileUtil;
import com.example.lorebase.util.L;
import com.example.lorebase.util.ToastUtil;

import java.io.File;
import java.util.concurrent.ExecutionException;

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

        ActivityCollector.addActivtity(this);
        launchActivity = this;
        String pathPre = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/Android/data/com.example.lorebase/";
        mHandler = new Handler();
        image_launch = findViewById(R.id.image_launch);
        RetrofitUtil.getBiYing(this, image_launch);
        RetrofitUtil.setTotoDataInterface((url) ->
                new Thread(() -> {
                    Bitmap bitmap;
                    try {
                        bitmap = Glide.with(LaunchActivity.this)
                                .asBitmap()
                                .load(url)
                                .submit(500, 500)
                                .get();
                        FileUtil.copy(pathPre + "nav_back_image" + ".jpg", FileUtil.Bitmap2Bytes(bitmap));
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start());
        String path = pathPre + "nav_back_image.jpg";
        if (fileIsExists(path))
            L.v("pathfile:" + path);
        else
            L.v("not exist;" + path);
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
                    ToastUtil.showShortToastCenter("Hello, LoreBase！", LaunchActivity.this);
                }
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        }, 2000);
    }

    public boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
