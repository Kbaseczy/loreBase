package com.example.lorebase.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lorebase.R;
import com.example.lorebase.bean.WeChat;
import com.example.lorebase.contain_const.UrlContainer;
import com.example.lorebase.ui.fragment.subFragment.WeChatArticleFragment;
import com.example.lorebase.util.L;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import okhttp3.Call;
import okhttp3.Request;

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
        String url = UrlContainer.baseUrl + UrlContainer.WX_ARTICLE_CHAPTER;
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        L.v("WE_CHAT", response);
                        Gson gson = new Gson();
                        list_weChat = gson.fromJson(response, WeChat.class).getData();
                        initWeChat();
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

    //计划数据恢复，重回时界面空白
    /*@Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        L.v("onSaveInstanceState test");
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = null;
        if (manager != null) {
            transaction = manager.beginTransaction();
        }
        for(WeChat.DataBean wechat : list_weChat){
            if(WeChatArticleFragment.getInstance(wechat.getId()) != null){
                if (transaction != null) {
                    transaction.hide(WeChatArticleFragment.getInstance(wechat.getId()));
                    transaction.commitAllowingStateLoss();
                }
            }
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = null;
        if (manager != null) {
            transaction = manager.beginTransaction();
        }
        for(WeChat.DataBean wechat : list_weChat){
            if(WeChatArticleFragment.getInstance(wechat.getId()) != null){
                if (transaction != null) {
                    transaction.hide(WeChatArticleFragment.getInstance(wechat.getId()));
                    transaction.commitAllowingStateLoss();
                }
            }
        }
        super.onViewStateRestored(savedInstanceState);
    }
*/
}
