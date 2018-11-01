package com.example.lorebase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lorebase.R;
import com.example.lorebase.bean.LoreTree;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

/*
        todo 1.两个List分别存储父级和子级的信息，这里的item需要的是父级的name字段和子级的name字段
        todo 2.传递到下一界面的信息是：父级和子级的id
        err:childrenBeanList:java.lang.Object java.util.List.get(int)' on a null object reference
 */
public class LoreTreeAdapter extends RecyclerView.Adapter<LoreTreeAdapter.ViewHolder> {

    private Context mContext;
    private List<LoreTree.DataBean.ChildrenBean> childrenBeanList ;
    private List<LoreTree.DataBean> fatherBeanList;
    public LoreTreeAdapter(List<LoreTree.DataBean> fatherBeanList, List<LoreTree.DataBean.ChildrenBean> childrenBeanList){
        this.fatherBeanList = fatherBeanList;
        this.childrenBeanList = childrenBeanList;
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
            Toast.makeText(mContext, "LoreTree list click", Toast.LENGTH_SHORT).show();

        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LoreTree.DataBean loreTree_father = fatherBeanList.get(position);
//        LoreTree.DataBean.ChildrenBean loreTree_child = childrenBeanList.get(position);
        holder.father_name.setText(loreTree_father.getName());
//        holder.child_name.setText(loreTree_child.getName());
    }

    @Override
    public int getItemCount() {
        return fatherBeanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
