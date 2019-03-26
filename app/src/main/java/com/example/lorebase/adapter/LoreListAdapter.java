package com.example.lorebase.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lorebase.MapReceiver;
import com.example.lorebase.MyApplication;
import com.example.lorebase.R;
import com.example.lorebase.bean.Article;
import com.example.lorebase.bean.BrowseHistory;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.http.RetrofitUtil;
import com.example.lorebase.ui.activity.AgentWebActivity;
import com.example.lorebase.ui.activity.LoginActivity;
import com.example.lorebase.util.L;
import com.example.lorebase.util.PreferencesUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class LoreListAdapter extends RecyclerView.Adapter<LoreListAdapter.ViewHolder> {

    private List<Article.DataBean.DatasBean> datasBeanList;
    private Context mContext;

    public LoreListAdapter(Context context, List<Article.DataBean.DatasBean> datasBeanList) {
        this.datasBeanList = datasBeanList;
        this.mContext = context;
    }

    public void addDatasBeanList(List<Article.DataBean.DatasBean> datasBeanList) {
        this.datasBeanList.addAll(datasBeanList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lore_list_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        int position = holder.getAdapterPosition();
        Article.DataBean.DatasBean datasBean = datasBeanList.get(position);
        holder.cardView.setOnClickListener(v -> {
            //跳转到LoreAgentWeb  need:link/title
            Intent intent = new Intent(mContext, AgentWebActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ConstName.OBJ, datasBean);
            intent.putExtras( bundle);
            intent.putExtra(ConstName.ACTIVITY, ConstName.activity.LORE);
            mContext.startActivity(intent);

            MapReceiver.getInstance().setPositionInterface((Latitude, Longitude) -> {
                L.v(Latitude + " \n" + Longitude + "  有没有啊");
                MyApplication.getDaoSession().getBrowseHistoryDao().insertOrReplace(new BrowseHistory(
                        null, datasBean.getTitle(), datasBean.getLink()
                        , datasBean.getNiceDate(), datasBean.isCollect(), Latitude, Longitude));
            });
        });

        L.v("HomeList_isCollect", PreferencesUtil.getIsLogin(mContext) + " Login_statue-lorelist");
        holder.imageView.setOnClickListener(v -> {

            if (PreferencesUtil.getIsLogin(mContext)) {
                if (!datasBean.isCollect()) {
                    RetrofitUtil.collectArticle(datasBean, mContext, this);
                } else {
                    RetrofitUtil.unCollectArticle(datasBean, mContext, this);
                }
            } else {
                mContext.startActivity(new Intent(mContext, LoginActivity.class)
                        .putExtra(ConstName.ACTIVITY, ConstName.activity.LORE));
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        Article.DataBean.DatasBean article = datasBeanList.get(position);
        holder.author.setText(article.getAuthor());
        holder.date.setText(article.getNiceDate());
        holder.title.setText(article.getTitle());
        String name = article.getSuperChapterName() + "/" + article.getChapterName();
        holder.chapterName.setText(name);
        holder.imageView.setImageResource(article.isCollect() ? R.drawable.ic_like : R.drawable.ic_like_not);
    }

    @Override
    public int getItemCount() {
//        return 0;
        return datasBeanList.size();// int java.util.List.size()' on a null object reference
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView author, date, title, chapterName;
        ImageView imageView;

        ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            author = view.findViewById(R.id.tv_article_author);
            date = view.findViewById(R.id.tv_article_date);
            title = view.findViewById(R.id.article_title);
            chapterName = view.findViewById(R.id.tv_article_chapterName);
            imageView = view.findViewById(R.id.iv_article_like);
        }
    }
}
