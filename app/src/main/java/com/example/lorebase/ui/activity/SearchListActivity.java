package com.example.lorebase.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import com.ajguan.library.EasyRefreshLayout;
import com.example.lorebase.BaseActivity;
import com.example.lorebase.MyApplication;
import com.example.lorebase.R;
import com.example.lorebase.adapter.SearchListAdapter;
import com.example.lorebase.bean.Article;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.http.RetrofitApi;
import com.example.lorebase.ui.fragment.subFragment.EmptyFragment;
import com.example.lorebase.util.DividerItemGridDecoration;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Callback;
import retrofit2.Response;

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
    private List<Article.DataBean.DatasBean> search_list;
    private EmptyFragment emptyFragment;
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
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }
        search(key_word);  // zai 該方法中運行了initSearch()
    }

    @Override
    protected void onResume() {
        easyRefreshLayout = findViewById(R.id.easy_refresh_lore);
//        easyRefreshLayout.setRefreshHeadView(customizabaleView);
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

    private void initSearch() {
        RecyclerView recyclerView = findViewById(R.id.lore_rv);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        adapter = new SearchListAdapter(this, search_list);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemGridDecoration(this));
        FloatingActionButton fab = findViewById(R.id.fab_search_list);
        fab.setOnClickListener(v -> recyclerView.scrollToPosition(0));
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
        }, 500);
    }

    private void search(String keyWord) {
        RetrofitApi api = MyApplication.retrofit.create(RetrofitApi.class);
        retrofit2.Call<Article> searchArticleCall = api.getSearchArticle(page,keyWord);
        searchArticleCall.enqueue(new Callback<Article>() {
            @Override
            public void onResponse(retrofit2.Call<Article> call, Response<Article> response) {
                if (response.body() != null) {
                    search_list = response.body().getData().getDatas();
                    if (search_list.size() != 0) {
                        initSearch();
                    } else {
                        goEmpty();
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Article> call, Throwable t) {

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, SearchActivity.class));
                overridePendingTransition(R.animator.go_in, R.animator.go_out);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        emptyFragment = null;
    }
}