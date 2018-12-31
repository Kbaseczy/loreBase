package com.example.lorebase.ui.fragment.subFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lorebase.R;
import com.example.lorebase.adapter.WeChatArticleAdapter;
import com.example.lorebase.bean.WeChatArticle;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.contain_const.UrlContainer;
import com.example.lorebase.util.DividerItemGridDecoration;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Request;

public class WeChatArticleFragment extends Fragment {

    private int we_chat_id, page;
    private List<WeChatArticle.DataBean.DatasBean> beanList_WeChatArticle;
    private View view;
    private WeChatArticleAdapter articleAdapter;
    public static RecyclerView recyclerView;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_we_chat_article, container, false);
        getWeChatArticle(we_chat_id);
        return view;
    }

    private void getWeChatArticle(int we_chat_id) {
        String url = UrlContainer.baseUrl + "wxarticle/list/" + we_chat_id + "/" + page + "/json";
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
                        Log.v("WE_CHAT", response);
                        Gson gson = new Gson();
                        beanList_WeChatArticle = gson.fromJson(response, WeChatArticle.class).getData().getDatas();
                        initWeChatArticle();
                    }
                });
    }

    private void initWeChatArticle() {
        recyclerView = view.findViewById(R.id.recycler_we_chat);
        SmartRefreshLayout smartRefreshLayout = view.findViewById(R.id.smart_refresh_we_chat_article);
        nestedScrollView = view.findViewById(R.id.nest_scroll_we_chat);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 1);
        articleAdapter = new WeChatArticleAdapter(getActivity(), beanList_WeChatArticle);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(articleAdapter);
        recyclerView.addItemDecoration(new DividerItemGridDecoration(Objects.requireNonNull(getContext())));
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(Objects.requireNonNull(getContext())));
        smartRefreshLayout.setRefreshFooter(new BallPulseFooter(getContext()));
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            beanList_WeChatArticle.clear();
            getWeChatArticle(we_chat_id);
            articleAdapter.notifyDataSetChanged();
            refreshLayout.finishRefresh();
        });
//        smartRefreshLayout.autoLoadMore(200);  //没有完全，自动加载。仍有没加载部分
        smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            page++;
            getWeChatArticle(we_chat_id); //include the initWeChatArticle();
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
