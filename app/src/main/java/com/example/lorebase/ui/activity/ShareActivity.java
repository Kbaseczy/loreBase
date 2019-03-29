package com.example.lorebase.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.lorebase.BaseActivity;
import com.example.lorebase.MyApplication;
import com.example.lorebase.R;
import com.example.lorebase.adapter.ShareAdapter;
import com.example.lorebase.bean.ShareHistory;
import com.example.lorebase.greenDao.ShareHistoryDao;
import com.example.lorebase.http.RetrofitUtil;
import com.example.lorebase.util.ActivityCollector;
import com.example.lorebase.util.EmptyUtil;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShareActivity extends BaseActivity {

    ShareHistoryDao shareHistoryDao;
    List<ShareHistory> shareHistoryList;
    FloatingActionButton fab;
    NestedScrollView nest;
    ShareAdapter adapter;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ActivityCollector.addActivtity(this);
        shareHistoryDao = MyApplication.getDaoSession().getShareHistoryDao();
        shareHistoryList = shareHistoryDao.queryBuilder().list();
        initView();
        if (shareHistoryList.size() != 0) {
            initRecycler();
        } else {
            EmptyUtil.goEmpty(getSupportFragmentManager(), R.id.coordinator_share);
            fab.setVisibility(View.GONE);
        }
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar_share);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_share);
        ImageView portrait = findViewById(R.id.image_share);

        nest = findViewById(R.id.nest_share);
        fab = findViewById(R.id.fab_share_top);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle("我的分享");
        collapsingToolbarLayout.setCollapsedTitleTextColor(getColor(R.color.item_title));
        RetrofitUtil.getBiYing(this, portrait);
    }

    private void initRecycler() {
        RecyclerView recyclerView = findViewById(R.id.share_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        adapter = new ShareAdapter(this);
        adapter.setShareHistoryList(shareHistoryList);
        recyclerView.setAdapter(adapter);
        fab.setOnClickListener(view -> nest.post(() -> nest.fullScroll(View.FOCUS_UP)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_todo, menu);
        menu.findItem(R.id.action_add_todo).setIcon(R.drawable.ic_delete);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_todo:
                shareHistoryDao.deleteAll();
                adapter.notifyDeleteAll();
                EmptyUtil.goEmpty(getSupportFragmentManager(), R.id.coordinator_share);
                fab.setVisibility(View.GONE);
                break;
            case android.R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(R.animator.go_in, R.animator.go_out);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
