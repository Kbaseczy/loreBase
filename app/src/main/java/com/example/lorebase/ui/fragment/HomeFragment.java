package com.example.lorebase.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ajguan.library.EasyRefreshLayout;
import com.example.lorebase.R;
import com.example.lorebase.adapter.HomeAdapter;
import com.example.lorebase.bean.Article;
import com.example.lorebase.bean.Banner;
import com.example.lorebase.bean.News;
import com.example.lorebase.contain_const.UrlContainer;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 * 1发出请求request——>2得到响应response——>3解析数据——>4List容纳数据——>5适配器适配到recyclerView
 * <p>
 * todo 子RecyclerView 抢夺焦点问题。滑动完全交给NestScrollView 或者 父RecyclerView
 * ·android:focusableInTouchMode="true"
 * ·android:focusable="true"
 */
public class HomeFragment extends Fragment {
    private View view;
    private int page = 0;
    private List<Banner.DataBean> banner_t;
    private List<News.DataBean> beanList_news;
    private List<Article.DataBean.DatasBean> beanList_article;
    public static RecyclerView recyclerView;
    private EasyRefreshLayout easyRefreshLayout;
    private HomeAdapter adapter;


    public static HomeFragment getInstantce() {
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
        easyRefreshLayout = view.findViewById(R.id.easy_refresh_home);
        easyRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                getDataList();
            }

            @Override
            public void onRefreshing() {
                getDataList();
            }
        });
        super.onResume();
    }

    private void initView() {
        recyclerView = view.findViewById(R.id.recycler_home);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        adapter = new HomeAdapter(getActivity(), banner_t, beanList_news, beanList_article);
        adapter.addList(banner_t, beanList_news, beanList_article);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.setBackgroundColor(Color.WHITE);
    }

    private void getDataList() {
        new Handler().postDelayed(() -> {
            if (easyRefreshLayout.isRefreshing()) {
                page = 0;
                getArticle();
                adapter.addList(banner_t, beanList_news, beanList_article);
                easyRefreshLayout.refreshComplete();
            } else {
                page++;
                getArticle();
                adapter.addList(banner_t, beanList_news, beanList_article);
                easyRefreshLayout.loadMoreComplete();
            }
        }, 500);
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

}
