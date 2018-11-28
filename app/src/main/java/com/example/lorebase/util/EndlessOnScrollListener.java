package com.example.lorebase.util;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class EndlessOnScrollListener extends RecyclerView.OnScrollListener {
    private LinearLayoutManager mLinearLayoutManager;

    //当前页，从第一页开始
    private int current_page;

    //已加载出来的item总数
    private int total_ItemCount;

    //存储上一个total_ItemCount
    private int previousTotal = 0;

    //可见的item数
    private int visibleItemCount;

    //屏幕中第一个可见item
    private int firstVisibleItem;

    //是否加载数据
    private boolean loading = true;

    protected EndlessOnScrollListener(LinearLayoutManager linearLayoutManager){
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        total_ItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        if(loading){
            if(total_ItemCount > previousTotal){

                loading = false;  //数据加载结束
                previousTotal = total_ItemCount;
            }
        }

        if(!loading && total_ItemCount - visibleItemCount <= firstVisibleItem){
            current_page ++;
            onLoadMore(current_page);
            loading = true;
        }
    }

    public abstract void onLoadMore(int current_page) ;
}

