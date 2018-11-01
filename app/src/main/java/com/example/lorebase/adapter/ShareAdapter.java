package com.example.lorebase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lorebase.R;
import com.example.lorebase.bean.ShareInfo;

import java.util.List;

public class ShareAdapter extends BaseAdapter{
    private List<ShareInfo.DataBean> list;
    private Context ctx;
    private LayoutInflater mInflater;

    public ShareAdapter(Context context, List<ShareInfo.DataBean> list) {
        ctx = context;
        this.list = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ShareInfo.DataBean bean = list.get(position);
        ShareAdapter.ViewHolder viewHolder ;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item, null);
            viewHolder = new ShareAdapter.ViewHolder();
            viewHolder.name =  convertView.findViewById(R.id.tv_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ShareAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(" articleID  ："+bean.getArticleId() +"       sharing person： "+bean.getUserId() );
        return convertView;
    }

    static class ViewHolder {
        public TextView name;
    }
}
