package com.example.lorebase.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lorebase.MyApplication;
import com.example.lorebase.R;
import com.example.lorebase.adapter.LoreTreeAdapter;
import com.example.lorebase.bean.LoreTree;
import com.example.lorebase.http.RetrofitApi;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import retrofit2.Callback;
import retrofit2.Response;

/*
    ArrayAdapter<String>,Toast,MainActivity-->LoreTreeFragment
 */

public class LoreTreeFragment extends Fragment {
    public static RecyclerView recyclerView_loreTree;
    private View view;
    private List<LoreTree.DataBean> fatherBeanList;

    public static LoreTreeFragment getInstance() {
        LoreTreeFragment fragment = new LoreTreeFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lore_tree, null);
        getLoreTree();//include: initView 初始化视图需要数据填充，获取数据后再初始化视图
        return view;
    }

    private void getLoreTree() {
        RetrofitApi api = MyApplication.retrofit.create(RetrofitApi.class);
        retrofit2.Call<LoreTree> loreTreeCall = api.getLoreTree();
        loreTreeCall.enqueue(new Callback<LoreTree>() {
            @Override
            public void onResponse(retrofit2.Call<LoreTree> call, Response<LoreTree> response) {
                if (response.body() != null) {
                    fatherBeanList = response.body().getData();
                    initView();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<LoreTree> call, Throwable t) {

            }
        });
    }

    private void initView() {
        recyclerView_loreTree = view.findViewById(R.id.recycler_lore_tree);

        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        recyclerView_loreTree.setLayoutManager(manager);

        LoreTreeAdapter loreTreeAdapter = new LoreTreeAdapter(fatherBeanList);

        recyclerView_loreTree.setAdapter(loreTreeAdapter);

//        recyclerView_loreTree.addItemDecoration(new DividerItemGridDecoration(Objects.requireNonNull(getActivity())));
    }
}
