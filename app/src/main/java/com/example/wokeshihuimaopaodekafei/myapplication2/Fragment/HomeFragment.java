package com.example.wokeshihuimaopaodekafei.myapplication2.Fragment;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
//import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

//import com.example.wokeshihuimaopaodekafei.myapplication2.GoodsList;
import com.example.wokeshihuimaopaodekafei.myapplication2.R;
import com.example.wokeshihuimaopaodekafei.myapplication2.ShopList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    //private SimpleAdapter simpleadapter;
    private ImageView img1;
    private ViewFlipper viewFlipper;
    private ImageView img2;
    private int[] resId = {R.drawable.huadong1, R.drawable.huadong2, R.drawable.huadong3, R.drawable.huadong1};
    private GridView gridview;
    private List<Map<String, Object>> ijh;
    private String[] iconName = {"vivoX9", "vivoX9lus", "vivoX9s", "vivoX9splus", "耳麦", "充电器"};
    private double[] iconPrice = {2399, 2699, 2999, 3299, 599, 99};
    private String[] iconPriceNow = {"特惠：2299", "特惠：2599", "特惠：2899", "特惠：3199", "特惠：499", "特惠：99"};
    private int[] icon = {R.drawable.vivo9,
            R.drawable.vivo9plus,
            R.drawable.vivox9s,
            R.drawable.vivo9splus,
            R.drawable.vivoy53,
            R.drawable.vivoy66,};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_home, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        img1 = (ImageView) getActivity().findViewById(R.id.img1);
        img2 = (ImageView) getActivity().findViewById(R.id.img2);
        gridview = (GridView) getActivity().findViewById(R.id.icon_group);
        viewFlipper = (ViewFlipper) getActivity().findViewById(R.id.flipper);
        img1.setOnClickListener(new View.OnClickListener() {//点击img1时触发一个效果
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();//intent是传递值
                intent.setClass(getActivity(), ShopList.class);
                startActivity(intent);
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "你点击的为vivoX9", Toast.LENGTH_SHORT).show();
            }
        });
        for (int i = 0; i < resId.length; i++) {
            viewFlipper.addView(getImageView(resId[i]));

        }
        viewFlipper.setInAnimation(getContext(), R.anim.left_in);
        viewFlipper.setOutAnimation(getContext(), R.anim.left_out);
        viewFlipper.setFlipInterval(1000);
        viewFlipper.startFlipping();

        ijh = new ArrayList<>();
        SimpleAdapter simpleadapat = new SimpleAdapter(getContext(), getA(), R.layout.listview3,
                new String[]{"img", "text1", "text2", "text3"}, new int[]{R.id.img,
                R.id.text1, R.id.text2, R.id.text});
        gridview.setAdapter(simpleadapat);


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private ImageView getImageView(int resId) {
        ImageView image = new ImageView(getContext());
        image.setBackgroundResource(resId);
        return image;

    }

    private List<Map<String, Object>> getA() {
        for (int i = 0; i < icon.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("img", icon[i]);
            map.put("text1", iconName[i]);
            map.put("text2", iconPrice[i]);
            map.put("text3", iconPriceNow[i]);
            ijh.add(map);
        }
        return ijh;
    }
}


