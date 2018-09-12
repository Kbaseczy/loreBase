package com.example. wokeshihuimaopaodekafei.myapplication2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

//import com.example.wokeshihuimaopaodekafei.myapplication2.DataControl;
//import com.example.wokeshihuimaopaodekafei.myapplication2.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ShopList extends AppCompatActivity {
    private ListView listView;
    private ArrayList<HashMap<String, String>> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopdetail);
        listView = (ListView) findViewById(R.id.listViewr);
        data .add(DataControl.getHashMap(R.drawable.dianpu1 +"","vivo商城一店","星海广场","84832241"));
        data.add(DataControl.getHashMap(R.drawable.dianpu2 + "", "vivo商城二店", "胜利广场", "84832242"));
        data.add(DataControl.getHashMap(R.drawable.dianpu3 + "", "vivo商城三店", "柏威年", "84832243"));
        data.add(DataControl.getHashMap(R.drawable.dianpu4 + "", "vivo商城四店", "百年城", "84832244"));
        data.add(DataControl.getHashMap(R.drawable.dianpu5 + "", "vivo商城五店", "学苑广场", "84832245"));

        SimpleAdapter adapter = new SimpleAdapter(
                this,
                data,
                R.layout.shopdetail2,
                new String[]{"img", "storeName", "address", "telephone"},
                new int[]{R.id.img, R.id.storeName, R.id.address, R.id.telephone}
        );
        listView.setAdapter(adapter);
    }
}