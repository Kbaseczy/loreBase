package com.example.lorebase.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lorebase.BaseActivity;
import com.example.lorebase.R;
import com.zhengsr.viewpagerlib.anim.ZoomOutPageTransformer;
import com.zhengsr.viewpagerlib.bean.PageBean;
import com.zhengsr.viewpagerlib.indicator.ZoomIndicator;
import com.zhengsr.viewpagerlib.view.GlideViewPager;

import java.util.ArrayList;
import java.util.List;

/*
    1、如果单单使用ViewPager出现这种错误的情况，有可能是在此处出现问题的。
    viewpager获取布局尽量写在此方法中，不要在外部直接new 一个组件添加到List集合中，
    如果传入的情况下，父容器是没法remove代码中new的组件的。
//You must call removeView() on the child's parent first.
开源库'com.github.LillteZheng:ViewPagerHelper:v0.8'
 */
public class GuideActivity extends BaseActivity {

    int[] imageRes = new int[]{
            R.drawable.guide01,
            R.drawable.guide02,
            R.drawable.guide04
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        GlideViewPager viewPager = findViewById(R.id.splase_viewpager);
        ZoomIndicator zoomIndicator = findViewById(R.id.splase_bottom_layout);

        Button btn_start = findViewById(R.id.splase_start_btn);

        List<Integer> images = new ArrayList<>();
        for (int imageRe : imageRes) {
            images.add(imageRe);
        }

        PageBean bean = new PageBean.Builder<Integer>()
                .setDataObjects(images)
                .setIndicator(zoomIndicator)
                .setOpenView(btn_start)  //显示出按钮
                .builder();

        viewPager.setPageListener(bean, R.layout.item_guide, (view, data) -> {
            ImageView imageView = view.findViewById(R.id.image_guide);
            imageView.setBackgroundResource((Integer) data);
        });
        viewPager.setPageTransformer(false, new ZoomOutPageTransformer()); //设置动画效果

//        InitData();
//        btn_start = findViewById(R.id.start_guide);
//        viewPager = findViewById(R.id.viewpager_guide);
//        viewPager.setAdapter(new viewpagerAdapter());
//        viewPager.addOnPageChangeListener(new PageListener());
        btn_start.setOnClickListener(v -> {
            Intent goMain = new Intent(GuideActivity.this, MainActivity.class);
            startActivity(goMain);
            overridePendingTransition(R.animator.go_in, R.animator.go_out);
            Toast.makeText(GuideActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
            finish();
        });

    }
}