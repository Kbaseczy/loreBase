package com.example.lorebase.http;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.lorebase.MyApplication;
import com.example.lorebase.R;
import com.example.lorebase.adapter.MyselfAdapter;
import com.example.lorebase.adapter.TodoAdapter;
import com.example.lorebase.bean.Article;
import com.example.lorebase.bean.BiYing;
import com.example.lorebase.bean.TodoTodo;
import com.example.lorebase.contain_const.UrlContainer;
import com.example.lorebase.util.L;
import com.example.lorebase.util.ToastUtil;
import com.example.lorebase.util.TotoDataInterface;

import androidx.recyclerview.widget.RecyclerView;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil {
    private static RetrofitApi api = MyApplication.retrofit.create(RetrofitApi.class);

    public static void setTotoDataInterface(TotoDataInterface totoDataInterface) {
        RetrofitUtil.totoDataInterface = totoDataInterface;
    }

    private static TotoDataInterface totoDataInterface;

    public static void todoDelete(int id, int position, Context context, TodoAdapter adapter) {
        RetrofitApi api = MyApplication.retrofit.create(RetrofitApi.class);
        retrofit2.Call<TodoTodo> todoDeleteCall = api.postDeleteTodo(id);
        todoDeleteCall.enqueue(new Callback<TodoTodo>() {
            @Override
            public void onResponse(retrofit2.Call<TodoTodo> call, Response<TodoTodo> response) {
                if (response.body() != null) {
                    ToastUtil.showShortToastCenter(response.body().getErrorCode() == 0 ?
                            "已删除" : response.body().getErrorMsg(), context);
                    adapter.removeItem(position);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<TodoTodo> call, Throwable t) {

            }
        });
    }

    public static void todoComplete(TodoTodo.DataBean.DatasBean datasBean, int position, Context context, boolean is_done, TodoAdapter adapter) {
        RetrofitApi api = MyApplication.retrofit.create(RetrofitApi.class);
        retrofit2.Call<TodoTodo> todoDoneCall = api.postDoneTodo(datasBean.getId(), is_done ? 0 : 1);
        todoDoneCall.enqueue(new Callback<TodoTodo>() {
            @Override
            public void onResponse(retrofit2.Call<TodoTodo> call, Response<TodoTodo> response) {
                if (response.body() != null) {
                    ToastUtil.showShortToastCenter(is_done ? "撤销成功" : "标记完成", context);
                    datasBean.setStatus(is_done ? 0 : 1);
                    adapter.removeItem(position);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<TodoTodo> call, Throwable t) {

            }
        });
    }

    //普通列表界面
    public static void collectArticle(Article.DataBean.DatasBean project, Context context, RecyclerView.Adapter adapter) {
        retrofit2.Call<Article> collectCall = api.collectArticle(project.getId());
        collectCall.enqueue(new Callback<Article>() {
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                ToastUtil.showShortToastCenter("已收藏", context);
                project.setCollect(true);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Article> call, Throwable t) {

            }
        });
    }

    //agentWeb
    public static void collectArticle(Article.DataBean.DatasBean project, Context context) {
        retrofit2.Call<Article> collectCall = api.collectArticle(project.getId());
        collectCall.enqueue(new Callback<Article>() {
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                ToastUtil.showShortToastCenter("已收藏", context);
            }

            @Override
            public void onFailure(Call<Article> call, Throwable t) {

            }
        });
    }

    //收藏界面 取消收藏
    //网络请求，list删除item
    public static void deleteArticle(int id, int position, Context context, MyselfAdapter adapter) {
        RetrofitApi api = MyApplication.retrofit.create(RetrofitApi.class);
        retrofit2.Call<Article> articleCancellCall = api.deleteArticle(id, -1);
        articleCancellCall.enqueue(new Callback<Article>() {
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                if (response.body() != null)
                    ToastUtil.showShortToastCenter(response.body().getErrorCode() == 0 ?
                            "已取消收藏" : response.body().getErrorMsg(), context);
                adapter.remove(position);
            }

            @Override
            public void onFailure(Call<Article> call, Throwable t) {

            }
        });
    }

    //普通列表界面
    public static void unCollectArticle(Article.DataBean.DatasBean project, Context context, RecyclerView.Adapter adapter) {
        retrofit2.Call<Article> cancelArticleCall = api.cancellPageArticle(project.getId());
        cancelArticleCall.enqueue(new Callback<Article>() {
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                ToastUtil.showShortToastCenter("已取消收藏", context);
                project.setCollect(false);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Article> call, Throwable t) {

            }
        });
    }

    //agentWeb
    public static void unCollectArticle(Article.DataBean.DatasBean project, Context context) {
        retrofit2.Call<Article> cancelArticleCall = api.cancellPageArticle(project.getId());
        cancelArticleCall.enqueue(new Callback<Article>() {
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                ToastUtil.showShortToastCenter("取消成功", context);
            }

            @Override
            public void onFailure(Call<Article> call, Throwable t) {

            }
        });
    }

    //开屏页
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
                Glide.with(context)
                        .load(fullUrl)
                        .transition(new DrawableTransitionOptions().crossFade())
                        .placeholder(R.mipmap.ic_launcher_foreground)
                        .into(imageView);
                totoDataInterface.transferDataBean(fullUrl);
            }

            @Override
            public void onFailure(retrofit2.Call<BiYing> call, Throwable t) {

            }
        });
    }

}
