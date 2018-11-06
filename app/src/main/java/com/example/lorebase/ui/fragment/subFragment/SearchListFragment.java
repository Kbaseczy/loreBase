package com.example.lorebase.ui.fragment.subFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Request;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lorebase.R;
import com.example.lorebase.adapter.SearchListAdapter;
import com.example.lorebase.bean.Article;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.contain_const.UrlContainer;
import com.google.gson.Gson;
import com.scwang.smartrefresh.header.FlyRefreshHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.footer.FalsifyFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.header.FalsifyHeader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.utils.L;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class SearchListFragment extends Fragment {
    View view;
    private int page;
    private String key_word;
    private List<Article.DataBean.DatasBean> search_list;

    public SearchListFragment instance(String key_word){
        SearchListFragment instance_frag = new SearchListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ConstName.KEY_WORD,key_word);
        instance_frag.setArguments(bundle);
        return instance_frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_list, container, false);
        key_word = ""; //由searchActivity传入
        search(key_word);
        return view;
    }

    private void initSearch() {
        RecyclerView recyclerView = view.findViewById(R.id.lore_rv);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 1);
        SearchListAdapter adapter = new SearchListAdapter(search_list);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        SmartRefreshLayout smartRefreshLayout = view.findViewById(R.id.smart_refresh_lore);
        smartRefreshLayout.setRefreshHeader(new FlyRefreshHeader(getContext()));
        smartRefreshLayout.setRefreshFooter(new BallPulseFooter(getContext()));
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            search_list.clear();
            adapter.notifyDataSetChanged();
            refreshLayout.finishRefresh();
        });
        smartRefreshLayout.autoLoadMore(400);
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

}