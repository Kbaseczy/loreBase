package com.example.wokeshihuimaopaodekafei.myapplication2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.ListView;

public class NetActivity extends AppCompatActivity {

    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_net);
       lv= (ListView) findViewById(R.id.net_listview);
       GoodsTask task = new GoodsTask(this,lv);
        String url = "http://192.168.56.1:8080/vivo/goods/newGoodsForMobile.action";
        task.execute(url);
    }
}




