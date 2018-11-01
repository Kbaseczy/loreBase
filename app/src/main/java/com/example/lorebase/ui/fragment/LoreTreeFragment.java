package com.example.lorebase.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lorebase.R;
import com.example.lorebase.adapter.LoreTreeAdapter;
import com.example.lorebase.bean.LoreTree;
import com.example.lorebase.contain_const.UrlContainer;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Request;

/*
    ArrayAdapter<String>,Toast,MainActivity-->LoreTreeFragment
 */

public class LoreTreeFragment extends Fragment {

    View view;
    protected LoreTreeAdapter loreTreeAdapter;
    private List<LoreTree.DataBean> fatherBeanList;
    private List<LoreTree.DataBean.ChildrenBean> childrenBeanList;
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
                        childrenBeanList = gson.fromJson(response, LoreTree.DataBean.class).getChildren();
                        initView();
                    }
                });
    }

    private void initView() {
        RecyclerView recyclerView_loreTree = view.findViewById(R.id.recycler_lore_tree);

        GridLayoutManager manager = new GridLayoutManager(getActivity(),1);

        recyclerView_loreTree.setLayoutManager(manager);

        loreTreeAdapter = new LoreTreeAdapter(fatherBeanList,childrenBeanList);

        recyclerView_loreTree.setAdapter(loreTreeAdapter);
    }


}
