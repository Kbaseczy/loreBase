package com.example.lorebase.ui.activity;

import android.app.Activity;
import android.os.Bundle;

import com.example.lorebase.R;
import com.example.lorebase.adapter.NavigationAdapter;
import com.example.lorebase.bean.NavigateSite;
import com.example.lorebase.contain_const.UrlContainer;
import com.example.lorebase.util.DividerItemGridDecoration;
import com.example.lorebase.util.L;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Request;

/*
     // todo 增加体验 -> 提供其他视图，方便浏览
 */
public class NavigationActivity extends Activity {
    private List<NavigateSite.DataBean> beans_chapter;
    private Map<Integer, String> keys = new HashMap<>();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        recyclerView = findViewById(R.id.recycler_navigation);
        getNavigation();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.navigation);
        toolbar.setNavigationOnClickListener(v -> finish());

        FloatingActionButton floatingActionButton = findViewById(R.id.fab_navigate);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        NavigationAdapter adapter = new NavigationAdapter(beans_chapter);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

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
                        initView();
                    }
                });
    }
}
