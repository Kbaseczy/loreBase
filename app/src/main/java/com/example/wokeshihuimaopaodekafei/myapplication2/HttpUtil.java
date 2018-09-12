package com.example.wokeshihuimaopaodekafei.myapplication2;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpUtil {
      public static byte[] getJsonString(String path) throws ClientProtocolException, IOException{
    	  byte[] data = null;
  		HttpGet get = new HttpGet(path);
  		HttpClient client = new DefaultHttpClient();
  		
  		HttpResponse response ;
  		response = client.execute(get);
  		if(response.getStatusLine().getStatusCode()==200)
  		{
  			data = EntityUtils.toByteArray(response.getEntity());
  		}
  		return data;
    	  
      }

	

	
}
