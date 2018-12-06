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
import com.example.lorebase.bean.NaviArticle;
import com.example.lorebase.bean.NavigateSite;
import com.example.lorebase.bean.NavigateSite.DataBean;
import com.example.lorebase.bean.SearchHistory;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.ui.activity.AgentWebActivity;
import com.example.lorebase.util.L;
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
<<<<<<< HEAD
        tagFlow(holder,chapter);
    }

    private void tagFlow(@NonNull ViewHolder holder,NavigateSite.DataBean chapter) {
        TagAdapter<NavigateSite.DataBean> adapter_hot =
                new TagAdapter<DataBean>(beans_chapter) {
                    @Override
                    public View getView(FlowLayout parent, int position, NavigateSite.DataBean articlesBean) {
                        // position -> beans_chapter.size()'item
                        // articlesBean -> beans_chapter(position)
=======
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
>>>>>>> 5e2a850959dc8b061a6c117549560b4211dccd66
                        if (mContext == null) {
                            mContext = parent.getContext();
                        }
                        TextView navi_tag = (TextView) LayoutInflater.from(mContext)
                                .inflate(R.layout.tag_flow_tv, parent, false);
<<<<<<< HEAD
                            // 每组articles中数据个数的共性，得到i的上限->articlesBean.getArticles().size()
                            for (int i = 0; i < articlesBean.getArticles().size(); i++) {
                                navi_tag.setText(articlesBean.getArticles().get(i).getTitle());
                                L.v("nAdapter:"+articlesBean.getArticles().get(i).getTitle()
                                +"  "+articlesBean.getArticles().get(i).getChapterName());
                        }
                        navi_tag.setTextColor(position % 2 == 0 ? Color.BLACK : Color.RED); //字體顔色
=======
                        int tag_length = beans_chapter.get(position_item).getArticles().size();
                        tag_title[0] = beans_chapter.get(position_item)
                                .getArticles().get(position_tag).getTitle();
                        for (int i = 0; i < tag_length; i++) {
                            navi_tag.setText(tag_title[0]);
                        }

                        navi_tag.setTextColor(position_tag % 2 == 0 ? Color.BLACK : Color.RED); //字體顔色
>>>>>>> 5e2a850959dc8b061a6c117549560b4211dccd66
                        navi_tag.setBackgroundResource(R.color.Grey200);
                        return navi_tag;
                    }
                };
        holder.tagFlowLayout.setAdapter(adapter_hot);
        holder.tagFlowLayout.setOnTagClickListener((view, position_tag, parent) -> {
<<<<<<< HEAD
                for (int i = 0; i < chapter.getArticles().size(); i++) {
                    String tag_navi = chapter.getArticles().get(i).getTitle();
                    String tag_navi_link = chapter.getArticles().get(i).getLink();
                    MyApplication.getDaoSession().getSearchHistoryDao()
                            .insertOrReplace(new SearchHistory(null, tag_navi));
                    Intent intent = new Intent();
                    intent.setClass(mContext, AgentWebActivity.class)
                            .putExtra(ConstName.TITLE, tag_navi)
                            .putExtra(ConstName.ACTIVITY, ConstName.activity.NAVIGATION)
                            .setData(Uri.parse(tag_navi_link));
                    mContext.startActivity(intent);
            }
=======
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
>>>>>>> 5e2a850959dc8b061a6c117549560b4211dccd66
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
