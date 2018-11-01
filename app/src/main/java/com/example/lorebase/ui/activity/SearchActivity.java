package com.example.lorebase.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;

import com.example.lorebase.R;

public class SearchActivity extends AppCompatActivity {

    Toolbar toolbar_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        toolbar_search = findViewById(R.id.toolbar_search);

        toolbar_search.inflateMenu(R.menu.menu_search);
        toolbar_search.setNavigationOnClickListener(v->
            startActivity(new Intent().setClass(SearchActivity.this, MainActivity.class))
        );

//        toolbar_search.setOnMenuItemClickListener(item -> {
//            if(item .getItemId() == R.id.action_search){
//                Toast.makeText(SearchActivity.this, "Search action", Toast.LENGTH_SHORT).show();
//            }
//            return false;
//        });
    }

}
