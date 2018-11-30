package com.example.lorebase.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lorebase.R;
import com.example.lorebase.bean.LoreTree;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.ui.activity.LoreActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
/*
        1.两个List分别存储父级和子级的信息，这里的item需要的是父级的name字段和子级的name字段 -> err
        2.传递到下一界面的信息是：父级和子级的id
        err:childrenBeanList:java.lang.Object java.util.List.get(int)' on a null object reference
        -> todo child依赖father，这样分离创建2个list使得child独立，便是空对象，child对象依赖father对象，通过father获取child
        -> 即 father.getChild();
 */
public class LoreTreeAdapter extends RecyclerView.Adapter<LoreTreeAdapter.ViewHolder> {

    private Context mContext;
    private List<LoreTree.DataBean> fatherBeanList;
    public LoreTreeAdapter(List<LoreTree.DataBean> fatherBeanList){
        this.fatherBeanList = fatherBeanList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_lore_tree_list,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(v->{
            int position = holder.getAdapterPosition();
            //获取当前点击项对象,在LoreActivity 也需要对这个对象进行遍历取出每一个子级对象
            //"father":{"child","child","child"};
            LoreTree.DataBean loreTree_father = fatherBeanList.get(position);
//
//            LoreTree.DataBean loreTree_father = new LoreTree.DataBean();
            //包装数据并传递  -> LoreActivity
            Bundle bundle  = new Bundle();
            bundle.putSerializable(ConstName.OBJ, loreTree_father);
            Intent intent = new Intent(mContext,LoreActivity.class);
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LoreTree.DataBean loreTree_father = fatherBeanList.get(position);
        holder.father_name.setText(loreTree_father.getName());
        holder.child_name.setText(" ");
        //通过父级（一级目录名）获取它的子级对象(loreTree_father.getChildren())，然后遍历这个子级对象的数据（二级目录名）
        for(LoreTree.DataBean.ChildrenBean child : loreTree_father.getChildren()){
            holder.child_name.append(child.getName()+"   ");
        }
    }

    @Override
    public int getItemCount() {
        return fatherBeanList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView father_name,child_name;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            father_name = itemView.findViewById(R.id.title_first);
            child_name = itemView.findViewById(R.id.title_second);
        }
    }
}
