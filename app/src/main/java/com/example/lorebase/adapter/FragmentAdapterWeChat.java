package com.example.lorebase.adapter;

import com.example.lorebase.bean.WeChat;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class FragmentAdapterWeChat extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;

    private List<WeChat.DataBean> beanList_we_chat;

    public FragmentAdapterWeChat(FragmentManager fm, List<Fragment> fragments,
                                 List<WeChat.DataBean> childrenBeanList) {
        super(fm);
        this.fragments = fragments;
        this.beanList_we_chat = childrenBeanList;
    }


    @Override
    public Fragment getItem(int position) {

//        return LoreListFragment.instantiate(childrenBeanList.get(position).getId());
        return fragments.get(position);
    }


    @Override
    public int getCount() {
        return beanList_we_chat != null ? beanList_we_chat.size() : 0;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return beanList_we_chat.get(position).getName();
    }
}
