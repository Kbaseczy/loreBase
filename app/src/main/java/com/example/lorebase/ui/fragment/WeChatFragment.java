package com.example.lorebase.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lorebase.MyApplication;
import com.example.lorebase.R;
import com.example.lorebase.bean.WeChat;
import com.example.lorebase.http.RetrofitApi;
import com.example.lorebase.ui.fragment.subFragment.WeChatArticleFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Callback;
import retrofit2.Response;

public class WeChatFragment extends Fragment {
    private View view;
    private List<WeChat.DataBean> list_weChat;

    public static WeChatFragment getInstance() {
        WeChatFragment fragment = new WeChatFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_we_chat, null);
        getWeChat();
        return view;
    }

    private void getWeChat() {
        RetrofitApi api = MyApplication.retrofit.create(RetrofitApi.class);
        retrofit2.Call<WeChat> chatCall = api.getWeChatChapter();
        chatCall.enqueue(new Callback<WeChat>() {
            @Override
            public void onResponse(retrofit2.Call<WeChat> call, Response<WeChat> response) {
                if (response.body() != null) {
                    list_weChat = response.body().getData();
                    initWeChat();
                }

            }

            @Override
            public void onFailure(retrofit2.Call<WeChat> call, Throwable t) {

            }
        });
    }

    private void initWeChat() {
        ViewPager viewPager = view.findViewById(R.id.viewpager_we_chat);
        TabLayout tabLayout = view.findViewById(R.id.tab_we_chat);

        for (WeChat.DataBean wechat : list_weChat) {
            tabLayout.addTab(tabLayout.newTab().setText(wechat.getName()));
        }

        List<Fragment> fragments = new ArrayList<>();
        for (WeChat.DataBean wechat : list_weChat) {
            fragments.add(WeChatArticleFragment.getInstance(wechat.getId()));
        }

        FragmentStatePagerAdapter adapterWeChat = new FragmentStatePagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return list_weChat != null ? list_weChat.size() : 0;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return list_weChat.get(position).getName();
            }
        };

        viewPager.setAdapter(adapterWeChat);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
    }
}
