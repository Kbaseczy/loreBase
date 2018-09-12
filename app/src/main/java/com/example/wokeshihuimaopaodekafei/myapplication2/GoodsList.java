package com.example.wokeshihuimaopaodekafei.myapplication2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class GoodsList extends AppCompatActivity{
    private ListView listView01;
    private ArrayList<HashMap<String,String>> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goodslist);
        listView01=(ListView)findViewById(R.id.list);
        data.add(DataControl.getCar(R.drawable.sp1+"","vivoX9","很好用","2399","3.5f"));
        data.add(DataControl.getCar(R.drawable.sp2+"","vivoX9plus","很好用","2699","5f"));
        data.add(DataControl.getCar(R.drawable.sp3+"","vivoX9s","很好用","2999","1.5f"));
        data.add(DataControl.getCar(R.drawable.sp4+"","vivoe耳机","很好用","3299","2f"));
        data.add(DataControl.getCar(R.drawable.sp5+"","vivoXpsplus","很好用","599","3f"));
        data.add(DataControl.getCar(R.drawable.sp6+"","vivoY55","很好用","1999","1f"));
        MyAdapter myAdapter =new MyAdapter(this,data);
        listView01.setAdapter(myAdapter);
    }
}
