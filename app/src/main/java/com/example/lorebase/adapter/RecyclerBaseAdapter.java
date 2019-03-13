package com.example.lorebase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lorebase.R;
import com.example.lorebase.bean.VideoModel;
import com.example.lorebase.holder.RecyclerItemViewHolder;
import com.example.lorebase.util.L;
import com.shuyu.gsyvideoplayer.utils.GSYVideoHelper;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerBaseAdapter extends RecyclerView.Adapter {

    private List<VideoModel> itemDataList;

    private Context context;

    private GSYVideoHelper smallVideoHelper;

    private GSYVideoHelper.GSYVideoHelperBuilder gsySmallVideoHelperBuilder;

    public RecyclerBaseAdapter(Context context, List<VideoModel> itemDataList) {
        this.itemDataList = itemDataList;
        this.context = context;
        L.v("RecyclerBaseAdapter execute.  "+context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        L.v("onCreateViewHolder execute.  "+viewType);
        View v = LayoutInflater.from(context).inflate(R.layout.list_video_item, parent, false);
        return new RecyclerItemViewHolder(context, v);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        L.v("onBindViewHolder execute.  "+position);
        RecyclerItemViewHolder recyclerItemViewHolder = (RecyclerItemViewHolder) holder;
        recyclerItemViewHolder.setVideoHelper(smallVideoHelper, gsySmallVideoHelperBuilder);
        recyclerItemViewHolder.setAdapter(this);
        recyclerItemViewHolder.onBind(position, itemDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemDataList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    public void setListData(List<VideoModel> data) {
        itemDataList = data;
        notifyDataSetChanged();
    }

    public GSYVideoHelper getVideoHelper() {
        return smallVideoHelper;
    }

    public void setVideoHelper(GSYVideoHelper smallVideoHelper, GSYVideoHelper.GSYVideoHelperBuilder gsySmallVideoHelperBuilder) {
        this.smallVideoHelper = smallVideoHelper;
        this.gsySmallVideoHelperBuilder = gsySmallVideoHelperBuilder;
    }
}
