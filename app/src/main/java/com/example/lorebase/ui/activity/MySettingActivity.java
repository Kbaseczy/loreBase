package com.example.lorebase.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.example.lorebase.BaseActivity;
import com.example.lorebase.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        return super.onKeyDown(keyCode, event);
    }
}
