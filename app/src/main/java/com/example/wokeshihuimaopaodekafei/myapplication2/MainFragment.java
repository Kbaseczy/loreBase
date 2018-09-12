package com.example.wokeshihuimaopaodekafei.myapplication2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.example.wokeshihuimaopaodekafei.myapplication2.Fragment.HomeFragment;
import com.example.wokeshihuimaopaodekafei.myapplication2.Fragment.MeFragment;
import com.example.wokeshihuimaopaodekafei.myapplication2.Fragment.SearchFragment;
import com.example.wokeshihuimaopaodekafei.myapplication2.Fragment.ShopCarFragment;
import com.example.wokeshihuimaopaodekafei.myapplication2.Fragment.SortFragment;
//import com.example.wokeshihuimaopaodekafei.myapplication2.R;

import java.util.ArrayList;
/*
  每次运行时候连接服务器都需要重新查询本机ip，
  在模拟器wifi里面设置手动代理器，代理ip即为本机ip（每次都需要重新查询）
 */
public class MainFragment extends FragmentActivity implements OnCheckedChangeListener {
    private ViewPager main_viewPager;
    private RadioGroup main_tab_RadioGroup;
    private RadioButton radiohome, radioshopcar, radiosort, radiome, radiosearch;
    private ArrayList<Fragment> fragmentList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // 界面初始函数，用来获取定义的各控件对应的ID
        InitView();
        // ViewPager初始化函数
        InitViewPager();
    }

    public void InitView() {
        main_tab_RadioGroup = (RadioGroup) findViewById(R.id.RadioGroup);
        radiohome = (RadioButton) findViewById(R.id.shouye);
        RadioButton btn0 = (RadioButton) (main_tab_RadioGroup.getChildAt(0));
        btn0.setChecked(true);
        radioshopcar = (RadioButton) findViewById(R.id.gouwuche);
        radiosort = (RadioButton) findViewById(R.id.fenlei);
        radiosearch = (RadioButton) findViewById(R.id.sousuo);
        radiome = (RadioButton) findViewById(R.id.wode);
        main_tab_RadioGroup.setOnCheckedChangeListener(this);

        /*
        添加监听器
         */
    }

    public void InitViewPager() {
        main_viewPager = (ViewPager) findViewById(R.id.main_ViewPager);
        fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new SortFragment());
        fragmentList.add(new ShopCarFragment());
        fragmentList.add(new SearchFragment());
        fragmentList.add(new MeFragment());
        main_viewPager.setAdapter(new MyAdapter(getSupportFragmentManager(), fragmentList));
        main_viewPager.setCurrentItem(0);
        main_viewPager.addOnPageChangeListener(new MyListener());
    }

    public class MyAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> list;

        public MyAdapter(FragmentManager fm, ArrayList<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int arg0) {
            return list.get(arg0);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

    public class MyListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            int current = main_viewPager.getCurrentItem();
            switch (current) {
                case 0:
                    main_tab_RadioGroup.check(R.id.shouye);
                    break;
                case 1:
                    main_tab_RadioGroup.check(R.id.gouwuche);
                    break;
                case 2:
                    main_tab_RadioGroup.check(R.id.fenlei);
                    break;
                case 3:
                    main_tab_RadioGroup.check(R.id.sousuo);
                    break;
                case 4:
                    main_tab_RadioGroup.check(R.id.wode);
                    break;
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
        int current = 0;
        switch (checkId) {
            case R.id.shouye:
                current = 0;
                break;
            case R.id.gouwuche:
                current = 1;
                break;
            case R.id.fenlei:
                current = 2;
                break;
            case R.id.sousuo:
                current = 3;
                break;
            case R.id.wode:
                current = 4;
                break;
        }
        if (main_viewPager.getCurrentItem() != current) {
            main_viewPager.setCurrentItem(current);
        }
    }

}
