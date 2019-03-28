package com.example.lorebase.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lorebase.MapReceiver;
import com.example.lorebase.MyApplication;
import com.example.lorebase.R;
import com.example.lorebase.bean.BrowseHistory;
import com.example.lorebase.bean.NavigateSite;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.ui.activity.AgentWebActivity;
import com.example.lorebase.util.L;
import com.example.lorebase.util.TimeUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/* todo 要命题
    第一步.  recyclerView的item項和tagFlowLayout的tag区分开，通过position_item先定位到item再通过position_tag定位每个tag
      chapter 对应 position_item : recyclerView 的位置参数
      article 对应 position_tag  : tagFlowLayout 的位置参数
    第二步.if (position_tag <= beans_chapter.get(position_item).getArticles().size() - 1)
          ->数组越界，控制取数据的index，index应该比List.size()-1
          ->List的 index 和 position_tag都是从0开始，控制position_tag上限 <= List.size()-1
          以上做法舍弃，无法控制tag的正确数量。
          todo 应给tagFlowLayout设置对应的数据List -> beans_chapter.get(position_item).getArticles()
          对应position_item下的articles
    -> 多虑加了for循环，recyclerView 和 tagFlowLayout 一样都是自己遍历数据，有对应的position放置对应位置数据
 */
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
        TagAdapter<NavigateSite.DataBean.ArticlesBean> adapter_hot =
                new TagAdapter<NavigateSite.DataBean.ArticlesBean>(beans_chapter.get(position_item).getArticles()) {  //beans_chapter.size 決定tag數量
                    //chapter 对应 position_item : recyclerView 的位置参数
                    //article 对应 position_tag  : tagFlowLayout 的位置参数
                    @Override
                    public View getView(FlowLayout parent, int position_tag, NavigateSite.DataBean.ArticlesBean articlesBean) {
                        if (mContext == null) {
                            mContext = parent.getContext();
                        }
                        TextView navi_tag = (TextView) LayoutInflater.from(mContext)
                                .inflate(R.layout.tag_flow_tv, parent, false);
                        //todo 多虑加了for循环，recyclerView 和 tagFlowLayout 一样都是自己遍历数据，有对应的position放置对应位置数据
                        String tag_navi = articlesBean.getTitle();
                        navi_tag.setText(tag_navi);
                        navi_tag.setTextColor(Color.BLACK); //字體顔色
                        navi_tag.setBackgroundResource(R.color.Grey400);
                        return navi_tag;
                    }
                };
        holder.tagFlowLayout.setAdapter(adapter_hot);
        holder.tagFlowLayout.setOnTagClickListener((view, position_tag, parent) -> {
            if (position_tag <= beans_chapter.get(position_item).getArticles().size() - 1) {
                String tag_navi = beans_chapter.get(position_item)
                        .getArticles().get(position_tag).getTitle();
                String tag_link = beans_chapter.get(position_item)   //对应item項 position_item
                        .getArticles().get(position_tag).getLink();   //对应tag項 position_tag
                String tag_date = beans_chapter.get(position_item)
                        .getArticles().get(position_tag).getNiceDate();
                boolean is_collect = beans_chapter.get(position_item)
                        .getArticles().get(position_tag).isCollect();

                MapReceiver.getInstance().setPositionInterface((Latitude, Longitude) -> {
                    L.v(Latitude + " \n" + Longitude + "  有没有啊");
                    MyApplication.getDaoSession().getBrowseHistoryDao().insertOrReplace(new BrowseHistory(
                            null, tag_navi, tag_link,
                            TimeUtils.date2String(new Date(System.currentTimeMillis()))
                            ,false,Latitude,Longitude,true));
                });

                Intent intent = new Intent();
                intent.setClass(mContext, AgentWebActivity.class)
                        .putExtra(ConstName.TITLE, tag_navi)
                        .putExtra(ConstName.ACTIVITY, ConstName.activity.NAVIGATION)
                        .putExtra(ConstName.IS_OUT,true)
                        .setData(Uri.parse(tag_link));
                mContext.startActivity(intent);
            }
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
