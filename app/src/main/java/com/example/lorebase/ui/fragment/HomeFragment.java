package com.example.lorebase.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ajguan.library.EasyRefreshLayout;
import com.example.lorebase.MyApplication;
import com.example.lorebase.R;
import com.example.lorebase.adapter.HomeAdapter;
import com.example.lorebase.bean.Article;
import com.example.lorebase.bean.Banner;
import com.example.lorebase.bean.News;
import com.example.lorebase.http.RetrofitApi;
import com.example.lorebase.util.L;
import com.example.lorebase.util.TimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * 1发出请求request——>2得到响应response——>3解析数据——>4List容纳数据——>5适配器适配到recyclerView
 * <p>
 * ·android:focusableInTouchMode="true"
 * ·android:focusable="true"
 */
public class HomeFragment extends Fragment {
    private View view;
    private int page = 0;
    private List<Banner.DataBean> banner_t = new ArrayList<>();
    private List<News.DataBean> beanList_news = new ArrayList<>();
    private List<Article.DataBean.DatasBean> beanList_article = new ArrayList<>();
    public RecyclerView recyclerView;
    private EasyRefreshLayout easyRefreshLayout;
    private HomeAdapter adapter;

    public static HomeFragment getInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        adapter = new HomeAdapter(getActivity());
        adapter.setList(banner_t, beanList_news, beanList_article);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        easyRefreshLayout = view.findViewById(R.id.easy_refresh_home);
        easyRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
//                getDataList();
            }

            @Override
            public void onRefreshing() {
                getDataList();
            }
        });
    }

    private void getDataList() {
        new Handler().postDelayed(() -> {
            if (easyRefreshLayout.isRefreshing()) {
                page = 0;
                getArticle();
                adapter.addArticle(beanList_article);
                easyRefreshLayout.refreshComplete();
            } else {
                page++;
                getArticle();
                adapter.addArticle(beanList_article);
                easyRefreshLayout.loadMoreComplete();
            }
        }, 500);
    }

    private void getBanner() {
        RetrofitApi api = MyApplication.retrofit.create(RetrofitApi.class);
        retrofit2.Call<Banner> bannerCall = api.getHomeBanner();
        bannerCall.enqueue(new Callback<Banner>() {
            @Override
            public void onResponse(retrofit2.Call<Banner> call, Response<Banner> response) {
                if (response.body() != null) {
                    banner_t = response.body().getData();
                    getFlipper();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Banner> call, Throwable t) {

            }
        });
    }

    private void getFlipper() {
        RetrofitApi api = MyApplication.retrofit.create(RetrofitApi.class);
        retrofit2.Call<News> homeArticleCall = api.getHomeNews();
        homeArticleCall.enqueue(new Callback<News>() {
            @Override
            public void onResponse(retrofit2.Call<News> call, Response<News> response) {
                if (response.body() != null) {
                    beanList_news = response.body().getData();
                    getArticle();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<News> call, Throwable t) {

            }
        });
    }

    private void getArticle() {
        RetrofitApi api = MyApplication.retrofit.create(RetrofitApi.class);
        retrofit2.Call<Article> homeArticleCall = api.getHomeArticle(page);
        homeArticleCall.enqueue(new Callback<Article>() {
            @Override
            public void onResponse(retrofit2.Call<Article> call, Response<Article> response) {
                if (response.body() != null) {
                    beanList_article = response.body().getData().getDatas();
                    initView();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Article> call, Throwable t) {

            }
        });
    }

}
