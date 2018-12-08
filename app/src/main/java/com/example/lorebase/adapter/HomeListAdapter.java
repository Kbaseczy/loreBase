package com.example.lorebase.adapter;

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

import com.bumptech.glide.Glide;
import com.example.lorebase.MyApplication;
import com.example.lorebase.R;
import com.example.lorebase.bean.Article;
import com.example.lorebase.bean.BrowseHistory;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.greenDao.BrowseHistoryDao;
import com.example.lorebase.http.CollectArticle;
import com.example.lorebase.ui.activity.AgentWebActivity;
import com.example.lorebase.ui.activity.LoginActivity;

import java.util.List;

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> {

    private Context mContext;
    private List<Article.DataBean.DatasBean> beanList;

    public HomeListAdapter(List<Article.DataBean.DatasBean> beanList) {
        this.beanList = beanList;
    }

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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Article.DataBean.DatasBean article = beanList.get(position);
        holder.title.setText(article.getTitle());
        holder.author.setText(article.getAuthor());
        holder.chapterName.setText(article.getChapterName());
        holder.date.setText(article.getNiceDate());
        if (article.isCollect())
            holder.collect.setImageResource(R.drawable.ic_like);
        else
            holder.collect.setImageResource(R.drawable.ic_like_not);

        BrowseHistoryDao browseHistoryDao = MyApplication.getDaoSession().getBrowseHistoryDao();

        holder.cardView.setOnClickListener(v -> {
            browseHistoryDao.insertOrReplace(new BrowseHistory(null, article.getTitle(), article.getLink(), article.getNiceDate()));
            Intent intent = new Intent(mContext, AgentWebActivity.class);
            intent.putExtra(ConstName.TITLE, article.getTitle());
            intent.putExtra(ConstName.PROJECT_AUTHOR, article.getAuthor());
            intent.putExtra(ConstName.ID, article.getId());
            intent.setData(Uri.parse(article.getLink()));
            mContext.startActivity(intent);
        });

        SharedPreferences sp = mContext.getSharedPreferences(ConstName.LOGIN_DATA, Context.MODE_PRIVATE);
        boolean isLogin = sp.getBoolean(ConstName.IS_LOGIN, false);
        //todo 收藏图标点击事件
        holder.collect.setOnClickListener(v -> {
                    //获取某子项位置，并得到该项的数据对象
                    if (isLogin) {
                        if (!article.isCollect()) {
                            CollectArticle.collectArticle(mContext, article.getId());
                            holder.collect.setImageResource(R.drawable.ic_like); //点击图标后变为红色表示已收藏
                            notifyDataSetChanged();
                        }
                        if (article.isCollect()) {
                            CollectArticle.unCollect_originID(mContext, article.getId());
                            holder.collect.setImageResource(R.drawable.ic_like_not);
                            notifyDataSetChanged();
                        }
                    } else {
                        mContext.startActivity(new Intent(mContext, LoginActivity.class));
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView collect;
        TextView title, author, chapterName, date;

        ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            collect = itemView.findViewById(R.id.iv_article_like);
            title = itemView.findViewById(R.id.article_title);
            author = itemView.findViewById(R.id.tv_article_author);
            chapterName = itemView.findViewById(R.id.tv_article_chapterName);
            date = itemView.findViewById(R.id.tv_article_date);
        }

    }
}
