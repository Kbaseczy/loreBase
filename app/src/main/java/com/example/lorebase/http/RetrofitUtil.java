package com.example.lorebase.http;

import android.content.Context;
import android.widget.Toast;

import com.example.lorebase.MyApplication;
import com.example.lorebase.bean.Article;
import com.example.lorebase.bean.TodoTodo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitUtil {
    private static RetrofitApi api = MyApplication.retrofit.create(RetrofitApi.class);
    public static void todoDelete(int id, Context context) {
        RetrofitApi api = MyApplication.retrofit.create(RetrofitApi.class);
        retrofit2.Call<TodoTodo> todoDeleteCall = api.postDeleteTodo(id);
        todoDeleteCall.enqueue(new Callback<TodoTodo>() {
            @Override
            public void onResponse(retrofit2.Call<TodoTodo> call, Response<TodoTodo> response) {
                if (response.body() != null) {
                    Toast.makeText(context, response.body().getErrorCode() == 0 ?
                            "删除成功" : response.body().getErrorMsg(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(context, is_done ? "撤销成功" : "标记完成", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<TodoTodo> call, Throwable t) {

            }
        });
    }

    public static void collectArticle(int id,Context context){
        retrofit2.Call<Article> collectCall = api.collectArticle(id);
        collectCall.enqueue(new Callback<Article>() {
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                Toast.makeText(context, "收藏成功", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(context, response.body().getErrorCode() == 0 ?
                            "删除成功" : response.body().getErrorMsg(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Article> call, Throwable t) {

            }
        });
    }

    //普通列表界面
    public static void unCollectArticle(int id ,Context context){
        retrofit2.Call<Article> cancelArticleCall = api.cancellPageArticle(id);
        cancelArticleCall.enqueue(new Callback<Article>() {
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                Toast.makeText(context, "取消成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Article> call, Throwable t) {

            }
        });
    }

}