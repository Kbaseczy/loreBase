package com.example.lorebase.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lorebase.R;
import com.example.lorebase.bean.TodoTodo;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.http.OkGet;
import com.example.lorebase.ui.activity.TodoEditActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private List<TodoTodo.DataBean.DatasBean> list_todo;
    private boolean is_done;
    Context mContext;

    public TodoAdapter(Context context, List<TodoTodo.DataBean.DatasBean> list_todo, boolean is_done) {
        this.list_todo = list_todo;
        this.is_done = is_done;
        this.mContext = context;
    }

    public void addList_todo(List<TodoTodo.DataBean.DatasBean> list_todo) {
        this.list_todo.addAll(list_todo);
    }

    @NonNull
    @Override
    public TodoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null)
            mContext = parent.getContext();
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_todo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TodoAdapter.ViewHolder holder, int position) {
        TodoTodo.DataBean.DatasBean datasBean = list_todo.get(position);
        holder.action_complete.setImageResource(is_done ? R.drawable.cancel_todo : R.drawable.complete_todo);
        holder.item_name.setText(datasBean.getTitle());
        holder.item_desc.setText(datasBean.getContent());
        holder.item_date.setText(datasBean.getDateStr());

        holder.action_complete.setOnClickListener(v -> {
            OkGet.todoComplete(datasBean.getId(), mContext, is_done ? 0 : 1, is_done);
            notifyDataSetChanged();
        });

        holder.action_delete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(R.string.tip)
                    .setMessage(R.string.tip_content_clear_history)
                    .setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(R.string.ok, (dialogInterface, i) ->
                            OkGet.todoDelete(datasBean.getId(), mContext))
                    .show();
            notifyDataSetChanged();
            notifyItemRemoved(position);
        });

        holder.view.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ConstName.TODO_BEAN, datasBean);
            mContext.startActivity(new Intent(mContext, TodoEditActivity.class)
                    .putExtra(ConstName.TODO_BEAN, bundle)); //todo 未测试，获取list时登陆没通过。  通过序列化传递bean对象
        });
    }

    @Override
    public int getItemCount() {
        return list_todo.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView item_name, item_desc, item_date;
        ImageView action_complete, action_delete;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.item_name);
            item_desc = itemView.findViewById(R.id.item_des);
            item_date = itemView.findViewById(R.id.item_date);
            action_complete = itemView.findViewById(R.id.item_action_complete);
            action_delete = itemView.findViewById(R.id.item_action_delete);
            view = itemView.findViewById(R.id.todo_layout);
        }
    }


}


