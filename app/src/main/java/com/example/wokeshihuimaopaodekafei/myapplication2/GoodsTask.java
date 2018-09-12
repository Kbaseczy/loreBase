package com.example.wokeshihuimaopaodekafei.myapplication2;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
//import android.widget.GridView;
import android.widget.ListView;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import java.io.IOException;
import java.util.List;
import java.util.Map;


public class GoodsTask extends AsyncTask<String, Void, List<Map>> {

	private Context context;
	private ListView listView;

	public GoodsTask(Context context,ListView listView) {
		super();
		this.context = context;
		this.listView = listView;
	}
	@Override
	protected List<Map> doInBackground(String... params) {
		// TODO Auto-generated method stub
		String url = params[0];
		List<Map> list = null;
		if(url!=null)
		{
			try {
				Log.d("1111111","url地址是："+url);
				byte[] data = HttpUtil.getJsonString(url);
				String jsonString = new String(data,"utf-8");
				list = ParseTool.parseGoods(jsonString);
				Log.d("1111111","返回的list是："+list);
				
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}
	@Override
	protected void onPostExecute(List<Map> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		GoodsAdapter adapter = new GoodsAdapter(context, result);
		listView.setAdapter(adapter);
	}
}
