package com.example.lorebase.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.ajguan.library.EasyRefreshLayout;
import com.bumptech.glide.Glide;
import com.example.lorebase.BaseActivity;
import com.example.lorebase.R;
import com.example.lorebase.adapter.MyselfAdapter;
import com.example.lorebase.bean.Article;
import com.example.lorebase.contain_const.UrlContainer;
import com.example.lorebase.util.DividerItemGridDecoration;
import com.example.lorebase.util.L;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Request;

public class MyselfActivity extends BaseActivity {

    private List<Article.DataBean.DatasBean> datasBeanList;
    private EasyRefreshLayout easyRefreshLayout;
    private int page = 0;
    private MyselfAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself);
        getCollect();
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onResume() {
        easyRefreshLayout = findViewById(R.id.easy_refresh_myself);
        easyRefreshLayout.autoRefresh(100);
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
        String url = UrlContainer.baseUrl + "lg/collect/list/" + page + "/json";
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
                        L.v(response);
                        Gson gson = new Gson();
                        datasBeanList = gson.fromJson(response, Article.class).getData().getDatas();
                        initView();
                    }
                });
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar_myself);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_myself);
        ImageView portrait = findViewById(R.id.portrait_image_view);
        RecyclerView recyclerView = findViewById(R.id.my_collect_list);
        FloatingActionButton fab_note = findViewById(R.id.fab_myself_note);
        FloatingActionButton fab_top = findViewById(R.id.fab_myself_top);

        setSupportActionBar(toolbar); //todo 1.导包 2.父类为 AppCompatActivity
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); //todo
        }
        collapsingToolbarLayout.setTitle("Myself_Collection");
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.BLACK);
        collapsingToolbarLayout.setBackgroundColor(Color.BLUE);
        Glide.with(this).load(R.drawable.image_store).into(portrait);
        fab_note.setOnClickListener(view -> Toast.makeText(MyselfActivity.this, "悬浮注释输入框", Toast.LENGTH_SHORT).show());

        GridLayoutManager manager = new GridLayoutManager(this, 1);
        adapter = new MyselfAdapter(this, datasBeanList);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemGridDecoration(this));
        fab_top.setOnClickListener(v -> recyclerView.scrollToPosition(0));

    }

    private void getDataList() {
        new Handler().postDelayed(() -> {
            if (easyRefreshLayout.isRefreshing()) {
                page = 0;
                getCollect();
//                adapter.setBeanList_article(beanList_article);
                adapter.addDatasBeanList(datasBeanList);
                easyRefreshLayout.refreshComplete();
            } else {
                page++;
                getCollect();
//                adapter.setBeanList_article(beanList_article);
                adapter.addDatasBeanList(datasBeanList);
                easyRefreshLayout.loadMoreComplete();
            }
        }, 500);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
