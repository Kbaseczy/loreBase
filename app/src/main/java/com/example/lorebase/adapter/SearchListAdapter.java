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
import com.example.lorebase.util.TagFilter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {


    private List<Article.DataBean.DatasBean> search_list;
    private Context mContext;

    public SearchListAdapter(Context context, List<Article.DataBean.DatasBean> search_list) {
        this.search_list = search_list;
        this.mContext = context;
    }

    public void setSearch_list(List<Article.DataBean.DatasBean> search_list) {
        this.search_list.addAll(search_list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
        String filterTitle = TagFilter.delHTMLTag(search.getTitle()); //过滤搜索带标签的title
        holder.author.setText(search.getAuthor());
        holder.date.setText(search.getNiceDate());
        holder.title.setText(filterTitle);
        String name = search.getSuperChapterName() + "/" + search.getChapterName();
        holder.chapterName.setText(name);
        holder.imageView.setImageResource(search.isCollect() ? R.drawable.ic_like : R.drawable.ic_like_not);
        holder.cardView.setOnClickListener(v -> {
            MapReceiver.getInstance().setPositionInterface((Latitude, Longitude) -> {
                L.v(Latitude + " \n" + Longitude + "  有没有啊");
                MyApplication.getDaoSession().getBrowseHistoryDao().insertOrReplace(new BrowseHistory(
                        null, filterTitle, search.getLink(), search.getNiceDate(), search.isCollect()
                        , Latitude, Longitude));
            });
            Intent intent = new Intent(mContext, AgentWebActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ConstName.OBJ, search);
            intent.putExtra(ConstName.BUNDLE, bundle);
            intent.putExtra(ConstName.ACTIVITY, ConstName.activity.SEARCH_LIST);
            mContext.startActivity(intent);
        });
        L.v("HomeList_isCollect", PreferencesUtil.getIsLogin(mContext) + " Login_statue-searchList");
        holder.imageView.setOnClickListener(v -> {
                    if (PreferencesUtil.getIsLogin(mContext)) {
                        if (!search.isCollect()) {
                            RetrofitUtil.collectArticle(search, mContext,this);
                        } else {
                            RetrofitUtil.unCollectArticle(search, mContext,this);
                        }
                    } else {
                        mContext.startActivity(new Intent(mContext, LoginActivity.class)
                                .putExtra(ConstName.ACTIVITY, ConstName.activity.SEARCH_LIST));
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return search_list.size();
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
