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

import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Request;

public class NavigationActivity extends Activity {

    private List<NavigateSite.DataBean> beans_chapter;

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
        NavigationAdapter adapter = new NavigationAdapter(beans_chapter);
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
                        for (NavigateSite.DataBean articlesBean : beans_chapter) {
                            // 每组articles中数据个数的共性，得到i的上限->articlesBean.getArticles().size()
                            for (int i = 0; i < articlesBean.getArticles().size(); i++) {
                                if (articlesBean.getArticles().get(i).getTitle().length() != 0)
                                    L.v("title", articlesBean.getArticles().get(i).getTitle() + "  " +
                                            articlesBean.getArticles().get(i).getChapterName());
                            }
                        }
                        initView();
                    }
                });
    }
}
