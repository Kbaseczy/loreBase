package com.example.lorebase.http;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.lorebase.MyApplication;
import com.example.lorebase.bean.Article;
import com.example.lorebase.bean.BiYing;
import com.example.lorebase.bean.TodoTodo;
import com.example.lorebase.contain_const.UrlContainer;
import com.example.lorebase.util.L;
import com.example.lorebase.util.ToastUtil;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil {
    private static RetrofitApi api = MyApplication.retrofit.create(RetrofitApi.class);

    public static void todoDelete(int id, Context context) {
        RetrofitApi api = MyApplication.retrofit.create(RetrofitApi.class);
        retrofit2.Call<TodoTodo> todoDeleteCall = api.postDeleteTodo(id);
        todoDeleteCall.enqueue(new Callback<TodoTodo>() {
            @Override
            public void onResponse(retrofit2.Call<TodoTodo> call, Response<TodoTodo> response) {
                if (response.body() != null) {
                    ToastUtil.showShortToastCenter(response.body().getErrorCode() == 0 ?
                            "删除成功" : response.body().getErrorMsg(),context);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<TodoTodo> call, Throwable t) {

            }
        });
    }

    public static void todoComplete(int id, Context context, boolean is_done) {
        RetrofitApi api = MyApplication.retrofit.create(RetrofitApi.class);
        retrofit2.Call<TodoTodo> todoDoneCall = api.postDoneTodo(id, is_done ? 0 : 1);
        todoDoneCall.enqueue(new Callback<TodoTodo>() {
            @Override
            public void onResponse(retrofit2.Call<TodoTodo> call, Response<TodoTodo> response) {
                if (response.body() != null) {
                    ToastUtil.showShortToastCenter(is_done ? "撤销成功" : "标记完成",context);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<TodoTodo> call, Throwable t) {

            }
        });
    }

    public static void collectArticle(int id, Context context) {
        retrofit2.Call<Article> collectCall = api.collectArticle(id);
        collectCall.enqueue(new Callback<Article>() {
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                ToastUtil.showShortToastCenter("收藏成功",context);
            }

            @Override
            public void onFailure(Call<Article> call, Throwable t) {

            }
        });
    }

    //收藏界面
    public static void deleteArticle(int id, Context context) {
        RetrofitApi api = MyApplication.retrofit.create(RetrofitApi.class);
        retrofit2.Call<Article> articleCancellCall = api.deleteArticle(id, -1);
        articleCancellCall.enqueue(new Callback<Article>() {
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                if (response.body() != null)
                    ToastUtil.showShortToastCenter(response.body().getErrorCode() == 0 ?
                            "删除成功" : response.body().getErrorMsg(),context);
            }

            @Override
            public void onFailure(Call<Article> call, Throwable t) {

            }
        });
    }

    //普通列表界面
    public static void unCollectArticle(int id, Context context) {
        retrofit2.Call<Article> cancelArticleCall = api.cancellPageArticle(id);
        cancelArticleCall.enqueue(new Callback<Article>() {
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                ToastUtil.showShortToastCenter("取消成功",context);
            }

            @Override
            public void onFailure(Call<Article> call, Throwable t) {

            }
        });
    }

    //Context context, ImageView imageView
    public static void getBiYing(Context context, ImageView imageView) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UrlContainer.BI_YING_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
        RetrofitApi api = retrofit.create(RetrofitApi.class);
        retrofit2.Call<BiYing> biYingCall = api.getBiYing();
        biYingCall.enqueue(new Callback<BiYing>() {
            @Override
            public void onResponse(retrofit2.Call<BiYing> call, Response<BiYing> response) {
                if (response.body() != null) {
                    L.v("BYimage", response.body().getImages().get(0).getUrl() + " retrofit");

                }
                String fullUrl = UrlContainer.BI_YING_BASE + response.body().getImages().get(0).getUrl();
                Glide.with(context).load(fullUrl).transition(new DrawableTransitionOptions().crossFade()).into(imageView);
            }

            @Override
            public void onFailure(retrofit2.Call<BiYing> call, Throwable t) {

            }
        });
    }
}
