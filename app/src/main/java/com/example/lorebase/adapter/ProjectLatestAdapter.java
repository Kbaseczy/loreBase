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

import com.bumptech.glide.Glide;
import com.example.lorebase.R;
import com.example.lorebase.bean.ProjectLatest;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.http.CollectArticle;
import com.example.lorebase.ui.activity.AgentWebActivity;
import com.example.lorebase.ui.activity.LoginActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ProjectLatestAdapter extends RecyclerView.Adapter<ProjectLatestAdapter.ViewHolder> {
    private Context mContext;
    private List<ProjectLatest.DataBean.DatasBean> list_project;
    private boolean isCollect = false;

    public ProjectLatestAdapter(List<ProjectLatest.DataBean.DatasBean> list_project) {
        this.list_project = list_project;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.project_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            ProjectLatest.DataBean.DatasBean project = list_project.get(position);
            Intent intent = new Intent(mContext, AgentWebActivity.class);
            intent.putExtra(ConstName.TITLE, project.getTitle());
            intent.putExtra(ConstName.PROJECT_AUTHOR, project.getAuthor());
            intent.putExtra(ConstName.ID, project.getId());
            intent.setData(Uri.parse(project.getLink()));
            mContext.startActivity(intent);
        });

        SharedPreferences sp = mContext.getSharedPreferences(ConstName.LOGIN_DATA, Context.MODE_PRIVATE);
        boolean isLogin = sp.getBoolean(ConstName.IS_LOGIN, false);
        //todo 收藏图标点击事件
        holder.collect_image.setOnClickListener(v -> {
                    //获取某子项位置，并得到该项的数据对象
                    int position = holder.getAdapterPosition();
                    ProjectLatest.DataBean.DatasBean project = list_project.get(position);
                    if (isLogin) {
                        if (!isCollect) {
                            CollectArticle.collectArticle(mContext, project.getId());
                            holder.collect_image.setImageResource(R.drawable.ic_like); //点击图标后变为红色表示已收藏
                            isCollect = true;
                        } else {
                            CollectArticle.unCollect_originID(mContext, project.getId());
                            holder.collect_image.setImageResource(R.drawable.ic_like_not);
                            isCollect = false;
                        }
                    } else {
                        mContext.startActivity(new Intent(mContext, LoginActivity.class));
                    }
                }
        );
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProjectLatest.DataBean.DatasBean project = list_project.get(position);
        holder.project_text.setText(project.getTitle());
        holder.project_author.setText(project.getAuthor());
        holder.project_desc.setText(project.getDesc());
        holder.project_date.setText(project.getNiceDate());
        Glide.with(mContext).load(project.getEnvelopePic()).into(holder.project_image);
        if (isCollect)
            holder.collect_image.setImageResource(R.drawable.ic_like);
        else
            holder.collect_image.setImageResource(R.drawable.ic_like_not);
    }

    @Override
    public int getItemCount() {
        return list_project.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView project_image, collect_image;
        TextView project_text, project_author, project_desc, project_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            project_image = itemView.findViewById(R.id.project_image);
            collect_image = itemView.findViewById(R.id.item_project_list_like_iv);
            project_text = itemView.findViewById(R.id.project_name);
            project_author = itemView.findViewById(R.id.project_author);
            project_desc = itemView.findViewById(R.id.item_project_list_content_tv);
            project_date = itemView.findViewById(R.id.item_project_list_time_tv);
        }
    }
}
