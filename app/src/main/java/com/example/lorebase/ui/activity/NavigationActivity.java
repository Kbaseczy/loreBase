package com.example.lorebase.ui.activity;

import android.app.Activity;
import android.os.Bundle;

import com.example.lorebase.R;
import com.example.lorebase.adapter.NavigationAdapter;
import com.example.lorebase.bean.NavigateSite;
import com.example.lorebase.contain_const.UrlContainer;
import com.example.lorebase.util.L;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Request;

public class NavigationActivity extends Activity {

    private List<NavigateSite.DataBean> beans_chapter;
    private List<NavigateSite.DataBean.ArticlesBean> beans_article = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        getNavigation();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.navigation);
        toolbar.setNavigationOnClickListener(v -> finish());
        RecyclerView recyclerView = findViewById(R.id.recycler_navigation);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        NavigationAdapter adapter = new NavigationAdapter(beans_chapter, beans_article);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    private void getNavigation() {
        String url = UrlContainer.baseUrl + UrlContainer.NAVI;
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
                        beans_chapter = gson.fromJson(response, NavigateSite.class).getData();
                        beans_article = gson.fromJson(response, NavigateSite.DataBean.class).getArticles();
                        for(NavigateSite.DataBean.ArticlesBean articlesBean:beans_article)
                            L.v("jankin_navisite",articlesBean.getTitle()+'/');

                        initView();
                    }
                });
    }
}
