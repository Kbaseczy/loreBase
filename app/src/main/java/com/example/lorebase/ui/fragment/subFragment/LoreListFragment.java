package com.example.lorebase.ui.fragment.subFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ajguan.library.EasyRefreshLayout;
import com.example.lorebase.R;
import com.example.lorebase.adapter.LoreListAdapter;
import com.example.lorebase.bean.Article;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.contain_const.UrlContainer;
import com.example.lorebase.util.DividerItemGridDecoration;
import com.example.lorebase.util.L;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Request;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class LoreListFragment extends Fragment {

    private View view;
    private List<Article.DataBean.DatasBean> datasBeanList;
    private int page = 0; //todo 上拉加载

    //实例化  -->todo 替代构造方法传递数据（在重新创建Fragment的时候，数据会丢失），
    //          todo 用setArguments(bundle)传递数据更安全。
    public LoreListFragment instantiate(int cid) {
        //TODO The step is the most important!
        //TODO->which to transfer the chapterID for getting the data about recyclerView .
        LoreListFragment instance_frag = new LoreListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ConstName.CHAPTER_CID, cid);
        instance_frag.setArguments(bundle);
        return instance_frag;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lore_list, container, false);
        assert getArguments() != null;
        int chapterId = getArguments().getInt(ConstName.CHAPTER_CID);
        //（loreActivity中添加Fragment对象时传递了chapterID）从fragment实例获取chapterID
        getLore(page, chapterId);
        return view;
    }

    private void initRecycler() {
        RecyclerView recyclerView = view.findViewById(R.id.lore_rv);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 1);

        recyclerView.setLayoutManager(manager);
        LoreListAdapter adapter = new LoreListAdapter(datasBeanList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemGridDecoration(Objects.requireNonNull(getContext())));

        EasyRefreshLayout easyRefreshLayout = view.findViewById(R.id.easy_refresh_lore);
        easyRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {

            }

            @Override
            public void onRefreshing() {

            }
        });
    }

    private void getLore(int page, int chapterID) {
        String url = UrlContainer.baseUrl + "article/list/" + page + "/json?cid=" + chapterID;
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
                        L.v("LoreListFragment " + response);

//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            Gson gson = new Gson();
//                            Type type = new TypeToken<List<Article>>(){}.getType();
//                            datasBeanList = gson.fromJson(jsonObject.getJSONArray("data").toString(),type);
//
//                            for(Article.DataBean.DatasBean article : datasBeanList){
//                                Log.v("get_ArticleData",article.getTitle());
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }

                        //TODO :Expected Object but Array -> ok
                        // todo 请求服务器正常，并获取响应。   数据解析存储ok
                        Gson gson = new Gson();
                        datasBeanList = gson.fromJson(response, Article.class).getData().getDatas();
                        initRecycler();
                    }
                });
    }
}
