package com.example.lorebase.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.lorebase.BaseActivity;
import com.example.lorebase.MyApplication;
import com.example.lorebase.R;
import com.example.lorebase.adapter.BrowseHistoryAdapter;
import com.example.lorebase.bean.BrowseHistory;
import com.example.lorebase.greenDao.BrowseHistoryDao;
import com.example.lorebase.util.DividerItemGridDecoration;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BrowseHistoryActivity extends BaseActivity {

    private List<BrowseHistory> browseHistoryList;
    private FloatingActionButton fab_delete, fab_top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_history);
        initView();
    }

    @SuppressLint("RestrictedApi")
    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar_browse_history);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.browse_history__collapsing_toolbar);
        ImageView portrait = findViewById(R.id.browse_history_portrait_image_view);
        RecyclerView recyclerView = findViewById(R.id.browse_history_list);
        fab_top = findViewById(R.id.fab_browse_history_top);
        fab_delete = findViewById(R.id.fab_browse_history_delete);
        NestedScrollView nestedScrollView = findViewById(R.id.nest_scroll_bh);

        toolbar.setNavigationOnClickListener(v -> finish());
        collapsingToolbarLayout.setTitle("Browsing History");
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.BLACK);
        collapsingToolbarLayout.setBackgroundColor(Color.GRAY);
        Glide.with(this).load(R.drawable.image_timetree).into(portrait);

        GridLayoutManager manager = new GridLayoutManager(this, 1);
        BrowseHistoryDao browseHistoryDao = MyApplication.getDaoSession().getBrowseHistoryDao();
        browseHistoryList = browseHistoryDao.queryBuilder().list();
        BrowseHistoryAdapter adapter = new BrowseHistoryAdapter(browseHistoryList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemGridDecoration(this));
        fab_delete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.tip)
                    .setIcon(R.mipmap.ic_launcher_round)
                    .setMessage(R.string.tip_content_clear_history)
                    .setPositiveButton(R.string.ok, (dialog, which) -> {
                        browseHistoryDao.deleteAll();
                        if (browseHistoryList != null) {
                            browseHistoryList.clear();
                        }
                        fab_delete.setVisibility(View.INVISIBLE);
                        fab_top.setVisibility(View.INVISIBLE);
                        adapter.notifyDataSetChanged();
                        Glide.with(this).load(R.drawable.empty_cup).into(portrait);
                    }); //清空数据库
            builder.create().show(); //遗漏
        });

        fab_top.setOnClickListener(v -> nestedScrollView.post(() -> nestedScrollView.fullScroll(View.FOCUS_UP)));

        if (browseHistoryList.size() == 0) {
            Glide.with(this).load(R.drawable.empty_cup).into(portrait);
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onResume() {
        super.onResume();
        fab_delete.setVisibility(browseHistoryList.size() == 0 ? View.INVISIBLE : View.VISIBLE);
        fab_top.setVisibility(browseHistoryList.size() < 15 ? View.INVISIBLE : View.VISIBLE);
    }
}
