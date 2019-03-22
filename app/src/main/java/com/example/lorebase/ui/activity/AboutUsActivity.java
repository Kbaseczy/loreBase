package com.example.lorebase.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

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
        toolbar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(AboutUsActivity.this, MainActivity.class));
            overridePendingTransition(R.animator.go_in, R.animator.go_out);
        });
        feed_back = findViewById(R.id.about_us_feed_back);
    }


    public void onClickGithub(View view) {
        Intent intent = new Intent();
        intent.setClass(AboutUsActivity.this, AgentWebActivity.class);
        intent.setData(Uri.parse(UrlContainer.GITHUB));
        intent.putExtra(ConstName.TITLE, R.string.githb);
        intent.putExtra(ConstName.ACTIVITY, ConstName.activity.ABOUT_US);
        startActivity(intent);
    }

    public void onClickEmail(View view) {
        Intent data = new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailto:folio96@163.com"));
        data.putExtra(Intent.EXTRA_SUBJECT, "在此处输入主题（该部分可删除）");
        data.putExtra(Intent.EXTRA_TEXT, "在此处输入正文（该部分可删除）：");
        startActivity(data);
    }

    public void onClickFab(View view) {
        Intent intent = new Intent();
        intent.setClass(AboutUsActivity.this, AgentWebActivity.class);
        intent.setData(Uri.parse(UrlContainer.FEED_BACK));
        intent.putExtra(ConstName.TITLE, R.string.feed_back);
        intent.putExtra(ConstName.ACTIVITY, ConstName.activity.ABOUT_US);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        return super.onKeyDown(keyCode, event);
    }
}
