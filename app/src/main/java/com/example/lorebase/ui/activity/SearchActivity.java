package com.example.lorebase.ui.activity;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import okhttp3.Call;
import okhttp3.Request;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.example.lorebase.BaseActivity;
import com.example.lorebase.R;
import com.example.lorebase.bean.HotKey;
import com.example.lorebase.contain_const.UrlContainer;
import com.example.lorebase.ui.fragment.subFragment.SearchListFragment;
import com.example.lorebase.util.L;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

/*
 *   key_word -> from:1.editText 2.hot_key 3.history_search
 *   sequence : key_word > onClick_search > goSearchListFrag >
 *   點擊事件觸發條件：1.搜索框輸入文本后，點擊搜索圖標  2.點擊熱搜標簽  3.點擊歷史搜索item
 *
 *   plan:search()方法考慮用傳參的形式傳遞key_word
 * */
public class SearchActivity extends BaseActivity {

    Toolbar toolbar_search;
    private String key_word;
    private boolean isFab = false;
    private List<HotKey.DataBean> hot_list = new ArrayList<>();
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


//        toolbar_search.setOnMenuItemClickListener(item -> {
//            if(item .getItemId() == R.id.action_search){
//                Toast.makeText(SearchActivity.this, "Search action", Toast.LENGTH_SHORT).show();
//            }
//            return false;
//        });
        initView();
    }

    private void initView() {
        getHot();
        toolbar_search = findViewById(R.id.toolbar_search);
        toolbar_search.inflateMenu(R.menu.menu_search);
        toolbar_search.setNavigationOnClickListener(v ->
                startActivity(new Intent().setClass(SearchActivity.this, MainActivity.class)));
        FloatingActionButton fab = findViewById(R.id.fab_search);
        //key_word-2熱搜 tagFlowLayout
        TagFlowLayout tagFlowLayout = findViewById(R.id.tag_flow_hot);
        TagAdapter<HotKey.DataBean> adapter = new TagAdapter<HotKey.DataBean>(hot_list) {
            @Override
            public View getView(FlowLayout parent, int position, HotKey.DataBean dataBean) {
                if (mContext == null) {
                    mContext = parent.getContext();
                }
                TextView hot = (TextView) LayoutInflater.from(mContext)
                        .inflate(R.layout.tag_flow_tv, parent, false);
                hot.setText(dataBean.getName());
                hot.setOnClickListener(v -> {
                    key_word = dataBean.getName();
                    search(); //觸發條件2
                });
                return hot;
            }
        };
        tagFlowLayout.setAdapter(adapter);
        //key_word-3.歷史搜索 ： greenDao

        //key_word-1.搜索框獲取 ： searchView
        fab.setOnClickListener(v -> {
            //设置在搜索界面隐藏
            SearchListFragment.recyclerView.scrollToPosition(0);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_action);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.onActionViewExpanded();
        searchView.setQueryHint("search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setSubmitButtonEnabled(true);

        return super.onCreateOptionsMenu(menu);
    }

    //
    private void search() {
        //點擊事件觸發條件：1.搜索框輸入文本后，點擊搜索圖標  2.點擊熱搜標簽  3.點擊歷史搜索item
        //点击事件触发后：1.跳转searchListFragment 2.传递数据-搜索关键词 - 创建SearchListFragment实例，携带String参数
        toolbar_search.setTitle(key_word);
        SearchListFragment searchListFragment = new SearchListFragment().instance(key_word);
        //這裏通過在Frag中的實例化方法傳值，也可以如下面
        /*Bundle bundle = new Bundle();
        bundle.putString(ConstName.KEY_WORD,key_word);
        searchListFragment.setArguments(bundle);*/
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.search_content, searchListFragment);
    }

    private void getHot() {
        String url = UrlContainer.baseUrl + UrlContainer.HOT_KEYWORD;
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
                        L.v("search_hot_key " + response);

                        Gson gson = new Gson();
                        hot_list = gson.fromJson(response, HotKey.class).getData();
                        for (HotKey.DataBean hot : hot_list) {
                            Log.v("get_ArticleData", hot.getName());
                        }
                    }
                });
    }

}
