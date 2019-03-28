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
import com.example.lorebase.greenDao.BrowseHistoryDao;
import com.example.lorebase.http.RetrofitUtil;
import com.example.lorebase.ui.activity.AgentWebActivity;
import com.example.lorebase.ui.activity.LoginActivity;
import com.example.lorebase.util.L;
import com.example.lorebase.util.PreferencesUtil;
import com.example.lorebase.util.TimeUtils;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> {

    public double getLatitude() {
        return latitude;
    }

    void setPosition(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    private double latitude, longitude;
    private Context mContext;
    private List<Article.DataBean.DatasBean> beanList;

    HomeListAdapter(List<Article.DataBean.DatasBean> beanList) {
        this.beanList = beanList;
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Article.DataBean.DatasBean article = beanList.get(position);
        holder.title.setText(article.getTitle());
        holder.author.setText(article.getAuthor());
        holder.chapterName.setText(article.getChapterName());
        holder.date.setText(article.getNiceDate());
        holder.collect.setImageResource(article.isCollect() ? R.drawable.ic_like : R.drawable.ic_like_not);
        BrowseHistoryDao browseHistoryDao = MyApplication.getDaoSession().getBrowseHistoryDao();

        holder.cardView.setOnClickListener(v -> {
            MapReceiver.getInstance().setPositionInterface((Latitude, Longitude) -> {
                L.v("mapHomeList", Latitude + " \n" + Longitude + "  有没有啊");
                browseHistoryDao.insertOrReplace(new BrowseHistory(null, article.getTitle(),
                        article.getLink(),
                        TimeUtils.date2String(new Date(System.currentTimeMillis())),
                        article.isCollect(), Latitude, Longitude,false));
            });

            L.v("mapHomeList", "  点击比较" + latitude + "\t" + longitude);
            Intent intent = new Intent(mContext, AgentWebActivity.class);
            Bundle bundle =new Bundle();
            bundle.putSerializable(ConstName.OBJ,article);
            intent.putExtras(bundle);
            L.v("HomeList_isCollect", article.isCollect() + " statue");
            mContext.startActivity(intent);
        });

        //todo 收藏图标点击事件
        L.v("HomeList_isCollect", PreferencesUtil.getIsLogin(mContext) + " Login_statue-home");
        holder.collect.setOnClickListener(v -> {
            //获取某子项位置，并得到该项的数据对象
            if (PreferencesUtil.getIsLogin(mContext)) {
                if (!article.isCollect()) {
                    RetrofitUtil.collectArticle(article, mContext,this);
                } else {
                    RetrofitUtil.unCollectArticle(article, mContext,this);
                }
            } else {
                mContext.startActivity(new Intent(mContext, LoginActivity.class)
                        .putExtra(ConstName.ACTIVITY, ConstName.activity.MAIN));
            }
        });
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
