package com.example.lorebase.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lorebase.MyApplication;
import com.example.lorebase.R;
import com.example.lorebase.bean.NavigateSite;
import com.example.lorebase.bean.NavigateSite.DataBean;
import com.example.lorebase.bean.SearchHistory;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.ui.activity.AgentWebActivity;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.ViewHolder> {

    private Context mContext;
    private List<NavigateSite.DataBean> beans_chapter;

    public NavigationAdapter(List<NavigateSite.DataBean> beans_chapter) {
        this.beans_chapter = beans_chapter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_navigation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NavigateSite.DataBean chapter = beans_chapter.get(position);
        holder.chapter.setText(chapter.getName());
        tagFlow(holder, position);
    }

    private void tagFlow(@NonNull ViewHolder holder, int position_item) {
        final String[] tag_title = new String[1];
        TagAdapter<NavigateSite.DataBean> adapter_hot =
                new TagAdapter<NavigateSite.DataBean>(beans_chapter) {
                    //chapter 对应 position_item : recyclerView 的位置参数
                    //article 对应 position_tag  : tagFlowLayout 的位置参数
                    @Override
                    public View getView(FlowLayout parent, int position_tag, NavigateSite.DataBean chapter) {
                        if (mContext == null) {
                            mContext = parent.getContext();
                        }
                        TextView navi_tag = (TextView) LayoutInflater.from(mContext)
                                .inflate(R.layout.tag_flow_tv, parent, false);
                        int tag_length = beans_chapter.get(position_item).getArticles().size();
                        tag_title[0] = beans_chapter.get(position_item)
                                .getArticles().get(position_tag).getTitle();
                        for (int i = 0; i < tag_length; i++) {
                            navi_tag.setText(tag_title[0]);
                        }

                        navi_tag.setTextColor(position_tag % 2 == 0 ? Color.BLACK : Color.RED); //字體顔色
                        navi_tag.setBackgroundResource(R.color.Grey200);
                        return navi_tag;
                    }
                };
        holder.tagFlowLayout.setAdapter(adapter_hot);
        holder.tagFlowLayout.setOnTagClickListener((view, position_tag, parent) -> {
//
//            String tag_navi = beans_chapter.get(position_item)
//                    .getArticles().get(position_tag).getTitle();
            String tag_link = beans_chapter.get(position_item)   //对应item項 position_item
                    .getArticles().get(position_tag).getLink();   //对应tag項 position_tag
            MyApplication.getDaoSession().getSearchHistoryDao()
                    .insertOrReplace(new SearchHistory(null, tag_title[0]));
            Intent intent = new Intent();
            intent.setClass(mContext, AgentWebActivity.class)
                    .putExtra(ConstName.TITLE, tag_title[0])
                    .putExtra(ConstName.ACTIVITY, ConstName.activity.NAVIGATION)
                    .setData(Uri.parse(tag_link));
            mContext.startActivity(intent);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return beans_chapter.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView chapter;
        TagFlowLayout tagFlowLayout;

        ViewHolder(@NonNull View view) {
            super(view);
            chapter = view.findViewById(R.id.tv_navigation_chapter);
            tagFlowLayout = view.findViewById(R.id.tag_navigation);
        }
    }
}
