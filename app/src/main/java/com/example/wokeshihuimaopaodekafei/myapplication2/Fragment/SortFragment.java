package com.example.wokeshihuimaopaodekafei.myapplication2.Fragment;

//import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.wokeshihuimaopaodekafei.myapplication2.GoodsTask;
import com.example.wokeshihuimaopaodekafei.myapplication2.NetActivity;
import com.example.wokeshihuimaopaodekafei.myapplication2.R;

import java.util.List;
//import com.example.wokeshihuimaopaodekafei.myapplication2.ShopList;

public class SortFragment extends Fragment {

    Button button;
    ListView lv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.sortfragment, container, false);
        button=(Button)view.findViewById(R.id.button_to_net);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), NetActivity.class);
                startActivity(intent);
            }
        });
        lv = (ListView)view.findViewById(R.id.lv);
        GoodsTask task = new GoodsTask(getActivity(),lv);
        String url = "http://192.168.56.1:8080/vivo/goods/newGoodsForMobile.action";
        task.execute(url);

        return view;
    }
}
