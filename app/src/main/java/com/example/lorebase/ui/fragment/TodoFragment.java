package com.example.lorebase.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ajguan.library.EasyRefreshLayout;
import com.example.lorebase.MyApplication;
import com.example.lorebase.R;
import com.example.lorebase.adapter.TodoAdapter;
import com.example.lorebase.bean.TodoTodo;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.http.RetrofitApi;
import com.example.lorebase.util.L;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * todo 逻辑代码完成。   需要注意：界面UI item的动态变化情况，和之前的收藏类似  --> 收藏图标需要刷新后可更新
 * todo err： 请求数据时，始终提示 ”请登录“。
 */
public class TodoFragment extends Fragment {

    private Boolean is_done;
    private View view;
    private int page = 1;
    private List<TodoTodo.DataBean.DatasBean> list_todo ;
    public static RecyclerView recyclerView;
    private EasyRefreshLayout easyRefreshLayout;
    private TodoAdapter todoAdapter;

    public static TodoFragment getInstance(Boolean is_done) {
        TodoFragment todoFragment = new TodoFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ConstName.IS_DONE, is_done);
        todoFragment.setArguments(bundle);
        return todoFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            is_done = bundle.getBoolean(ConstName.IS_DONE);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_todo, container, false);
        getTodoList();
        return view;
    }

    private void initView() {
        recyclerView = view.findViewById(R.id.todo_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        todoAdapter = new TodoAdapter(getActivity(), list_todo, is_done);
        recyclerView.setAdapter(todoAdapter);
    }

    @Override
    public void onResume() {
        easyRefreshLayout = view.findViewById(R.id.easy_refresh_todo);
        easyRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                getDataList();
            }

            @Override
            public void onRefreshing() {
                getDataList();
            }
        });
        super.onResume();
    }

    private void getDataList() {
        new Handler().postDelayed(() -> {
            if (easyRefreshLayout.isRefreshing()) {
                page = 1;
                getTodoList();
                todoAdapter.addList_todo(list_todo);
                easyRefreshLayout.refreshComplete();
            } else {
                page++;
                getTodoList();
                todoAdapter.addList_todo(list_todo);
                easyRefreshLayout.loadMoreComplete();
            }
        }, 500);
    }

    private void getTodoList() {
        RetrofitApi api = MyApplication.retrofit.create(RetrofitApi.class);
        retrofit2.Call<TodoTodo> todoCall;
        if (is_done) {
            todoCall = api.getDoneTodoList(page);
        } else {
            todoCall = api.getUnTodoList(page);
        }

        todoCall.enqueue(new Callback<TodoTodo>() {
            @Override
            public void onResponse(retrofit2.Call<TodoTodo> call, Response<TodoTodo> response) {
                if (response.body() != null) {
                    list_todo = response.body().getData().getDatas();
                    L.v(list_todo.size()+ "  size");
                    initView();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<TodoTodo> call, Throwable t) {
                L.v("sdfasdf", t.getMessage() + "is there having.");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != view)
            view = null;
    }
}
