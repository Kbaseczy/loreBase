package com.example.lorebase.ui.activity;

import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.lorebase.BaseActivity;
import com.example.lorebase.R;
import com.example.lorebase.contain_const.ConstName;
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
        mHandler = new Handler();
        image_launch = findViewById(R.id.image_launch);

        loadLaunchImage();
        initStartAnim();
    }

    private void loadLaunchImage() {
        //避免当日重复网络请求 //时间戳命名  年/月/日
        String pathPre = ConstName.IMAGE_PATH_PRE;
        L.v("本地图片名:" + new File(pathPre, ConstName.IMAGE_NAME).getName());
        //       如果今日时间戳图片存在
        if (FileUtil.fileIsExists(ConstName.IMAGE_NAME)) {
            //则设置今日时间戳图片
            image_launch.setImageDrawable(FileUtil.getDrawableImage(this));
            L.v("本地图片调用");
        } else {
            L.v("网络请求调用");
            //否则网络请求今日时间戳图片，并保存到本地。今日图片名：ConstName.IMAGE_NAME
            RetrofitUtil.getBiYing(this, image_launch);  //网络请求并设置
            RetrofitUtil.setTotoDataInterface((url) ->  //下载到本地
                    new Thread(() -> {
                        Bitmap bitmap;
                        try {
                            bitmap = Glide.with(launchActivity)
                                    .asBitmap()
                                    .load(url)
                                    .submit(1080, 1920)
                                    .get();
                            FileUtil.save(pathPre, bitmap);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start());
        }
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

}
