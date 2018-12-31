package com.example.lorebase.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.lorebase.BaseActivity;
import com.example.lorebase.R;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.contain_const.UrlContainer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.widget.Toolbar;

public class AboutUsActivity extends BaseActivity {

    FloatingActionButton feed_back;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        toolbar = findViewById(R.id.about_us_toolbar);
        toolbar.setTitle(R.string.nav_about);
        toolbar.setNavigationOnClickListener(v ->
                startActivity(new Intent(AboutUsActivity.this, MainActivity.class))
        );

        feed_back = findViewById(R.id.about_us_feed_back);
        Intent intent = new Intent();
        feed_back.setOnClickListener(v ->  //反馈点击事件
                {
                    intent.setClass(AboutUsActivity.this, AgentWebActivity.class);
                    intent.setData(Uri.parse(UrlContainer.FEED_BACK));
                    intent.putExtra(ConstName.TITLE, R.string.feed_back);
                    intent.putExtra(ConstName.ACTIVITY, ConstName.activity.ABOUT_US);
                    startActivity(intent);
                }
        );
    }
}
