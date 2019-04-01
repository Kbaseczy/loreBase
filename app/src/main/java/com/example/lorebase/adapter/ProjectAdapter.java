package com.example.lorebase.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
import com.example.lorebase.util.TimeUtils;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {


    private List<Article.DataBean.DatasBean> beanList_project;
    private Context mContext;

    public ProjectAdapter(Context context, List<Article.DataBean.DatasBean> beanList_project) {
        this.beanList_project = beanList_project;
        this.mContext = context;
    }

    public void addBeanList_project(List<Article.DataBean.DatasBean> beanList_project) {
        this.beanList_project.addAll(beanList_project);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (null == mContext)
            mContext = parent.getContext();
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.project_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        Article.DataBean.DatasBean project = beanList_project.get(position);
        Glide.with(mContext).load(project.getEnvelopePic()).into(holder.image_project);
        holder.name.setText(project.getTitle());
        holder.author.setText(project.getAuthor());
        holder.time.setText(project.getNiceDate());
        holder.content.setText(project.getDesc());
        holder.image_collect.setImageResource(project.isCollect() ? R.drawable.ic_like : R.drawable.ic_like_not);
        holder.cardView.setOnClickListener(v -> {
            MapReceiver.getInstance().setPositionInterface((Latitude, Longitude, country, province, city, district, street) -> {
                L.v(Latitude + " \n" + Longitude + "  有没有啊");
                MyApplication.getDaoSession().getBrowseHistoryDao().insertOrReplace(new BrowseHistory(
                        null, project.getTitle(), project.getLink(),
                        TimeUtils.date2String(new Date(System.currentTimeMillis()))
                        , project.isCollect(), Latitude, Longitude,false, country, province, city, district, street));
            });

            Intent intent = new Intent(mContext, AgentWebActivity.class);
            Bundle bundle =new Bundle();
            bundle.putSerializable(ConstName.OBJ,project);
            intent.putExtras(bundle);
            intent.putExtra(ConstName.ACTIVITY, ConstName.activity.MAIN);
            mContext.startActivity(intent);
        });

        holder.image_collect.setOnClickListener(v -> {
            L.v("HomeList_isCollect", PreferencesUtil.getIsLogin(mContext) + " project");
            if (PreferencesUtil.getIsLogin(mContext)) {
                if (project.isCollect()) {
                    RetrofitUtil.unCollectArticle(project, mContext,this);
                } else {
                    RetrofitUtil.collectArticle(project, mContext,this);
                }
            } else {
                mContext.startActivity(new Intent(mContext, LoginActivity.class)
                        .putExtra(ConstName.ACTIVITY, ConstName.activity.MAIN));
            }
        });
    }

    @Override
    public int getItemCount() {
        return beanList_project.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView image_project, image_collect;
        TextView name, content, author, time;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            image_project = view.findViewById(R.id.project_image);
            image_collect = view.findViewById(R.id.item_project_list_like_iv);
            name = view.findViewById(R.id.project_name);
            author = view.findViewById(R.id.project_author);
            content = view.findViewById(R.id.item_project_list_content_tv);
            time = view.findViewById(R.id.item_project_list_time_tv);
        }
    }
}
