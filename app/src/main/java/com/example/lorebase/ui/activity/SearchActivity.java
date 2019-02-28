package com.example.lorebase.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.lorebase.BaseActivity;
import com.example.lorebase.MyApplication;
import com.example.lorebase.R;
import com.example.lorebase.bean.HotKey;
import com.example.lorebase.bean.SearchHistory;
import com.example.lorebase.contain_const.UrlContainer;
import com.example.lorebase.greenDao.SearchHistoryDao;
import com.example.lorebase.util.L;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import okhttp3.Call;
import okhttp3.Request;

/*
 *   key_word -> from:1.editText 2.hot_key 3.history_search
 *   sequence : key_word > onClick_search > goSearchListFrag >
 *   點擊事件觸發條件：1.搜索框輸入文本后，點擊搜索圖標  2.點擊熱搜標簽  3.點擊歷史搜索item
 *
 *   plan:search()方法考慮用傳參的形式傳遞key_word  --> have got it
 *
 *   难点：searchView 的使用，尤其是得理解去获取源代码中组件id，这里同样用到反射。在返回监听中判断
 *
 *   技术点：1.搜索框获取关键字(SearchView) 2.热搜搜索(TagFlowLayout)  3.历史搜索（GreenDao）
 *   小细节，悬浮按钮在searchActivity内部是不可见的，在SearchListFragment中可见  -> 直接setVisibility() 不需要flag
 *
 *   修复：历史搜索记录不及时变化显示->在onResume()方法中再运行历史记录的数据方法historyRecord()
 * */
public class SearchActivity extends BaseActivity {

    Toolbar toolbar_search;
    private List<HotKey.DataBean> hot_list = new ArrayList<>();
    private Context mContext;
    private SearchView.SearchAutoComplete autoComplete;
    private SearchView mSearchView;
    SearchHistoryDao searchHistoryDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE); //去除标题栏title
        setContentView(R.layout.activity_search);
        searchHistoryDao = MyApplication.getDaoSession().getSearchHistoryDao();
        getHot();
    }

    @SuppressLint("RestrictedApi")
    private void initView() {
        toolbar_search = findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar_search);

        toolbar_search.setTitle(R.string.action_search);
        toolbar_search.setNavigationOnClickListener(v -> {
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
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(R.animator.go_in, R.animator.go_out);
            }
        });

        //todo key_word-2熱搜 tagFlowLayout
        TagFlowLayout tag_flow_hot = findViewById(R.id.tag_flow_hot);
        TagAdapter<HotKey.DataBean> adapter_hot = new TagAdapter<HotKey.DataBean>(hot_list) {
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
                return hot;
            }
        };
        tag_flow_hot.setAdapter(adapter_hot);
        tag_flow_hot.setOnTagClickListener((view, position, parent) -> {
            String hot_word = hot_list.get(position).getName();
            //入库2  点击热搜tag，存入数据库 . 若存在则替换，不存在则插入。避免 UNIQUE重复的错误
            searchHistoryDao.insertOrReplace(new SearchHistory(null, hot_word));
            search(SearchActivity.this, hot_word);
            return true;
        });
//        historyRecord();
    }

    @Override
    protected void onResume() {
        super.onResume();
        historyRecord(); //解决历史搜索记录tag增加的变化
    }

    private void historyRecord() {
        //todo key_word-3.歷史搜索 ： greenDao
        //查詢所有数据得到list_history
        List<SearchHistory> list_history = searchHistoryDao.queryBuilder().list();
        TagFlowLayout tag_flow_history = findViewById(R.id.tag_flow_history);
        TagAdapter<SearchHistory> adapter_history = new TagAdapter<SearchHistory>(list_history) {
            @Override
            public View getView(FlowLayout parent, int position, SearchHistory searchHistory) {
                if (mContext == null) {
                    mContext = parent.getContext();
                }
                TextView history = (TextView) LayoutInflater.from(mContext)
                        .inflate(R.layout.tag_flow_tv, parent, false);
                history.setText(searchHistory.getKey_word());
                history.setTextColor(position % 2 == 0 ? Color.BLACK : Color.RED); //字體顔色
                history.setBackgroundResource(R.color.Grey200);
                return history;
            }
        };
        tag_flow_history.setAdapter(adapter_history);
        tag_flow_history.setOnTagClickListener((view, position, parent) -> {
            //点击历史搜索tag，触发搜索事件
            search(SearchActivity.this, list_history.get(position).getKey_word());
            return true;
        });

        TextView clear_all = findViewById(R.id.clear_history);
        clear_all.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.tip)
                    .setIcon(R.mipmap.ic_launcher_round)
                    .setMessage(R.string.tip_content_clear_history)
                    .setPositiveButton(R.string.ok, (dialog, which) -> {
                        searchHistoryDao.deleteAll();
                        list_history.clear();
                        visibility_history(list_history, clear_all);
                        adapter_history.notifyDataChanged();

                    }); //清空数据库
            builder.create().show(); //遗漏
        });
        visibility_history(list_history, clear_all);
    }

    private void visibility_history(List<SearchHistory> list_history, TextView clear_all) {
        clear_all.setVisibility(list_history.size() == 0 ? View.INVISIBLE : View.VISIBLE); // 设置clear可见性
        TextView historyTv = findViewById(R.id.history_tv);
        historyTv.setVisibility(list_history.size() == 0 ? View.INVISIBLE : View.VISIBLE);
    }

    //todo key_word-1.文本输入SearchView
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
                // 入库1  文本输入，存入数据库
                searchHistoryDao.insertOrReplace(new SearchHistory(null, query));
                search(SearchActivity.this, query);
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
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("RestrictedApi")
    private void search(Context context, String key_word) {
        //點擊事件觸發條件：1.搜索框輸入文本后，點擊搜索圖標  2.點擊熱搜標簽  3.點擊歷史搜索item
        //点击事件触发后：1.跳转searchListFragment 2.传递数据-搜索关键词 - 创建SearchListFragment实例，携带String参数
       /* toolbar_search.setTitle(key_word);
        fab.setVisibility(View.VISIBLE);
        SearchListActivity searchListFragment = new SearchListActivity().instance(key_word);
        //這裏通過在Frag中的實例化方法傳值，也可以如下面
        *//*Bundle bundle = new Bundle();
        bundle.putString(ConstName.KEY_WORD,key_word);
        searchListFragment.setArguments(bundle);*//*
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //add:点击一个热搜后，再点击没有效果   replace:无影响
        transaction.replace(R.id.coordinator_search, searchListFragment);
        transaction.commitAllowingStateLoss(); // good 你又忘了这一步，最后不跳转o(∩_∩)o*/

        //搜索结果页面改为activity,后期如果需要再优化界面
        SearchListActivity.actionStart(context, key_word);
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
                        initView();
                    }
                });
    }

}
