package com.example.lorebase.ui.fragment.subFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Request;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lorebase.R;
import com.example.lorebase.adapter.LoreListAdapter;
import com.example.lorebase.bean.Article;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.contain_const.UrlContainer;
import com.example.lorebase.util.L;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class LoreListFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Article.DataBean.DatasBean> datasBeanList ;

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
        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_lore_list, container, false);
//        getLore();
        assert getArguments() != null;
        getLore(getArguments().getInt(ConstName.CHAPTER_CID));
        return recyclerView;
    }

    private void getLore(int chapterID ){

        int page = 0; //todo 上拉加载
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
                        L.v("LoreListFragment "+response);
                        Gson gson = new Gson();
                        //TODO :Expected Object but Array -> ok
                        datasBeanList = gson.fromJson(response, Article.DataBean.class).getDatas();
                        L.v("getArticleTitle"+datasBeanList.get(2).getTitle());
//                        for(Article.DataBean.DatasBean article : datasBeanList){
//                            Log.v("get_ArticleData",article.getTitle());
//                        }
                    }
                });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.setAdapter(new LoreListAdapter(datasBeanList));
    }


}
