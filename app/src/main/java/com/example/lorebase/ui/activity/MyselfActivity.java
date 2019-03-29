package com.example.lorebase.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.ajguan.library.EasyRefreshLayout;
import com.example.lorebase.BaseActivity;
import com.example.lorebase.MyApplication;
import com.example.lorebase.R;
import com.example.lorebase.adapter.MyselfAdapter;
import com.example.lorebase.bean.Article;
import com.example.lorebase.http.RetrofitApi;
import com.example.lorebase.util.ActivityCollector;
import com.example.lorebase.util.EmptyUtil;
import com.example.lorebase.util.FileUtil;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Callback;
import retrofit2.Response;

public class MyselfActivity extends BaseActivity {

    private List<Article.DataBean.DatasBean> datasBeanList;
    private EasyRefreshLayout easyRefreshLayout;
    private int page = 0;
    private MyselfAdapter adapter;
    FloatingActionButton fab_note;
    FloatingActionButton fab_top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself);
        ActivityCollector.addActivtity(this);
        initView();
        getCollect();
    }

    @Override
    protected void onResume() {

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

    private void getCollect() {
        RetrofitApi api = MyApplication.retrofit.create(RetrofitApi.class);
        retrofit2.Call<Article> articleCall = api.getCollect(page);
        articleCall.enqueue(new Callback<Article>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onResponse(retrofit2.Call<Article> call, Response<Article> response) {
                if (response.body() != null) {
                    datasBeanList = response.body().getData().getDatas();
                    if (datasBeanList.size() != 0) {
                        initRecycler();
                    } else {
                        EmptyUtil.goEmpty(getSupportFragmentManager(), R.id.coordinator_myself);
                        fab_note.setVisibility(View.GONE);
                        fab_top.setVisibility(View.GONE);
                        easyRefreshLayout.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Article> call, Throwable t) {

            }
        });
    }

    private void initView() {
        easyRefreshLayout = findViewById(R.id.easy_refresh_myself);
        Toolbar toolbar = findViewById(R.id.toolbar_myself);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_myself);
        ImageView portrait = findViewById(R.id.portrait_image_view);

        fab_note = findViewById(R.id.fab_myself_note);

        setSupportActionBar(toolbar); //todo 1.导包 2.父类为 AppCompatActivity
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle("我的收藏");
        collapsingToolbarLayout.setCollapsedTitleTextColor(getColor(R.color.item_title));
        portrait.setImageDrawable(FileUtil.getDrawableImage(this)); //设置本地图片
        fab_note.setOnClickListener(view -> {
            startActivity(new Intent(this, TODOActivity.class));
            overridePendingTransition(R.animator.go_in, R.animator.go_out);
        });
    }

    private void initRecycler() {
        fab_top = findViewById(R.id.fab_myself_top);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        adapter = new MyselfAdapter(this, datasBeanList);
        RecyclerView recyclerView = findViewById(R.id.my_collect_list);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        fab_top.setOnClickListener(v -> recyclerView.scrollToPosition(0));
    }

    private void getDataList() {
        new Handler().postDelayed(() -> {
            if (easyRefreshLayout.isRefreshing()) {
                page = 0;
                getCollect();
                adapter.addDatasBeanList(datasBeanList);
                easyRefreshLayout.refreshComplete();
            } else {
                page++;
                getCollect();
                adapter.addDatasBeanList(datasBeanList);
                easyRefreshLayout.loadMoreComplete();
            }
        }, 500);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(R.animator.go_in, R.animator.go_out);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        return super.onKeyDown(keyCode, event);
    }
}
