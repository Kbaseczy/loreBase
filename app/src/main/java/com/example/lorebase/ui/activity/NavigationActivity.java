package com.example.lorebase.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.example.lorebase.MyApplication;
import com.example.lorebase.R;
import com.example.lorebase.adapter.NavigationAdapter;
import com.example.lorebase.bean.NavigateSite;
import com.example.lorebase.http.RetrofitApi;
import com.example.lorebase.util.ActivityCollector;
import com.example.lorebase.util.DividerItemGridDecoration;
import com.example.lorebase.util.L;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Callback;
import retrofit2.Response;

public class NavigationActivity extends Activity {
    private List<NavigateSite.DataBean> beans_chapter;
    private Map<Integer, String> keys = new HashMap<>();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        ActivityCollector.addActivtity(this);
        recyclerView = findViewById(R.id.recycler_navigation);
        getNavigation();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.navigation);
        toolbar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(R.animator.go_in, R.animator.go_out);
        });

        FloatingActionButton floatingActionButton = findViewById(R.id.fab_navigate);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        NavigationAdapter adapter = new NavigationAdapter(beans_chapter);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        //测试，  根据关键词自动滑动到指定位置
        for (int i = 0; i < beans_chapter.size(); i++) {
            keys.put(i, beans_chapter.get(i).getName());
            L.v("stringSparseArray", keys.get(i) + "");
            if (Objects.equals(keys.get(i), "后端云")) {
                recyclerView.scrollToPosition(i);
                L.v("indexOfValue", i + "  jankin");
            }
        }

        recyclerView.addItemDecoration(new DividerItemGridDecoration(this));

        floatingActionButton.setOnClickListener(v -> recyclerView.scrollToPosition(0));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getNavigation() {
        RetrofitApi api = MyApplication.retrofit.create(RetrofitApi.class);
        retrofit2.Call<NavigateSite> navigateSiteCall = api.getNavigateSite();
        navigateSiteCall.enqueue(new Callback<NavigateSite>() {
            @Override
            public void onResponse(retrofit2.Call<NavigateSite> call, Response<NavigateSite> response) {
                if (response.body() != null) {
                    beans_chapter = response.body().getData();
                    initView();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<NavigateSite> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        return super.onKeyDown(keyCode, event);
    }
}
