package com.example.lorebase.ui.fragment.subFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ajguan.library.EasyRefreshLayout;
import com.example.lorebase.MyApplication;
import com.example.lorebase.R;
import com.example.lorebase.adapter.WeChatArticleAdapter;
import com.example.lorebase.bean.Article;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.http.RetrofitApi;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Callback;
import retrofit2.Response;

public class WeChatArticleFragment extends Fragment {

    private int we_chat_id, page;
    private List<Article.DataBean.DatasBean> beanList_WeChatArticle;
    private View view;
    private WeChatArticleAdapter articleAdapter;
    public static RecyclerView recyclerView;
    private EasyRefreshLayout easyRefreshLayout;
    @SuppressLint("StaticFieldLeak")
    public static NestedScrollView nestedScrollView;

    public static WeChatArticleFragment getInstance(int we_chat_id) {
        WeChatArticleFragment weChatArticleFragment = new WeChatArticleFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ConstName.ID, we_chat_id);
        weChatArticleFragment.setArguments(bundle);
        return weChatArticleFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            we_chat_id = getArguments().getInt(ConstName.ID, 0);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_we_chat_article, container, false);
        getWeChatArticle(we_chat_id);
        return view;
    }

    private void getWeChatArticle(int we_chat_id) {
        RetrofitApi api = MyApplication.retrofit.create(RetrofitApi.class);
        retrofit2.Call<Article> chatArticleCall = api.getWXList(we_chat_id,page);
        chatArticleCall.enqueue(new Callback<Article>() {
            @Override
            public void onResponse(retrofit2.Call<Article> call, Response<Article> response) {
                if (response.body() != null) {
                    beanList_WeChatArticle = response.body().getData().getDatas();
                    initWeChatArticle();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Article> call, Throwable t) {

            }
        });

    }

    private void initWeChatArticle() {
        recyclerView = view.findViewById(R.id.recycler_we_chat);
        nestedScrollView = view.findViewById(R.id.nest_scroll_we_chat);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 1);
        articleAdapter = new WeChatArticleAdapter(getActivity(), beanList_WeChatArticle);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(articleAdapter);
    }

    private void getDataList() {
        new Handler().postDelayed(() -> {
            if (easyRefreshLayout.isRefreshing()) {
                page = 0;
                getWeChatArticle(we_chat_id);
                articleAdapter.setWe_chat_article_list(beanList_WeChatArticle);
                easyRefreshLayout.refreshComplete();
            } else {
                page++;
                getWeChatArticle(we_chat_id);
                articleAdapter.setWe_chat_article_list(beanList_WeChatArticle);
                easyRefreshLayout.loadMoreComplete();
            }
        }, 1000);
    }

    @Override
    public void onResume() {
        easyRefreshLayout = view.findViewById(R.id.easy_refresh_we_chat_article);
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
}
