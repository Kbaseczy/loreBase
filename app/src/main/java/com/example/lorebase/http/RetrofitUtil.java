package com.example.lorebase.http;

import android.content.Context;
import android.widget.Toast;

import com.example.lorebase.MyApplication;
import com.example.lorebase.bean.TodoTodo;

import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitUtil {

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



}
