package com.example.lorebase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lorebase.R;
import com.example.lorebase.bean.ArticleInfo;

import java.util.List;

public class ArticleAdapter extends BaseAdapter {
    private List<ArticleInfo.DataBean.ListBean> list;
    private Context ctx;
    private LayoutInflater mInflater;

    public ArticleAdapter(Context context, List<ArticleInfo.DataBean.ListBean> list) {
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
        ArticleInfo.DataBean.ListBean bean = list.get(position);
        ViewHolder viewHolder ;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item, null);
            viewHolder = new ViewHolder();
            viewHolder.name =  convertView.findViewById(R.id.tv_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(" 名称  ："+bean.getName() +"         作者： "+bean.getAuthor() );
        return convertView;
    }

    static class ViewHolder {
        public TextView name;
    }

}
