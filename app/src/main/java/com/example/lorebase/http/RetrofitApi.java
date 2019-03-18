package com.example.lorebase.http;


import com.example.lorebase.bean.Article;
import com.example.lorebase.bean.TodoTodo;
import com.example.lorebase.bean.User;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitApi {
    @POST("user/login")
    Call<User> login(@Query("username") String username,
                     @Query("password") String password);

    @GET("lg/todo/v2/list/{page}/json?status=1&orderby=2")
    Call<TodoTodo> getDoneTodoList(@Path("page") int page);

    @GET("lg/todo/v2/list/{page}/json?status=0")
    Call<TodoTodo> getUnTodoList(@Path("page") int page);

    @GET("lg/collect/list/{page}/json")
    Call<Article> getCollect(@Path("page") int page);

    /*
        添加TODO
     */
    @FormUrlEncoded
    @POST("lg/todo/add/json")
    Call<TodoTodo> postAddTodo(@FieldMap Map<String,String> map);

    /*
        修改TODO
     */
    @FormUrlEncoded
    @POST("lg/todo/update/{id}/json")
    Call<TodoTodo> postEditTodo(@Path("id") int id,@FieldMap Map<String,String> map);

    @POST("lg/todo/delete/{id}/json")
    Call<TodoTodo> postDeleteTodo(@Path("id") int page);

    @POST("lg/todo/done/{id}/json?status=1")
    Call<TodoTodo> postDoneTodo(@Path("id") int page);

    @POST("lg/todo/done/{id}/json?status=0")
    Call<TodoTodo> postUnDoneTodo(@Path("id") int page);

}
