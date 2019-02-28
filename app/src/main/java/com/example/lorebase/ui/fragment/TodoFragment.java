package com.example.lorebase.ui.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.example.lorebase.R;
import com.example.lorebase.adapter.TodoAdapter;
import com.example.lorebase.bean.TodoTodo;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.contain_const.UrlContainer;
import com.example.lorebase.util.L;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 * todo 逻辑代码完成。   需要注意：界面UI item的动态变化情况，和之前的收藏类似  --> 收藏图标需要刷新后可更新
 * todo err： 请求数据时，始终提示 ”请登录“。
 */
public class TodoFragment extends Fragment {

    private Boolean is_done;
    private View view;
    private int page = 1;
    private List<TodoTodo.DataBean.DatasBean> list_todo;
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
        // Inflate the layout for this fragment
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

        L.v("TodoFragment", is_done ? "Todo" : "complete");
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
        String uri_todo = UrlContainer.TODO_TODO + page + "/json?status=0";
        String uri_complete = UrlContainer.TODO_COMPLETE + page + "/json?status=1&orderby=2";
        OkHttpUtils
                .get()
                .url(is_done ? uri_complete : uri_todo)
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
                        L.d("TAGTODO", response);
                        try {
                            if (new JSONObject(response).getInt("errorCode") == 0) {
                                Gson gson = new Gson();
                                list_todo = gson.fromJson(response, TodoTodo.class).getData().getDatas();
                                initView();
                            } else {
                                L.e("TAGTODO", new JSONObject(response).getString("errorMsg") + "  有没有");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
