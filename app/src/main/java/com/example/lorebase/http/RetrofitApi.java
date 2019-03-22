package com.example.lorebase.http;

import com.example.lorebase.bean.Article;
import com.example.lorebase.bean.Banner;
import com.example.lorebase.bean.BiYing;
import com.example.lorebase.bean.LoreTree;
import com.example.lorebase.bean.News;
import com.example.lorebase.bean.Project;
import com.example.lorebase.bean.ProjectChapter;
import com.example.lorebase.bean.TodoTodo;
import com.example.lorebase.bean.User;
import com.example.lorebase.bean.WeChat;
import com.example.lorebase.contain_const.UrlContainer;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitApi {
    //登陆
    @POST("user/login")
    Call<User> login(@Query("username") String username,
                     @Query("password") String password);

    //注销
    @GET(UrlContainer.LOGOUT)
    Call<User> logout();

    //必应每日一图
    @GET("HPImageArchive.aspx?format=js&idx=0&n=1")
    Call<BiYing> getBiYing();

    //首页文章列表
    @GET(UrlContainer.HOME_LIST)
    Call<Article> getHomeArticle(@Path("page") int page);

    //首页news
    @GET(UrlContainer.FRIEND)
    Call<News> getHomeNews();

    //首页banner
    @GET(UrlContainer.MAIN_BANNER)
    Call<Banner> getHomeBanner();

    //知识体系
    @GET(UrlContainer.TREE)
    Call<LoreTree> getLoreTree();

    //项目目录
    @GET(UrlContainer.PROJECT)
    Call<ProjectChapter> getProjectChapter();

    //微信公众号目录
    @GET(UrlContainer.WX_ARTICLE_CHAPTER)
    Call<WeChat> getWeChatChapter();

    //微信公众号文章
    @GET(UrlContainer.WX_ARTICLE_LIST)
    Call<Article> getWXList(@Path("id") int id,@Path("page") int page);

    //项目列表
    @GET(UrlContainer.PROJECT_LIST)
    Call<Project> getProjectList(@Path("page") int page,@Query("cid") int cid);

    //搜索
    @FormUrlEncoded
    @POST(UrlContainer.SEARCH)
    Call<Article> getSearchArticle(@Path("page") int page,@Field("k") String key_word);

    //获取完成事项
    @GET("lg/todo/v2/list/{page}/json?status=1&orderby=2")
    Call<TodoTodo> getDoneTodoList(@Path("page") int page);

    //获取待办事项
    @GET("lg/todo/v2/list/{page}/json?status=0")
    Call<TodoTodo> getUnTodoList(@Path("page") int page);

    //收藏列表
    @GET("lg/collect/list/{page}/json")
    Call<Article> getCollect(@Path("page") int page);

    // 添加TODO
    @FormUrlEncoded
    @POST("lg/todo/add/json")
    Call<TodoTodo> postAddTodo(@FieldMap Map<String, String> map);

    //修改TODO
    @FormUrlEncoded
    @POST("lg/todo/update/{id}/json")
    Call<TodoTodo> postEditTodo(@Path("id") int id, @FieldMap Map<String, String> map);

    //刪除TODO
    @POST("lg/todo/delete/{id}/json")
    Call<TodoTodo> postDeleteTodo(@Path("id") int id);

    //更新TODO状态  待办/完成
    @POST("lg/todo/done/{id}/json")
    Call<TodoTodo> postDoneTodo(@Path("id") int id, @Query("status") int status);

    @POST(UrlContainer.COLLECT_ARTICLE)
    Call<Article> collectArticle(@Path("id") int id);

    //取消收藏 收藏界面  ok
    @POST(UrlContainer.DELETE_COLLECT_ARTICLE)
    @FormUrlEncoded
    Call<Article> deleteArticle(@Path("id") int id, @Field("originId") int originId);

    //取消收藏 文章列表文章 站内文章  ok
    @POST(UrlContainer.UNCOLLECT_ARTICLE)
    Call<Article> cancellPageArticle(@Path("id") int id);

    //知识体系下文章列表
    @GET(UrlContainer.TREE_LIST)
    Call<Article> getLoreList(@Path("page") int page, @Query("cid") int cid);





}
