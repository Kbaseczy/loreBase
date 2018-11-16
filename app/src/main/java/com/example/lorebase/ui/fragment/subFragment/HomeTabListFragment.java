package com.example.lorebase.ui.fragment.subFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lorebase.R;
import com.example.lorebase.adapter.HomeListAdapter;
import com.example.lorebase.bean.Article;
import com.example.lorebase.contain_const.ConstName;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Request;

public class HomeTabListFragment extends Fragment {

    private String url;
    private List<Article.DataBean.DatasBean> beanList;
    private View view;
    public static RecyclerView recyclerView;

    public static HomeTabListFragment newInstance(String url) {
        HomeTabListFragment fragment = new HomeTabListFragment();
        Bundle args = new Bundle();
        args.putString(ConstName.URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            url = getArguments().getString(ConstName.URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_home_list, container, false);
        getData(url);
        return view;
    }

    private void getData(String url) {
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
                        //TODO :Expected Object but Array -> ok
                        beanList = gson.fromJson(response, Article.class).getData().getDatas();
                        initView();
                    }
                });
    }

    private void initView() {
        recyclerView = view.findViewById(R.id.home_recycler_view);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);

        recyclerView.setLayoutManager(layoutManager);

        HomeListAdapter homeListAdapter = new HomeListAdapter(beanList);

        recyclerView.setAdapter(homeListAdapter);

        SmartRefreshLayout smartRefreshLayout = view.findViewById(R.id.smart_refresh_home);

        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        smartRefreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()));
//        smartRefreshLayout.autoRefresh();
//        smartRefreshLayout.autoLoadMore();
    }
}
