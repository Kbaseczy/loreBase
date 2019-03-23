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
import com.example.lorebase.MapReceiver;
import com.example.lorebase.MyApplication;
import com.example.lorebase.R;
import com.example.lorebase.bean.BrowseHistory;
import com.example.lorebase.bean.Project;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.http.RetrofitUtil;
import com.example.lorebase.ui.activity.AgentWebActivity;
import com.example.lorebase.ui.activity.LoginActivity;
import com.example.lorebase.util.L;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {


    private List<Project.DataBean.DatasBean> beanList_project;
    private Context mContext;

    public ProjectAdapter(Context context,List<Project.DataBean.DatasBean> beanList_project) {
        this.beanList_project = beanList_project;
        this.mContext = context;
    }
    public void addBeanList_project(List<Project.DataBean.DatasBean> beanList_project) {
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
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Project.DataBean.DatasBean project = beanList_project.get(position);
        Glide.with(mContext).load(project.getEnvelopePic()).into(holder.image_project);
        holder.name.setText(project.getTitle());
        holder.author.setText(project.getAuthor());
        holder.time.setText(project.getNiceDate());
        holder.content.setText(project.getDesc());
        if (project.isCollect()) {
            holder.image_collect.setImageResource(R.drawable.ic_like);

        } else {
            holder.image_collect.setImageResource(R.drawable.ic_like_not);

        }
        holder.cardView.setOnClickListener(v -> {
            MapReceiver.getInstance().setPositionInterface((Latitude, Longitude) -> {
                L.v(Latitude + " \n" + Longitude + "  有没有啊");
                MyApplication.getDaoSession().getBrowseHistoryDao().insertOrReplace(new BrowseHistory(
                        null, project.getTitle(), project.getLink(), project.getNiceDate(),project.isCollect()
                        ,Latitude,Longitude));
            });

            Intent intent = new Intent(mContext, AgentWebActivity.class);
            intent.putExtra(ConstName.TITLE, project.getTitle());
            intent.putExtra(ConstName.ID, project.getId());
            intent.putExtra(ConstName.ACTIVITY, ConstName.activity.MAIN);
            intent.putExtra(ConstName.IS_COLLECT,project.isCollect());
            intent.setData(Uri.parse(project.getLink()));
            mContext.startActivity(intent);
        });
        SharedPreferences sp = mContext.getSharedPreferences(ConstName.LOGIN_DATA, Context.MODE_PRIVATE);
        boolean isLogin = sp.getBoolean(ConstName.IS_LOGIN, false);
        holder.image_collect.setOnClickListener(v -> {
            if (isLogin) {
                if (project.isCollect()) {
                    RetrofitUtil.unCollectArticle( project.getId(),mContext);
                    holder.image_collect.setImageResource(R.drawable.ic_like_not);
                }
                if (!project.isCollect()) {
                    RetrofitUtil.collectArticle(project.getId(),mContext);
                }
            } else {
                mContext.startActivity(new Intent(mContext, LoginActivity.class));
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
