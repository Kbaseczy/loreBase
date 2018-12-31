package com.example.lorebase.adapter;

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
import com.example.lorebase.bean.BrowseHistory;
import com.example.lorebase.bean.WeChatArticle;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.http.CollectArticle;
import com.example.lorebase.ui.activity.AgentWebActivity;
import com.example.lorebase.ui.activity.LoginActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class WeChatArticleAdapter extends RecyclerView.Adapter<WeChatArticleAdapter.ViewHolder> {

    private List<WeChatArticle.DataBean.DatasBean> we_chat_article_list;
    private Context mContext;

    public WeChatArticleAdapter(Context context, List<WeChatArticle.DataBean.DatasBean> we_chat_article_list) {
        this.we_chat_article_list = we_chat_article_list;
        this.mContext = context;
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
        WeChatArticle.DataBean.DatasBean we_chat_article = we_chat_article_list.get(position);
        SharedPreferences sp = mContext.getSharedPreferences(ConstName.LOGIN_DATA, Context.MODE_PRIVATE);
        boolean isLogin = sp.getBoolean(ConstName.IS_LOGIN, false);
        holder.author.setText(we_chat_article.getAuthor());
        holder.date.setText(we_chat_article.getNiceDate());
        holder.title.setText(we_chat_article.getTitle());
        String name = we_chat_article.getSuperChapterName() + "/" + we_chat_article.getChapterName();
        holder.chapterName.setText(name);

        holder.cardView.setOnClickListener(v -> {
            MyApplication.getDaoSession().getBrowseHistoryDao().insertOrReplace(
                    new BrowseHistory(null, we_chat_article.getTitle(), we_chat_article.getLink(), we_chat_article.getNiceDate()));
            Intent intent = new Intent(mContext, AgentWebActivity.class);
            intent.putExtra(ConstName.TITLE, we_chat_article.getTitle());
            intent.putExtra(ConstName.ACTIVITY, ConstName.activity.MAIN);
            intent.putExtra(ConstName.FRAGMENT, ConstName.fragment.WE_CHAT);
            intent.putExtra(ConstName.ID, we_chat_article.getId());
            intent.setData(Uri.parse(we_chat_article.getLink()));
            mContext.startActivity(intent);
        });

        holder.imageView.setOnClickListener(v -> {
                    if (isLogin) {
                        if (!we_chat_article.isCollect()) {
                            CollectArticle.collectArticle(mContext, we_chat_article.getId());
                            holder.imageView.setImageResource(R.drawable.ic_like);
                        } else {
                            CollectArticle.unCollect_originID(mContext, we_chat_article.getId());
                            holder.imageView.setImageResource(R.drawable.ic_like_not);
                        }
                    } else {
                        mContext.startActivity(new Intent(mContext, LoginActivity.class));
                    }
                }
        );
        if (we_chat_article.isCollect())
            holder.imageView.setImageResource(R.drawable.ic_like);
        else
            holder.imageView.setImageResource(R.drawable.ic_like_not);
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
