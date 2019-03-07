package com.example.lorebase.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lorebase.R;
import com.example.lorebase.contain_const.VideoConstant;
import com.example.lorebase.util.LoadVideoScreenShot;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class AdapterRecyclerViewVideo extends RecyclerView.Adapter<AdapterRecyclerViewVideo.MyViewHolder> {

    private static final String TAG = "AdapterRecyclerViewVideo";
    int[] videoIndexs = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    private Context context;

    public AdapterRecyclerViewVideo(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_relax_list, parent,
                false));
        return holder;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder [" + holder.jzvdStd.hashCode() + "] position=" + position);

        holder.jzvdStd.setUp(
                VideoConstant.videoUrls[0][position],
                VideoConstant.videoTitles[0][position], Jzvd.SCREEN_WINDOW_LIST);
        holder.name.setText(VideoConstant.videoTitles[0][position]);
//        Glide.with(holder.jzvdStd.getContext()).load(VideoConstant.videoThumbs[0][position]).into(holder.jzvdStd.thumbImageView);
        LoadVideoScreenShot.loadVideoScreenshot(
                holder.jzvdStd.getContext(),
                VideoConstant.videoThumbs[0][position],
                holder.jzvdStd.thumbImageView, 1000);
    }

    @Override
    public int getItemCount() {
        return videoIndexs.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        JzvdStd jzvdStd;
        TextView name;

        MyViewHolder(View itemView) {
            super(itemView);
            jzvdStd = itemView.findViewById(R.id.video_player);
            name = itemView.findViewById(R.id.relax_item_name);
        }
    }

}
