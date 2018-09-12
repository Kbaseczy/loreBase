/*
package com.example.wokeshihuimaopaodekafei.myapplication2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;



public class Search extends AppCompatActivity {
    private GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        gridView = (GridView) findViewById(R.id.list);
        Bundle bundle = this.getIntent().getExtras();
        String name = bundle.getString("goods_name");
        String url = "http://10.0.2.2:8080/vivo/goods/search.action?goods_name="+name;
        GoodsTask goodsTask = new GoodsTask(this, gridView);
        goodsTask.execute(url);
    }
}*/
