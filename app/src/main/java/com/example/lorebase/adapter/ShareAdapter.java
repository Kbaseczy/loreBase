package com.example.lorebase.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lorebase.R;
import com.example.lorebase.bean.ShareHistory;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.ui.activity.AgentWebActivity;
import com.example.lorebase.util.ToastUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ShareAdapter extends RecyclerView.Adapter {
    private List<ShareHistory> shareHistoryList;
    Context mContext;

    public ShareAdapter(Context context) {
        this.mContext = context;
    }

    public void setShareHistoryList(List<ShareHistory> shareHistoryList) {
        this.shareHistoryList = shareHistoryList;
    }

    public void notifyDeleteAll() {
        shareHistoryList.clear();
        notifyDataSetChanged();
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
            ((ShareViewHolder) holder).cardView.setOnLongClickListener(v -> {
                ToastUtil.showShortToastCenter("此处分享~", mContext);
                Intent intent_share = new Intent();
                intent_share.setAction(Intent.ACTION_SEND);
                intent_share.setType("text/plain");
                intent_share.putExtra(Intent.EXTRA_SUBJECT, ConstName.LORE_BASE);
                intent_share.putExtra(Intent.EXTRA_TEXT, shareHistory.getTitle()
                        + ":" + String.valueOf(shareHistory.getLink()));
                intent_share = Intent.createChooser(intent_share, "请选择分享路径");
                mContext.startActivity(intent_share);
                return false;
            });

            ((ShareViewHolder) holder).cardView.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, AgentWebActivity.class);
                intent.putExtra(ConstName.TITLE, shareHistory.getTitle());
                intent.setData(Uri.parse(shareHistory.getLink()));
                intent.putExtra(ConstName.IS_OUT, true);
                mContext.startActivity(intent);
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
