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

import androidx.constraintlayout.widget.ConstraintLayout;

public class LaunchActivity extends BaseActivity {

    AlphaAnimation alphaAnimation;
    ConstraintLayout launch_layout;
    ImageView image_launch;
    Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        View view = View.inflate(this,R.layout.activity_launch,null);
        setContentView(R.layout.activity_launch);
        mHandler = new Handler();
        launch_layout = findViewById(R.id.launch_layout);
        image_launch = findViewById(R.id.image_launch);
        Glide.with(getApplicationContext()).load(R.drawable.image_store).into(image_launch);
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
}
