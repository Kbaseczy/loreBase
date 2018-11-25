package com.example.lorebase.adapter;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lorebase.MyApplication;
import com.example.lorebase.R;
import com.example.lorebase.bean.Article;
import com.example.lorebase.bean.BrowseHistory;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.http.CollectArticle;
import com.example.lorebase.ui.activity.AgentWebActivity;
import com.example.lorebase.ui.activity.LoginActivity;
import com.google.gson.internal.bind.util.ISO8601Utils;

import java.util.List;
import java.util.PrimitiveIterator;

public class LoreListAdapter extends RecyclerView.Adapter<LoreListAdapter.ViewHolder> {

    private List<Article.DataBean.DatasBean> datasBeanList;
    private Context mContext;

    private boolean isCollect = false;

    public LoreListAdapter(List<Article.DataBean.DatasBean> datasBeanList) {
        this.datasBeanList = datasBeanList;
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
        holder.cardView.setOnClickListener(v -> {

            int position = holder.getAdapterPosition();
            Article.DataBean.DatasBean datasBean = datasBeanList.get(position);
            //跳转到LoreAgentWeb  need:link/title
            Intent intent = new Intent(mContext, AgentWebActivity.class);
            intent.putExtra(ConstName.TITLE, datasBean.getTitle());
            intent.putExtra(ConstName.ACTIVITY, ConstName.activity.LORE);
            intent.putExtra(ConstName.ID, datasBean.getId());
            intent.setData(Uri.parse(datasBean.getLink()));
            mContext.startActivity(intent);

            MyApplication.getDaoSession().getBrowseHistoryDao().insertOrReplace(new BrowseHistory(
                    null,datasBean.getTitle(),datasBean.getLink(),datasBean.getNiceDate()
            ));
        });

        SharedPreferences sp = mContext.getSharedPreferences(ConstName.LOGIN_DATA, Context.MODE_PRIVATE);
        boolean isLogin = sp.getBoolean(ConstName.IS_LOGIN, false);
        holder.imageView.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            Article.DataBean.DatasBean datasBean = datasBeanList.get(position);
            int article_id = datasBean.getId();

            if (isLogin) {
                if (!isCollect) {
                    CollectArticle.collectArticle(mContext, article_id);
                    holder.imageView.setImageResource(R.drawable.ic_like);
                    isCollect = true;
                } else {
                    CollectArticle.unCollect_originID(mContext, article_id);
                    holder.imageView.setImageResource(R.drawable.ic_like_not);
                    isCollect = false;
                }
            } else {
                mContext.startActivity(new Intent(mContext, LoginActivity.class));
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
        if (isCollect)
            holder.imageView.setImageResource(R.drawable.ic_like);
        else
            holder.imageView.setImageResource(R.drawable.ic_like_not);
//        Glide.with(mContext).load(R.drawable.ic_like_not).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
//        return 0;
        return datasBeanList.size();// int java.util.List.size()' on a null object reference
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
            imageView = view.findViewById(R.id.iv_like);
        }
    }
}
