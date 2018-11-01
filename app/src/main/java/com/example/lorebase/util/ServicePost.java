package com.example.lorebase.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServicePost {
    // 通过 POST 方式获取HTTP服务器数据
    private static String IP = "203.195.224.148:8080";
    public static String executeHttpPost(String username, String password) {

        try {
            String path = "http://" + IP + "/kbase/user/user_login.do";

            // 发送指令和信息
            Map<String, String> params = new HashMap<>();
            params.put("account", username);
            params.put("password", password);

            return sendPOSTRequest(path, params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // 处理发送数据请求
    private static String sendPOSTRequest(String path, Map<String, String> params, String encoding) throws Exception {

        List<NameValuePair> pairs = new ArrayList<>();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs, encoding);

        HttpPost post = new HttpPost(path);
        post.setEntity(entity);
        DefaultHttpClient client = new DefaultHttpClient();
        // 请求超时
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
        // 读取超时
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
        HttpResponse response = client.execute(post);

        // 判断是否成功收取信息
        if (response.getStatusLine().getStatusCode() == 200) {
            return getInfo(response);
        }

        // 未成功收取信息，返回空指针
        return null;
    }

    // 收取数据
    private static String getInfo(HttpResponse response) throws Exception {

        HttpEntity entity = response.getEntity();
        InputStream is = entity.getContent();
        // 将输入流转化为byte型
        byte[] data = Service.read(is);
        // 转化为字符串
        return new String(data, "UTF-8");
    }
}
