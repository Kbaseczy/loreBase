package com.example.lorebase.ui.fragment;


import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import okhttp3.Call;
import okhttp3.Request;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lorebase.R;
import com.example.lorebase.adapter.FragmentAdapterWeChat;
import com.example.lorebase.bean.WeChat;
import com.example.lorebase.contain_const.UrlContainer;
import com.example.lorebase.ui.fragment.subFragment.WeChatArticleFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

public class WeChatFragment extends Fragment {
    private View view;
    private List<WeChat.DataBean> list_weChat;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_we_chat,null);
        getWeChat();
        return view;
    }

    private void getWeChat(){
        String url = UrlContainer.baseUrl+UrlContainer.WX_ARTICLE_CHAPTER;
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
                        Log.v("WE_CHAT", response);
                        Gson gson = new Gson();
                        list_weChat = gson.fromJson(response, WeChat.class).getData();
                        initWeChat();
                    }
                });
    }

    private void initWeChat() {
        ViewPager viewPager = view.findViewById(R.id.viewpager_we_chat);
        TabLayout tabLayout = view.findViewById(R.id.tab_we_chat);

        for(WeChat.DataBean wechat : list_weChat){
            tabLayout.addTab(tabLayout.newTab().setText(wechat.getName()));
        }

        List<Fragment> fragments = new ArrayList<>();
        for(WeChat.DataBean wechat : list_weChat){
            fragments.add(WeChatArticleFragment.intance(wechat.getId()));
        }

        FragmentAdapterWeChat adapterWeChat = new FragmentAdapterWeChat(getFragmentManager(),fragments,list_weChat);

        viewPager.setAdapter(adapterWeChat);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);
    }
}
