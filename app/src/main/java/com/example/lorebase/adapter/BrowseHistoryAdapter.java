package com.example.lorebase.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import com.example.lorebase.util.L;
import com.example.lorebase.widget.behavior.CustomTextViewCollect;

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

    public void notifyDeleteAll() {
        datasBeanList.clear();
        notifyDataSetChanged();
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
            intent.putExtra(ConstName.IS_OUT, true);
            intent.setData(Uri.parse(browseHistory.getLink()));
            mContext.startActivity(intent);
        });

        holder.cardView.setOnLongClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            @SuppressLint("InflateParams")
            View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_view, null);
            CustomTextViewCollect positionContent = dialogView.findViewById(R.id.dialog_position_content);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(browseHistory.getProvince()).append("\n").append("\n")
                    .append(browseHistory.getCity()).append("\n").append("\n")
                    .append(browseHistory.getDistrict()).append("\n").append("\n")
                    .append(browseHistory.getStreet());
            positionContent.setText(stringBuilder);
            builder.setView(dialogView)
                    .create()
                    .show();
            L.v("" + browseHistory.getCountry() + "\t" + browseHistory.getCity() + "\t" + browseHistory.getStreet());
            return false;
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
