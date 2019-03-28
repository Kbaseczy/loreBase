package com.example.lorebase.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lorebase.R;
import com.example.lorebase.bean.BrowseHistory;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.ui.activity.AgentWebActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class BrowseHistoryAdapter extends RecyclerView.Adapter<BrowseHistoryAdapter.ViewHolder> {

    private Context mContext;
    private List<BrowseHistory> datasBeanList;

    public BrowseHistoryAdapter(List<BrowseHistory> datasBeanList) {
        this.datasBeanList = datasBeanList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_browse_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BrowseHistory browseHistory = datasBeanList.get(position);

        holder.date.setText(browseHistory.getDate());
        holder.title.setText(browseHistory.getTitle());

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, AgentWebActivity.class);
            intent.putExtra(ConstName.TITLE, browseHistory.getTitle());
            intent.putExtra(ConstName.ACTIVITY, ConstName.activity.BROWSE_HOSTORY);
            intent.putExtra(ConstName.IS_COLLECT, browseHistory.getIs_colloct());
            intent.putExtra(ConstName.IS_OUT, browseHistory.getIs_out());
            intent.setData(Uri.parse(browseHistory.getLink()));
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return datasBeanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView date, title;

        ViewHolder(@NonNull View view) {
            super(view);
            cardView = (CardView) view;
            date = view.findViewById(R.id.browse_history_article_date);
            title = view.findViewById(R.id.browse_history_article_title);
        }
    }
}
