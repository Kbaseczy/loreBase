package com.example.lorebase.adapter;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lorebase.R;
import com.example.lorebase.bean.Article;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.ui.activity.AgentWebActivity;

import java.util.List;

public class LoreListAdapter extends RecyclerView.Adapter<LoreListAdapter.ViewHolder> {

    private List<Article.DataBean.DatasBean> datasBeanList;
    private Context mContext;

    public LoreListAdapter(List<Article.DataBean.DatasBean> datasBeanList) {
        this.datasBeanList = datasBeanList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lore_list_item, parent, false);

        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(v->{
            int position = holder.getAdapterPosition();
            Article.DataBean.DatasBean datasBean = datasBeanList.get(position);
            //跳转到agentWeb  need:link/title
            Intent intent = new Intent(mContext,AgentWebActivity.class);
            intent.putExtra(ConstName.TITLE,datasBean.getTitle());
            intent.setData(Uri.parse(datasBean.getLink()));
            mContext.startActivity(intent);
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Article.DataBean.DatasBean article = datasBeanList.get(position);
        holder.author.setText(article.getAuthor());
        holder.date.setText(article.getNiceDate());
//        holder.title.setText(article.getTitle());
        holder.title.setText(R.string.articleName);
        String name = article.getSuperChapterName()+"/"+article.getChapterName();
        holder.chapterName.setText(name);
        Glide.with(mContext).load(R.drawable.ic_like_not).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        try{
            return datasBeanList.size();
        }catch (NullPointerException E){
            E.printStackTrace();
        }
        return 0; //todo
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView author,date,title,chapterName;
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
