package com.example.lorebase.ui.activity;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import okhttp3.Call;
import okhttp3.Request;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/*
 *   key_word -> from:1.editText 2.hot_key 3.history_search
 *   sequence : key_word > onClick_search > goSearchListFrag >
 *   點擊事件觸發條件：1.搜索框輸入文本后，點擊搜索圖標  2.點擊熱搜標簽  3.點擊歷史搜索item
 *
 *   plan:search()方法考慮用傳參的形式傳遞key_word
 *
 *   难点：searchView 的使用，尤其是得理解去获取源代码中组件id，这里同样用到反射。在返回监听中地判断
 *
 *   技术点：1.搜索框获取关键字(SearchView) 2.热搜搜索(TagFlowLayout)  3.历史搜索（GreenDao）
 *   小细节，悬浮按钮在searchActivity内部是不可见的，在SearchListFragment中可见  -> 直接setVisibility() 不需要flag
 * */
public class SearchActivity extends BaseActivity {

    Toolbar toolbar_search;
    FloatingActionButton fab;
    private String key_word;
    private List<HotKey.DataBean> hot_list = new ArrayList<>();
    private Context mContext;
    private SearchView.SearchAutoComplete autoComplete;
    private SearchView mSearchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE); //去除标题栏title
        setContentView(R.layout.activity_search);
        getHot();
    }

    @SuppressLint("RestrictedApi")
    private void initView() {
        toolbar_search = findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar_search);
        toolbar_search.setTitle(R.string.action_search);
        toolbar_search.setNavigationOnClickListener(v ->{
            //根据搜索框的打开状态进行事件监听
                    if (autoComplete.isShown()) {
                        //搜索框打开，则清除文本内容，并关闭搜索框
                        try {
                            //如果搜索框中有文字，则会先清空文字，但网易云音乐是在点击返回键时直接关闭搜索框
                            autoComplete.setText("");
                            Method method = mSearchView.getClass().getDeclaredMethod("onCloseClicked");//反射
                            method.setAccessible(true);
                            method.invoke(mSearchView);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        //搜索框没打开，退出searchActivity 回到MainActivity。
                        //注意这里，直接结束当前活动，则自动回到上一活动   本app中仅MainActivity -> SearchActivity
                          //然后呢，一个activity中不同fragment进入，会返回到原点fragment
                        finish();
                    }
                }
                /*startActivity(new Intent().setClass(SearchActivity.this, MainActivity.class))*/);
        fab = findViewById(R.id.fab_search);
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
                hot.setTextColor(position % 2 == 0 ? Color.BLACK : Color.RED); //字體顔色
                hot.setBackgroundResource(R.color.Grey200);
//                L.v(dataBean.getName()+"key_word");
                hot.setOnClickListener(v -> {
                    L.v("key_word click");
                    key_word = dataBean.getName();
                    L.v(dataBean.getName() + "key_word click");
                    search(key_word); //觸發條件2
                });
                return hot;
            }
        };
        tagFlowLayout.setAdapter(adapter);
        //key_word-3.歷史搜索 ： greenDao

        fab.setOnClickListener(v -> {
            //设置在搜索界面隐藏
            SearchListFragment.recyclerView.scrollToPosition(0);
        });
        fab.setVisibility(View.GONE);
    }

    //SearchView
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem search_item = menu.findItem(R.id.search_action);
        mSearchView = (SearchView) MenuItemCompat.getActionView(search_item);
        mSearchView.setMaxWidth(1000);
        mSearchView.setQueryHint("input what you want");

//        mSearchView.setIconified(true);   //搜索框总是打开状态，默认为true
        mSearchView.onActionViewExpanded();//内部调用了setIconified(false);
        //key_word-1.搜索框獲取 ： searchView
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mSearchView.setSubmitButtonEnabled(true);
        //setIconifiedByDefault(false);会让放大镜icon直接在搜索框中出现
        //查看源码 寻得输入框id  SearchAutoComplete继承自 EditText
        autoComplete = mSearchView.findViewById(androidx.appcompat.R.id.search_src_text);
        autoComplete.getText();
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("RestrictedApi")
    private void search(String key_word) {
        //點擊事件觸發條件：1.搜索框輸入文本后，點擊搜索圖標  2.點擊熱搜標簽  3.點擊歷史搜索item
        //点击事件触发后：1.跳转searchListFragment 2.传递数据-搜索关键词 - 创建SearchListFragment实例，携带String参数
        toolbar_search.setTitle(key_word);
        fab.setVisibility(View.VISIBLE);
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
                            Log.v("hot_list111", hot.getName());
                        }

                        initView();
                    }
                });
    }

}
