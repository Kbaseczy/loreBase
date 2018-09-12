package com.example.wokeshihuimaopaodekafei.myapplication2;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;



public class GoodsAdapter extends BaseAdapter {
    private List<Map> list ;

	private Context context ;

	public GoodsAdapter(Context context, List<Map> list) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list = list;
	}
	@Override
	public int getCount() {
		int count = 0 ; 
		if(list!=null){
			count = list.size();
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if(convertView==null){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_person, parent,false);
			ViewHolder holder = new ViewHolder();
			holder.cover = (ImageView) convertView.findViewById(R.id.goodImage);
			holder.goodsName = (TextView) convertView.findViewById(R.id.goodsName);
			holder.goodsPrice = (TextView) convertView.findViewById(R.id.goodsPrice);
			holder.goodsDiscount = (TextView) convertView.findViewById(R.id.goodsDiscount);
			
			convertView.setTag(holder);

		}
        final ViewHolder holder = (ViewHolder) convertView.getTag();
	    String goodsname= (String) list.get(position).get("goodsname");
		String goodsprice= (String) list.get(position).get("goodsprice");
		String cover= (String) list.get(position).get("cover");
		String goodsdiscount= (String) list.get(position).get("goodsdiscount");
		String imageUrl = "http://10.0.2.2:8080/eshop/"+cover;
		
		holder.goodsName.setText(goodsname);
		holder.goodsPrice.setText(goodsprice);
		holder.goodsDiscount.setText(goodsdiscount);

		Bitmap bitmap = ImageUtil.readImage(imageUrl);
		if(bitmap!=null){
			holder.cover.setImageBitmap(bitmap);
		}else{
			//异步任务下载
			new DownImageTask(new DownImageTask.DownLoadBack() {
				@Override
				public void response(Bitmap bitmap) {
					holder.cover.setImageBitmap(bitmap);
				}
			}).execute(imageUrl);
		}
		return convertView;
	}
	
   class ViewHolder{
	   TextView goodsName, goodsDiscount, goodsPrice;
	ImageView cover;
}

}
