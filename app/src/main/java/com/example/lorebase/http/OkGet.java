package com.example.lorebase.http;

import com.example.lorebase.contain_const.UrlContainer;
import com.example.lorebase.util.L;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

public class OkGet {
    private static void getData(List<?> list, String urla) {
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
}
