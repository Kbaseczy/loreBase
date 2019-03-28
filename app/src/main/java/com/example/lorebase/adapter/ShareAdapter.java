package com.example.lorebase.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lorebase.R;
import com.example.lorebase.bean.ShareHistory;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ShareAdapter extends RecyclerView.Adapter {
    private List<ShareHistory> shareHistoryList;

    public void setShareHistoryList(List<ShareHistory> shareHistoryList) {
        this.shareHistoryList = shareHistoryList;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShareViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_share, null));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ShareHistory shareHistory = shareHistoryList.get(position);
        if (holder instanceof ShareViewHolder) {
            ((ShareViewHolder) holder).title.setText(shareHistory.getTitle());
            ((ShareViewHolder) holder).link.setText(shareHistory.getLink());
            ((ShareViewHolder) holder).date.setText(shareHistory.getDate());
            ((ShareViewHolder) holder).share_man.setText(shareHistory.getShareMan());
            ((ShareViewHolder) holder).cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return shareHistoryList.size();
    }

    public class ShareViewHolder extends RecyclerView.ViewHolder {
        TextView title, link, date, share_man;
        CardView cardView;

        ShareViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            title = itemView.findViewById(R.id.share_title);
            link = itemView.findViewById(R.id.share_link);
            date = itemView.findViewById(R.id.share_date);
            share_man = itemView.findViewById(R.id.share_man);
        }


    }
}
