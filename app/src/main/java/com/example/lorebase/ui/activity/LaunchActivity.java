package com.example.lorebase.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lorebase.BaseActivity;
import com.example.lorebase.R;
import com.example.lorebase.contain_const.UrlContainer;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.constraintlayout.widget.ConstraintLayout;
import okhttp3.Call;
import okhttp3.Request;

public class LaunchActivity extends BaseActivity {
    LaunchActivity launchActivity;
    AlphaAnimation alphaAnimation;
    ConstraintLayout launch_layout;
    ImageView image_launch;
    Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        launchActivity = this;
        mHandler = new Handler();
        launch_layout = findViewById(R.id.launch_layout);
        image_launch = findViewById(R.id.image_launch);
        getImage(); //获取并设置bing图片
        alphaAnimation = new AlphaAnimation(0.3F,1.0F);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                    //动画开始时回调的内容
//                AlphaAnimation alpha = (AlphaAnimation) AnimationUtils.loadAnimation(LaunchActivity.this,R.anim.alpha);
//                launch_layout.startAnimation(alpha);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                jump(); //结束后跳转到主界面
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //重复时回调内容
            }
        });
        alphaAnimation.setDuration(2000); //动画持续时间
//        alphaAnimation.setFillAfter(true); //保持结束后的状态
        launch_layout.startAnimation(alphaAnimation);
    }

    private void jump() {
        //判断是否第一次进入app
        mHandler.postDelayed(new Runnable() {
            SharedPreferences sp = getPreferences(MODE_PRIVATE);
            Boolean isFirst = sp.getBoolean("isFirst",true);
            Intent intent = new Intent();
            @Override
            public void run() {
                if(isFirst){
                    //第一次进入,跳转引导页
                    sp.edit().putBoolean("isFirst",false).apply();
                    intent.setClass(LaunchActivity.this,GuideActivity.class);
                }else{
                    //否则进入主界面
                    intent.setClass(LaunchActivity.this,MainActivity.class);
                    Toast.makeText(LaunchActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                }
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        },3000);
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
                            String fullUrl = UrlContainer.BI_YING +url_image;
                            Glide.with(launchActivity).load(fullUrl).into(image_launch);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
