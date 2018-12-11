package com.example.lorebase.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.lorebase.MyApplication;
import com.example.lorebase.R;
import com.example.lorebase.adapter.HomeAdapter;
import com.example.lorebase.adapter.HomeTabAdapter;
import com.example.lorebase.bean.Article;
import com.example.lorebase.bean.Banner;
import com.example.lorebase.bean.BrowseHistory;
import com.example.lorebase.bean.News;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.contain_const.UrlContainer;
import com.example.lorebase.ui.activity.AgentWebActivity;
import com.example.lorebase.ui.activity.NavigationActivity;
import com.example.lorebase.ui.activity.ProjectActivity;
import com.example.lorebase.ui.fragment.subFragment.HomeTabListFragment;
import com.example.lorebase.util.L;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import okhttp3.Call;
import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 * 1发出请求request——>2得到响应response——>3解析数据——>4List容纳数据——>5适配器适配到recyclerView
 */
public class HomeFragment extends Fragment {
    private View view;

    private int page = 0;

    private List<Banner.DataBean> banner_t = new ArrayList<>();
    private List<News.DataBean> beanList_news = new ArrayList<>();
    private List<Article.DataBean.DatasBean> beanList_article = new ArrayList<>();
    public static NestedScrollView nestedScrollView;
    public static RecyclerView recyclerView;
    private HomeAdapter adapter;
    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, null);
        getBanner();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView() {
        recyclerView = view.findViewById(R.id.recycler_home);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        adapter = new HomeAdapter(getActivity(),banner_t, beanList_news, beanList_article);
        /*adapter.addList(banner_t, beanList_news, beanList_article);
        adapter.notifyDataSetChanged();*/
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        nestedScrollView = view.findViewById(R.id.nest_scroll_home);
        nestedScrollView.fullScroll(View.FOCUS_UP);
    }

    private void getBanner() {
        String url = UrlContainer.baseUrl + UrlContainer.MAIN_BANNER;
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
                        Gson gson = new Gson();
                        //右边是解析成javaBean,右边是从javabean取出list，整体存到banner_t
                        banner_t = gson.fromJson(response, Banner.class).getData();
                        getFlipper();
                    }
                });
    }

    private void getFlipper() {
        String url = UrlContainer.baseUrl + UrlContainer.FRIEND;
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
                        Gson gson = new Gson();
                        beanList_news = gson.fromJson(response, News.class).getData();
                        getArticle();
                    }
                });
    }

    private void getArticle() {
        String url = UrlContainer.baseUrl + "article/list/" + page + "/json";
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
                        Gson gson = new Gson();
                        beanList_article = gson.fromJson(response, Article.class).getData().getDatas();
                        initView();
                    }
                });
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
//        HomeAdapter.Holder_banner
        super.onStop();
    }

}
