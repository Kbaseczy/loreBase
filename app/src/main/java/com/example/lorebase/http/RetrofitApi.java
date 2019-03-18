package com.example.lorebase.http;


import com.example.lorebase.bean.TodoTodo;
import com.example.lorebase.bean.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitApi {
    @POST("user/login")
    Call<User> login(@Query("username") String username,
                     @Query("password") String password);

    @GET("{page}/json?status=1&orderby=2")
    Call<TodoTodo> getDoneTodoList(@Path("page") int page);

    @GET("lg/todo/v2/list/{page}/json?status=0")
    Call<TodoTodo> getUnTodoList(@Path("page") int page);
}
