package com.example.lorebase.adapter;


import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class HomeTabAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;

    private String[] title;

    public HomeTabAdapter(FragmentManager fm, List<Fragment> fragments,
                          String[] title) {
        super(fm);
        this.fragments = fragments;
        this.title = title;
    }


    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }


    @Override
    public int getCount() {
        return title != null ? title.length : 0;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
