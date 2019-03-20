package com.example.lorebase;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.example.lorebase.util.ActivityCollector;

import androidx.appcompat.app.AppCompatActivity;
import skin.support.app.SkinCompatActivity;

public class BaseActivity extends SkinCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        ActivityCollector.addActivtity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
