package com.example.lorebase.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.lorebase.BaseActivity;
import com.example.lorebase.R;
import com.example.lorebase.adapter.SearchListAdapter;
import com.example.lorebase.bean.Article;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.contain_const.UrlContainer;
import com.google.gson.Gson;
import com.scwang.smartrefresh.header.FlyRefreshHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.utils.L;

import java.util.List;

import androidx.appcompat.widget.Toolbar;
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
    private List<Article.DataBean.DatasBean> search_list;
    static RecyclerView recyclerView;

    //Fragment方式  接收数据
//    public SearchListActivity instance(String key_word){
//        SearchListActivity instance_frag = new SearchListActivity();
//        Bundle bundle = new Bundle();
//        bundle.putString(ConstName.KEY_WORD,key_word);
//        instance_frag.setArguments(bundle);
//        return instance_frag;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar_search);
        String key_word = getIntent().getStringExtra(ConstName.KEY_WORD);
        toolbar.setTitle(key_word);
        toolbar.setNavigationOnClickListener(v -> finish());
        search(key_word);  // zai 該方法中運行了initSearch()
    }

    private void initSearch() {
        recyclerView = findViewById(R.id.lore_rv);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        SearchListAdapter adapter = new SearchListAdapter(search_list);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        SmartRefreshLayout smartRefreshLayout = findViewById(R.id.smart_refresh_lore);
        smartRefreshLayout.setRefreshHeader(new FlyRefreshHeader(this));
        smartRefreshLayout.setRefreshFooter(new BallPulseFooter(this));
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            search_list.clear();
            adapter.notifyDataSetChanged();
            refreshLayout.finishRefresh();
        });
        smartRefreshLayout.autoLoadMore(200);
        smartRefreshLayout.finishLoadMoreWithNoMoreData();
//                setOnLoadMoreListener(refreshLayout -> {
//            page++;//修复
//            search(key_word);
//            adapter.notifyDataSetChanged();
//            refreshLayout.finishLoadMore();
//        });
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
                        L.e(response);
                        Gson gson = new Gson();
                        search_list = gson.fromJson(response, Article.class).getData().getDatas();
                        initSearch();
                    }
                });

    }

    // 启动该活动传递数据的封装。  一目了然传递了哪些数据
    public static void actionStart(Context context,String key_word){
        Intent intent = new Intent(context,SearchListActivity.class);
        intent.putExtra(ConstName.KEY_WORD,key_word);
        context.startActivity(intent);

    }
}