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
import com.example.lorebase.util.L;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MyselfAdapter extends RecyclerView.Adapter<MyselfAdapter.ViewHolder> {

    private Context mContext;

    private List<Article.DataBean.DatasBean> datasBeanList;

    public MyselfAdapter(Context context, List<Article.DataBean.DatasBean> datasBeanList) {
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.lore_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Article.DataBean.DatasBean my_collect = datasBeanList.get(position);

        holder.author.setText(my_collect.getAuthor());
        holder.date.setText(my_collect.getNiceDate());
        holder.title.setText(my_collect.getTitle());
        String name = my_collect.getChapterName();
        holder.chapterName.setText(name);
        holder.imageView.setImageResource(R.drawable.ic_like);

        holder.cardView.setOnClickListener(v -> {
            MapReceiver.getInstance().setPositionInterface((Latitude, Longitude) -> {
                L.v(Latitude + " \n" + Longitude + "  有没有啊MyselfAdapter");
                MyApplication.getDaoSession().getBrowseHistoryDao().insertOrReplace(new BrowseHistory(
                        null, my_collect.getTitle(), my_collect.getLink()
                        , my_collect.getNiceDate(), my_collect.isCollect(), Latitude, Longitude));
            });

            Intent intent = new Intent(mContext, AgentWebActivity.class);
            intent.putExtra(ConstName.ACTIVITY, ConstName.activity.MYSELF);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ConstName.OBJ, my_collect);
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        });

        holder.imageView.setOnClickListener(v -> RetrofitUtil.deleteArticle(my_collect, mContext, this));
    }

    @Override
    public int getItemCount() {
        return datasBeanList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView author, date, title, chapterName;
        ImageView imageView;

        ViewHolder(@NonNull View view) {
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
