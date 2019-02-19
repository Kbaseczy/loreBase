package com.example.lorebase.ui.fragment.subFragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ajguan.library.EasyRefreshLayout;
import com.example.lorebase.R;
import com.example.lorebase.adapter.ProjectAdapter;
import com.example.lorebase.bean.Project;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.contain_const.UrlContainer;
import com.example.lorebase.util.DividerItemGridDecoration;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;
import java.util.Objects;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Request;

public class ItemProjectFragment extends Fragment {

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    private int id, page = 0;
    private View view;
    private List<Project.DataBean.DatasBean> beans_project;
    public static RecyclerView recyclerView;
    private EasyRefreshLayout easyRefreshLayout;
    private ProjectAdapter adapter;

    public ItemProjectFragment instance(int id) {
        ItemProjectFragment projectFragment = new ItemProjectFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ConstName.ID, id);
        projectFragment.setArguments(bundle);
        return projectFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            id = getArguments().getInt(ConstName.ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_itemproject_list, container, false);
        getProject();
        return view;
    }

    @Override
    public void onResume() {
        easyRefreshLayout = view.findViewById(R.id.easy_refresh_project);
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
                page = 0;
                getProject();
//                adapter.setBeanList_article(beanList_article);
                adapter.addBeanList_project(beans_project);
                easyRefreshLayout.refreshComplete();
            } else {
                page++;
                getProject();
//                adapter.setBeanList_article(beanList_article);
                adapter.addBeanList_project(beans_project);
                easyRefreshLayout.loadMoreComplete();
            }
        }, 500);
    }

    private void initProjectList() {
        recyclerView = view.findViewById(R.id.project_rv);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 1);
        adapter = new ProjectAdapter(getActivity(),beans_project);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemGridDecoration(Objects.requireNonNull(getActivity())));
    }

    private void getProject() {
        String url = UrlContainer.baseUrl + "project/list/" + page + "/json?cid=" + id;
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
                        Log.v("flipper ", response);
                        Gson gson = new Gson();
                        beans_project = gson.fromJson(response, Project.class).getData().getDatas();
                        initProjectList();
                    }
                });
    }
}
