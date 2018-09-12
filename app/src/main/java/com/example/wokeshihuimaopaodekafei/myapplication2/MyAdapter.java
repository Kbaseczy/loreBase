package com.example.wokeshihuimaopaodekafei.myapplication2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MyAdapter extends BaseAdapter {
    Context context;
    ArrayList<HashMap<String,String>> data;

    public MyAdapter(Context context, ArrayList<HashMap<String, String>> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(context).inflate(R.layout.goodsdetail,null);
        ImageView imageView =(ImageView)convertView.findViewById(R.id.imageView);
        TextView txtName02 = (TextView)convertView.findViewById(R.id.txtName02);
        TextView txtInfo = (TextView)convertView.findViewById(R.id.txtInfo);
        TextView txtPrice=(TextView)convertView.findViewById(R.id.txtPrice);
        RatingBar ratingBar=(RatingBar)convertView.findViewById(R.id.ratingBar02);
        Button btnDelete=(Button)convertView.findViewById(R.id.btnDelete);
        imageView.setImageResource(Integer.parseInt(data.get(position).get("img")));
        txtName02.setText(data.get(position).get("carName"));
        txtInfo.setText(data.get(position).get("info"));
        txtPrice.setText(data.get(position).get("price"));
        ratingBar.setRating(Float.parseFloat(data.get(position).get("rating")));
        btnDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(context,"您点击了删除按钮",Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }
}