package com.example.lorebase.adapter;

import com.example.lorebase.bean.ProjectChapter;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class FragmentAdapterProjectList extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;

    private List<ProjectChapter.DataBean> beans;
    public FragmentAdapterProjectList(FragmentManager fm, List<Fragment> fragments,
                                      List<ProjectChapter.DataBean> beans) {
        super(fm);
        this.fragments = fragments;
        this.beans = beans;
    }


    @Override
    public Fragment getItem(int position) {

//        return LoreListFragment.instantiate(childrenBeanList.get(position).getId());
        return fragments.get(position);
    }


    @Override
    public int getCount() {
        return beans != null ? beans.size() : 0;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return beans.get(position).getName();
    }
}
