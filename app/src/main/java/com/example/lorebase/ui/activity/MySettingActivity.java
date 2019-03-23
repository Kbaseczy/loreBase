package com.example.lorebase.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;

import com.example.lorebase.BaseActivity;
import com.example.lorebase.R;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

public class MySettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_setting);
        Toolbar toolbar = findViewById(R.id.toolbar_setting);

        toolbar.setTitle(R.string.setting);
        toolbar.setNavigationOnClickListener((view) -> {
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(R.animator.go_in, R.animator.go_out);
        });


    }

    @Override
    protected void onResume() {
        setNightMode();
        super.onResume();
    }

    private void setNightMode() {
        if (PreferenceManager
                .getDefaultSharedPreferences(this)
                .getBoolean("setting_switch_skin", true)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        return super.onKeyDown(keyCode, event);
    }
}
