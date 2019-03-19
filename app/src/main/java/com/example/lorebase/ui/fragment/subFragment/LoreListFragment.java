package com.example.lorebase.ui.fragment.subFragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ajguan.library.EasyRefreshLayout;
import com.example.lorebase.MyApplication;
import com.example.lorebase.R;
import com.example.lorebase.adapter.LoreListAdapter;
import com.example.lorebase.bean.Article;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.http.RetrofitApi;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class LoreListFragment extends Fragment {

    private View view;
    private List<Article.DataBean.DatasBean> datasBeanList;
    private int page = 0; //todo 上拉加载
    private int chapterId;
    private EasyRefreshLayout easyRefreshLayout;
    private LoreListAdapter adapter;

    //实例化  -->todo 替代构造方法传递数据（在重新创建Fragment的时候，数据会丢失），
    //          todo 用setArguments(bundle)传递数据更安全。
    public LoreListFragment instantiate(int cid) {
        //TODO The step is the most important!
        //TODO->which to transfer the chapterID for getting the data about recyclerView .
        LoreListFragment instance_frag = new LoreListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ConstName.CHAPTER_CID, cid);
        instance_frag.setArguments(bundle);
        return instance_frag;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lore_list, container, false);
        assert getArguments() != null;
        chapterId = getArguments().getInt(ConstName.CHAPTER_CID);
        //（loreActivity中添加Fragment对象时传递了chapterID）从fragment实例获取chapterID
        getLore(chapterId);
        return view;
    }

    @Override
    public void onResume() {
        easyRefreshLayout = view.findViewById(R.id.easy_refresh_lore);
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

    private void initRecycler() {
        RecyclerView recyclerView = view.findViewById(R.id.lore_rv);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(manager);
        adapter = new LoreListAdapter(getActivity(), datasBeanList);
        recyclerView.setAdapter(adapter);
//        recyclerView.addItemDecoration(new DividerItemGridDecoration(Objects.requireNonNull(getContext())));
    }

    private void getDataList() {
        new Handler().postDelayed(() -> {
            if (easyRefreshLayout.isRefreshing()) {
                page = 0;
                getLore(chapterId);
//                adapter.setBeanList_article(beanList_article);
                adapter.addDatasBeanList(datasBeanList);
                easyRefreshLayout.refreshComplete();
            } else {
                page++;
                getLore(chapterId);
//                adapter.setBeanList_article(beanList_article);
                adapter.addDatasBeanList(datasBeanList);
                easyRefreshLayout.loadMoreComplete();
            }
        }, 500);
    }

    private void getLore(int chapterID) {
        RetrofitApi api = MyApplication.retrofit.create(RetrofitApi.class);
        retrofit2.Call<Article> loreListLall = api.getLoreList(page,chapterID);
        loreListLall.enqueue(new Callback<Article>() {
            @Override
            public void onResponse(retrofit2.Call<Article> call, Response<Article> response) {
                if (response.body() != null) {
                    datasBeanList = response.body().getData().getDatas();
                    initRecycler();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Article> call, Throwable t) {

            }
        });
    }
}
