package com.example.lorebase.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Request;

public class MyselfActivity extends BaseActivity {

    private List<Article.DataBean.DatasBean> datasBeanList;
    private NestedScrollView nestedScrollView;
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself);
        getCollect();
//        LoadAndRefresh();
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
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        ImageView portrait = findViewById(R.id.portrait_image_view);
        RecyclerView recyclerView = findViewById(R.id.my_collect_list);
        FloatingActionButton fab_note = findViewById(R.id.fab_myself_note);
        FloatingActionButton fab_top = findViewById(R.id.fab_myself_top);
        EasyRefreshLayout easyRefreshLayout = findViewById(R.id.easy_refresh_myself);
        nestedScrollView = findViewById(R.id.nest_scroll_myself);

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
        MyselfAdapter adapter = new MyselfAdapter(datasBeanList);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemGridDecoration(this));
        fab_top.setOnClickListener(v -> nestedScrollView.post(() -> nestedScrollView.fullScroll(View.FOCUS_UP)));

        easyRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {

            }

            @Override
            public void onRefreshing() {

            }
        });
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

    @SuppressLint("RestrictedApi")
    @Override
    protected void onResume() {
        super.onResume();
    }
}
