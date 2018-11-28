package com.example.lorebase.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lorebase.MyApplication;
import com.example.lorebase.R;
import com.example.lorebase.bean.Article;
import com.example.lorebase.bean.BrowseHistory;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.http.CollectArticle;
import com.example.lorebase.ui.activity.AgentWebActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MyselfAdapter extends RecyclerView.Adapter<MyselfAdapter.ViewHolder> {

    private Context mContext;
    private List<Article.DataBean.DatasBean> datasBeanList;


    public MyselfAdapter(List<Article.DataBean.DatasBean> datasBeanList) {
        this.datasBeanList = datasBeanList;
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
        String name = my_collect.getSuperChapterName() + "/" + my_collect.getChapterName();
        holder.chapterName.setText(name);
        holder.imageView.setImageResource(R.drawable.ic_like);

        holder.cardView.setOnClickListener(v -> {
            MyApplication.getDaoSession().getBrowseHistoryDao().insertOrReplace(
                    new BrowseHistory(null, my_collect.getTitle(),
                            my_collect.getLink(), my_collect.getNiceDate()));
            Intent intent = new Intent(mContext, AgentWebActivity.class);
            intent.putExtra(ConstName.TITLE, my_collect.getTitle());
            intent.putExtra(ConstName.ACTIVITY, ConstName.activity.MYSELF);
            intent.setData(Uri.parse(my_collect.getLink()));
            mContext.startActivity(intent);
        });

        holder.imageView.setOnClickListener(v -> CollectArticle.unCollect(mContext, my_collect.getId())
        );
    }

    @Override
    public int getItemCount() {
        return datasBeanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
            imageView = view.findViewById(R.id.iv_like);
        }
    }
}
