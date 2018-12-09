package com.example.lorebase.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lorebase.R;
import com.example.lorebase.adapter.LoreTreeAdapter;
import com.example.lorebase.bean.LoreTree;
import com.example.lorebase.contain_const.UrlContainer;
import com.example.lorebase.util.DividerItemGridDecoration;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;
import java.util.Objects;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import okhttp3.Call;
import okhttp3.Request;

/*
    ArrayAdapter<String>,Toast,MainActivity-->LoreTreeFragment
 */

public class LoreTreeFragment extends Fragment {
    public static RecyclerView recyclerView_loreTree;
    private View view;
    private List<LoreTree.DataBean> fatherBeanList;
    public static NestedScrollView nestedScrollView;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lore_tree,null);
        getLoreTree();//include: initView 初始化视图需要数据填充，获取数据后再初始化视图
        return view;
    }

    private void getLoreTree() {
        String url = UrlContainer.baseUrl + UrlContainer.TREE;
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
                        fatherBeanList = gson.fromJson(response, LoreTree.class).getData();
                        initView();
                    }
                });
    }

    private void initView() {
        recyclerView_loreTree = view.findViewById(R.id.recycler_lore_tree);

        nestedScrollView = view.findViewById(R.id.nest_scroll_lore_tree);

        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);

        recyclerView_loreTree.setLayoutManager(manager);

        LoreTreeAdapter loreTreeAdapter = new LoreTreeAdapter(fatherBeanList);

        recyclerView_loreTree.setAdapter(loreTreeAdapter);

        recyclerView_loreTree.addItemDecoration(new DividerItemGridDecoration(Objects.requireNonNull(getActivity())));
    }
}
