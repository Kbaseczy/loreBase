package com.example.lorebase.http;

import android.content.Context;
import android.widget.Toast;

import com.example.lorebase.contain_const.UrlContainer;
import com.example.lorebase.util.L;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

public class OkGet {
    public static void getData(List<?> list, String urla) {
        String url = UrlContainer.baseUrl + urla;
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        L.v("LoreListFragment " + response);


                        //TODO :Expected Object but Array -> ok
                        // todo 请求服务器正常，并获取响应。   数据解析存储ok
                        Gson gson = new Gson();
                    }
                });
    }

    public static void todoDelete(int id, Context context) {
        String url = UrlContainer.TODO_DELETE + id + "/json";
        OkHttpUtils
                .post()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        L.v("LoreListFragment " + response);
                        try {
                            if (new JSONObject(response).getInt("errorCode") == 0) {
                                Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public static void todoComplete(int id, Context context, int status, boolean is_done) {
        String url = UrlContainer.DONE_TODO_URI + id + "/json";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("status", String.valueOf(status))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            if (new JSONObject(response).getInt("errorCode") == 0) {
                                if (is_done)
                                    Toast.makeText(context, "撤销成功", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(context, "标记成功", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
