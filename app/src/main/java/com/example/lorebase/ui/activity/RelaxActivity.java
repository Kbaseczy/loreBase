package com.example.lorebase.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lorebase.BaseActivity;
import com.example.lorebase.R;
import com.example.lorebase.ui.fragment.subFragment.RelaxListFragment;
import com.example.lorebase.util.ActivityCollector;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * <p>
 * 1.设置自定义View 失败
 * 2.视频播放不出
 */
public class RelaxActivity extends BaseActivity {

    private int images[] = {R.drawable.icon_tab, R.drawable.icon_tab2, R.drawable.icon_tab3};
    private String[] title = {"安卓", "网络", "UI"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relax);
        ActivityCollector.addActivtity(this);

        initRelax();
    }

    private void initRelax() {
        Toolbar toolbar = findViewById(R.id.toolbar_lore);
        toolbar.setTitle(R.string.relax);
        toolbar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(R.animator.go_in, R.animator.go_out);
        });

        ViewPager viewPager = findViewById(R.id.vp_relax);
        TabLayout tabLayout = findViewById(R.id.tab_lore_title);

        int[] identity = {0, 1, 2};

        List<Fragment> fragments = new ArrayList<>();
        for (int ident : identity) {
            fragments.add(RelaxListFragment.getInstance(ident));
        }

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return title[position];
            }
        };

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < title.length; i++) {
            Objects.requireNonNull(tabLayout.getTabAt(i)).setCustomView(customTab(i));
        }
    }

    private View customTab(int position) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        ImageView imageView = view.findViewById(R.id.custom_tab_image);
        TextView textView = view.findViewById(R.id.custom_tab_title);
//        imageView.setImageResource(images[position]);
        Glide.with(this).load(images[position]).into(imageView);
        textView.setText(title[position]);
        return view;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        return super.onKeyDown(keyCode, event);
    }

    //    @Override
//    public void onBackPressed() {
//        int[] identity = {0, 1, 2};
//        for (int id : identity) {
//            if (RelaxListFragment.getInstance(id).onBackPressed())
//                return;
//        }
//        finish();
//    }

}
