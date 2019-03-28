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

public class WeChatArticleAdapter extends RecyclerView.Adapter<WeChatArticleAdapter.ViewHolder> {

    private List<Article.DataBean.DatasBean> we_chat_article_list;
    private Context mContext;

    public WeChatArticleAdapter(Context context, List<Article.DataBean.DatasBean> we_chat_article_list) {
        this.we_chat_article_list = we_chat_article_list;
        this.mContext = context;
    }

    public void setWe_chat_article_list(List<Article.DataBean.DatasBean> we_chat_article_list) {
        this.we_chat_article_list.addAll(we_chat_article_list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.lore_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Article.DataBean.DatasBean we_chat_article = we_chat_article_list.get(position);
        holder.author.setText(we_chat_article.getAuthor());
        holder.date.setText(we_chat_article.getNiceDate());
        holder.title.setText(we_chat_article.getTitle());
        String name = we_chat_article.getSuperChapterName() + "/" + we_chat_article.getChapterName();
        holder.chapterName.setText(name);
        holder.imageView.setImageResource(we_chat_article.isCollect() ? R.drawable.ic_like : R.drawable.ic_like_not);
        holder.cardView.setOnClickListener(v -> {

            MapReceiver.getInstance().setPositionInterface((Latitude, Longitude) -> {
                L.v(Latitude + " \n" + Longitude + "WeChatArticleAdapter aaa");
                MyApplication.getDaoSession().getBrowseHistoryDao().insertOrReplace(new BrowseHistory(
                        null, we_chat_article.getTitle(), we_chat_article.getLink(), we_chat_article.getNiceDate(), we_chat_article.isCollect()
                        , Latitude, Longitude,false));
            });

            Intent intent = new Intent(mContext, AgentWebActivity.class);
            intent.putExtra(ConstName.ACTIVITY, ConstName.activity.MAIN);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ConstName.OBJ, we_chat_article);
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        });
        L.v("HomeList_isCollect", PreferencesUtil.getIsLogin(mContext) + " Login_statue-wechat");
        holder.imageView.setOnClickListener(v -> {
            if (PreferencesUtil.getIsLogin(mContext)) {
                if (!we_chat_article.isCollect()) {
                    RetrofitUtil.collectArticle(we_chat_article, mContext,this);
                } else {
                    RetrofitUtil.unCollectArticle(we_chat_article, mContext,this);
                }
            } else {
                mContext.startActivity(new Intent(mContext, LoginActivity.class)
                        .putExtra(ConstName.ACTIVITY, ConstName.activity.MAIN));
            }
        });
    }

    @Override
    public int getItemCount() {
        return we_chat_article_list.size();
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
            imageView = view.findViewById(R.id.iv_article_like);
        }

    }
}
