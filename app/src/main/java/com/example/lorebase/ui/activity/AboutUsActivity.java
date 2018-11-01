package com.example.lorebase.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.lorebase.R;
import com.example.lorebase.contain_const.UrlContainer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AboutUsActivity extends Activity {

    FloatingActionButton feed_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        feed_back = findViewById(R.id.about_us_feed_back);
        feed_back.setOnClickListener(v->  //反馈点击事件
                    startActivity(new Intent(AboutUsActivity.this,
                            AgentWebActivity.class).setData(Uri.parse(UrlContainer.FEED_BACK)))
        );


    }
}
