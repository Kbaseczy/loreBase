package com.example.lorebase.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.ajguan.library.EasyRefreshLayout;
import com.example.lorebase.BaseActivity;
import com.example.lorebase.R;
import com.example.lorebase.adapter.SearchListAdapter;
import com.example.lorebase.bean.Article;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.contain_const.UrlContainer;
import com.example.lorebase.ui.fragment.subFragment.EmptyFragment;
import com.example.lorebase.util.DividerItemGridDecoration;
import com.example.lorebase.util.L;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Request;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 * sequence : key_word -> search(key_word) -> initSearch()
 */
@SuppressLint("Registered")
public class SearchListActivity extends BaseActivity {
    private int page;
    private String key_word;
    private List<Article.DataBean.DatasBean> search_list ;
    private EmptyFragment emptyFragment;
    private NestedScrollView nestedScrollView;
    private EasyRefreshLayout easyRefreshLayout;
    private SearchListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        emptyFragment = new EmptyFragment();
        Toolbar toolbar = findViewById(R.id.toolbar_search);
        key_word = getIntent().getStringExtra(ConstName.KEY_WORD);
        toolbar.setTitle(key_word);
        toolbar.setNavigationOnClickListener(v -> finish());
        search(key_word);  // zai 該方法中運行了initSearch()
    }

    private void initSearch() {
        RecyclerView recyclerView = findViewById(R.id.lore_rv);
        nestedScrollView = findViewById(R.id.nest_refresh_lore);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        adapter = new SearchListAdapter(this, search_list);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemGridDecoration(this));
        easyRefreshLayout = findViewById(R.id.easy_refresh_lore);
        easyRefreshLayout.autoRefresh();
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
        FloatingActionButton fab = findViewById(R.id.fab_search_list);
        fab.setOnClickListener(v -> nestedScrollView.fullScroll(View.FOCUS_UP));
    }

    private void getDataList() {
        new Handler().postDelayed(() -> {
            if (easyRefreshLayout.isRefreshing()) {
                page = 0;
                search(key_word);
                adapter.setSearch_list(search_list);
                easyRefreshLayout.refreshComplete();
            } else {
                page++;
                search(key_word);
                adapter.setSearch_list(search_list);
                easyRefreshLayout.loadMoreComplete();
            }
        }, 1000);
    }

    private void search(String keyWord) {
        String url = UrlContainer.baseUrl + "article/query/" + page + "/json";
        OkHttpUtils
                .post()
                .url(url)
                .addParams(ConstName.KEY_WORD, keyWord)
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
//                        L.e(response);
                        Gson gson = new Gson();
                        search_list = gson.fromJson(response, Article.class).getData().getDatas();
                        L.v("2000",search_list.size()+"  search size");
                        if (search_list.size() == 0) {
                            goEmpty();
                        } else {
                            initSearch();
                        }
                    }
                });

    }

    // 启动该活动传递数据的封装。  一目了然传递了哪些数据
    public static void actionStart(Context context, String key_word) {
        Intent intent = new Intent(context, SearchListActivity.class);
        intent.putExtra(ConstName.KEY_WORD, key_word);
        context.startActivity(intent);
    }

    private void goEmpty() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(
                R.animator.fragment_slide_left_enter,
                R.animator.fragment_slide_left_exit,
                R.animator.fragment_slide_right_exit,
                R.animator.fragment_slide_right_enter).
                replace(R.id.coordinator_search_result, emptyFragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        emptyFragment = null;
    }
}