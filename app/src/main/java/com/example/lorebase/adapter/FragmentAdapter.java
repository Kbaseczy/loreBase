package com.example.lorebase.adapter;

import com.example.lorebase.bean.LoreTree;
import com.example.lorebase.ui.fragment.subFragment.LoreListFragment;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentStatePagerAdapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class FragmentAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;

    //接收来自LoreActivity的数据（二级目录的数据，chapterName,chapterId)
    private List<LoreTree.DataBean.ChildrenBean> childrenBeanList;

    public FragmentAdapter(FragmentManager fm,List<Fragment> fragments,
                           List<LoreTree.DataBean.ChildrenBean> childrenBeanList) {
        super(fm);
        this.fragments = fragments;
        this.childrenBeanList = childrenBeanList;
    }


    @Override
    public Fragment getItem(int position) {

//        return LoreListFragment.instantiate(childrenBeanList.get(position).getId());
        return fragments.get(position);
    }


    @Override
    public int getCount() {
        return childrenBeanList != null ? childrenBeanList.size() : 0;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return childrenBeanList.get(position).getName();
    }

    public List<LoreTree.DataBean.ChildrenBean> getChildrenBeanList() {
        return childrenBeanList;
    }

    public void setChildrenBeanList(List<LoreTree.DataBean.ChildrenBean> childrenBeanList) {
        this.childrenBeanList = childrenBeanList;
    }
}
