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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.lorebase.BaseActivity;
import com.example.lorebase.R;
import com.example.lorebase.contain_const.UrlContainer;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Request;

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
//        app_name = findViewById(R.id.app_launch);
        getImage(); //获取并设置bing图片
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

    private void getImage() {
        String url = "https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1";
        OkHttpUtils
                .get()
                .url(url)
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
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("images");
                            String url_image = jsonArray.getJSONObject(0).getString("url");
                            String fullUrl = UrlContainer.BI_YING + url_image;
                            Glide.with(launchActivity).load(fullUrl).transition(new DrawableTransitionOptions().crossFade()).into(image_launch);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
