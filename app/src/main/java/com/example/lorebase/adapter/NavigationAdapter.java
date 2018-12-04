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
    private List<NavigateSite.DataBean.ArticlesBean> beans_article;

    public NavigationAdapter(List<NavigateSite.DataBean> beans_chapter,
                             List<NavigateSite.DataBean.ArticlesBean> beans_article) {
        this.beans_chapter = beans_chapter;
        this.beans_article = beans_article;
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
        tagFlow(holder);
    }

    private void tagFlow(@NonNull ViewHolder holder) {
        TagAdapter<DataBean.ArticlesBean> adapter_hot =
                new TagAdapter<DataBean.ArticlesBean>(beans_article) {
                    @Override
                    public View getView(FlowLayout parent, int position, DataBean.ArticlesBean articlesBean) {
                        if (mContext == null) {
                            mContext = parent.getContext();
                        }
                        TextView navi_tag = (TextView) LayoutInflater.from(mContext)
                                .inflate(R.layout.tag_flow_tv, parent, false);
                        navi_tag.setText(articlesBean.getTitle());
                        navi_tag.setTextColor(position % 2 == 0 ? Color.BLACK : Color.RED); //字體顔色
                        navi_tag.setBackgroundResource(R.color.Grey200);
                        return navi_tag;
                    }
                };
        holder.tagFlowLayout.setAdapter(adapter_hot);
        holder.tagFlowLayout.setOnTagClickListener((view, position_tag, parent) -> {
            String tag_navi = beans_article.get(position_tag).getTitle();
            MyApplication.getDaoSession().getSearchHistoryDao()
                    .insertOrReplace(new SearchHistory(null, tag_navi));
            Intent intent = new Intent();
            intent.setClass(mContext, AgentWebActivity.class)
                    .putExtra(ConstName.TITLE, tag_navi)
                    .putExtra(ConstName.ACTIVITY, ConstName.activity.NAVIGATION)
                    .setData(Uri.parse(beans_article.get(position_tag).getLink()));
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
