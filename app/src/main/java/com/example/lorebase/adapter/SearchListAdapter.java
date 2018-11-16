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
import android.widget.Toast;

import com.example.lorebase.R;
import com.example.lorebase.bean.Article;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.http.CollectArticle;
import com.example.lorebase.ui.activity.AgentWebActivity;
import com.example.lorebase.ui.activity.LoginActivity;
import com.example.lorebase.ui.activity.MainActivity;
import com.example.lorebase.util.Constant;

import java.util.List;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {

    private List<Article.DataBean.DatasBean> search_list;
    private Context mContext;

    private boolean isCollect = false;

    public SearchListAdapter(List<Article.DataBean.DatasBean> search_list) {
        this.search_list = search_list;
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
        Article.DataBean.DatasBean search = search_list.get(position);
        SharedPreferences sp = mContext.getSharedPreferences(ConstName.LOGIN_DATA,Context.MODE_PRIVATE);
        boolean isLogin = sp.getBoolean(ConstName.IS_LOGIN,false);
        holder.author.setText(search.getAuthor());
        holder.date.setText(search.getNiceDate());
        holder.title.setText(search.getTitle());
        String name = search.getSuperChapterName() + "/" + search.getChapterName();
        holder.chapterName.setText(name);

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, AgentWebActivity.class);
            intent.putExtra(ConstName.TITLE, search.getTitle());
            intent.putExtra(ConstName.ACTIVITY, ConstName.activity.SEARCH);
            intent.putExtra(ConstName.ID, search.getId());
            intent.setData(Uri.parse(search.getLink()));
            mContext.startActivity(intent);
        });

        holder.imageView.setOnClickListener(v -> {
            if (isLogin) {
                if (!isCollect) {
                    CollectArticle.collectArticle(mContext, search.getId());
                    holder.imageView.setImageResource(R.drawable.ic_like);
                    isCollect = true;
                } else {
                    CollectArticle.unCollect_originID(mContext, search.getId());
                    holder.imageView.setImageResource(R.drawable.ic_like_not);
                    isCollect = false;
                }
            }else{
                mContext.startActivity(new Intent(mContext,LoginActivity.class));
            }
                }
        );
        if(isCollect)
             holder.imageView.setImageResource(R.drawable.ic_like);
        else
            holder.imageView.setImageResource(R.drawable.ic_like_not);
    }

    @Override
    public int getItemCount() {
        return search_list.size();
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
