package com.example.lorebase.adapter;

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
import com.example.lorebase.bean.ProjectLatest;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.ui.activity.AgentWebActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ProjectLatestAdapter extends RecyclerView.Adapter<ProjectLatestAdapter.ViewHolder> {
    private Context mContext;
    private List<ProjectLatest.DataBean.DatasBean> list_project;
    public ProjectLatestAdapter(List<ProjectLatest.DataBean.DatasBean> list_project){
        this.list_project = list_project;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.project_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            ProjectLatest.DataBean.DatasBean project = list_project.get(position);
            Intent intent = new Intent(mContext,AgentWebActivity.class);
            intent.putExtra(ConstName.TITLE,project.getTitle());
            intent.putExtra(ConstName.PROJECT_AUTHOR,project.getAuthor());
            intent.setData(Uri.parse(project.getLink()));
            mContext.startActivity(intent);
        });

        //todo 收藏图标点击事件
        holder.project_image.setOnClickListener(v ->{
            //获取某子项位置，并得到该项的数据对象
            int position = holder.getAdapterPosition();
            ProjectLatest.DataBean.DatasBean project = list_project.get(position);

            holder.project_image.setImageResource(R.drawable.ic_like); //点击图标后变为红色表示已收藏

                }
        );
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //绑定数据    从服务器获取响应并解析后数据存储在实体bean中然后给list_project，
        // so->从list_project获取之，后加到viewHolder的item布局中
        ProjectLatest.DataBean.DatasBean project = list_project.get(position);
        holder.project_text.setText(project.getTitle());
        holder.project_author.setText(project.getAuthor());
        holder.project_desc.setText(project.getDesc());
        holder.project_date.setText(project.getNiceDate());
        Glide.with(mContext).load(project.getEnvelopePic()).into(holder.project_image);
    }

    @Override
    public int getItemCount() {
        return list_project.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView project_image;
        TextView project_text,project_author,project_desc,project_date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            project_image = itemView.findViewById(R.id.project_image);
            project_text = itemView.findViewById(R.id.project_name);
            project_author = itemView.findViewById(R.id.project_author);
            project_desc = itemView.findViewById(R.id.item_project_list_content_tv);
            project_date = itemView.findViewById(R.id.item_project_list_time_tv);
        }
    }
}
