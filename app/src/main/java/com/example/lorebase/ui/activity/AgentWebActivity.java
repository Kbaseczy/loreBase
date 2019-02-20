package com.example.lorebase.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.lorebase.BaseActivity;
import com.example.lorebase.R;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.http.CollectArticle;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.NestedScrollAgentWebView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

/*
    界面数据没有显示：因为用了CoordinatorLayout 将界面占据完了，没其余位置给展示数据。
    TODO 解决：去掉CoordinatorLayout，原来准备加“回到顶部功能”，也是不行的，这个需要ScrollView,而这里是直接获取的数据，没有布局效果。

    todo 注意，儅返回到loreActivity時，由於這一界面的數據來自LoreTreActivity,如果沒有數據則報錯
 */
public class AgentWebActivity extends BaseActivity {

    LinearLayout linearLayout;
    Toolbar toolbar;
    AgentWeb agentWeb;
    NestedScrollAgentWebView scrollAgentWebView;
    SharedPreferences sp;
    boolean isCollect = false;
    MenuItem menuItem;
    boolean is_collect;
    int flag_frag, article_id, flag_activity;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_web);

        title = getIntent().getStringExtra(ConstName.TITLE);
        flag_frag = getIntent().getIntExtra(ConstName.FRAGMENT, 1); //在微信frag,进入agent,返回按钮根据这个标志到对应Fragment
        article_id = getIntent().getIntExtra(ConstName.ID, 0);
        flag_activity = getIntent().getIntExtra(ConstName.ACTIVITY, 0);//獲取標志位-由哪個activity（界面）進入的
        is_collect = getIntent().getBooleanExtra(ConstName.IS_COLLECT, true);
        toolbar = findViewById(R.id.web_toolbar);
        toolbar.inflateMenu(R.menu.menu_agent_web);

        toolbar.setTitle(title);
        toolbar.setSubtitle(getIntent().getStringExtra(ConstName.PROJECT_AUTHOR));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }
        linearLayout = findViewById(R.id.web_parent);
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(linearLayout, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .setWebViewClient(new WebViewClient())
                .setWebChromeClient(new WebChromeClient())
                .createAgentWeb()
                .ready()
                .go(String.valueOf(getIntent().getData()));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (agentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_agent_web, menu);
        menuItem = menu.findItem(R.id.web_collect);
        if(is_collect)
            menuItem.setTitle(R.string.nav_my_uncollect);
        else
            menuItem.setTitle(R.string.nav_my_collect);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.web_collect:
                sp = getSharedPreferences(ConstName.LOGIN_DATA, MODE_PRIVATE);
                if (sp.getBoolean(ConstName.IS_LOGIN, false)) {
                    //收藏接口  ,  根据isCollect(默认是false),false则调用收藏，true则调用取消收藏

                    if (!is_collect) {  //疑惑，当二次进入时。如何区分不同文章，进行收藏、取消操作
                        CollectArticle.collectArticle(this, article_id);
                        isCollect = true;
                    } else {
                        CollectArticle.unCollect_originID(this, article_id);
                        isCollect = false;
                    }

                } else {
                    startActivity(new Intent(AgentWebActivity.this, LoginActivity.class));
                    Toast.makeText(this, "Please login.", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.web_share:
                Toast.makeText(this, "share action", Toast.LENGTH_SHORT).show();
                Intent intent_share = new Intent();
                intent_share.setAction(Intent.ACTION_SEND);
                intent_share.setType("text/plain");
//                    intent_share.putExtra(Intent.EXTRA_SUBJECT,ConstName.LORE_BASE+":"
//                            +getIntent().getStringExtra("title"));
                intent_share.putExtra(Intent.EXTRA_TEXT, title + ":" + String.valueOf(getIntent().getData()));
                startActivity(intent_share);
                break;

            case R.id.web_browser:
                Toast.makeText(this, "browser action", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse(String.valueOf(getIntent().getData()));
                intent.setData(uri);
                startActivity(intent);
                break;
            //toolbar 菜单键
            case android.R.id.home:
                //根據flag標志位判斷  返回到那個activity
                toolbarNavi();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //根據flag標志位判斷  返回到那個activity
    private void toolbarNavi() {
        Intent intent = new Intent();
        switch (flag_activity) {
            case ConstName.activity.MAIN:
                intent.setClass(AgentWebActivity.this, MainActivity.class)
                        .putExtra(ConstName.FRAGMENT, flag_frag);
                break;
            case ConstName.activity.ABOUT_US:
                intent.setClass(AgentWebActivity.this, AboutUsActivity.class);
                break;
            case ConstName.activity.LORE:
                intent.setClass(AgentWebActivity.this, LoreActivity.class); //这里会报错,需要持久化存储数据,选择LitePal
                break;
            case ConstName.activity.SEARCH:
                intent.setClass(AgentWebActivity.this, SearchActivity.class);
                break;
            case ConstName.activity.SEARCH_LIST:
                intent.setClass(AgentWebActivity.this, SearchListActivity.class);
                break;
            case ConstName.activity.MYSELF:
                intent.setClass(AgentWebActivity.this, MyselfActivity.class);
                break;
            case ConstName.activity.BROWSE_HOSTORY:
                intent.setClass(AgentWebActivity.this, BrowseHistoryActivity.class);
                break;
            case ConstName.activity.PROJECT:
                intent.setClass(AgentWebActivity.this, ProjectActivity.class);
                break;
            case ConstName.activity.NAVIGATION:
                intent.setClass(AgentWebActivity.this, NavigationActivity.class);
                break;

            default:
                intent.setClass(AgentWebActivity.this, MainActivity.class);
                break;
        }
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        agentWeb.getWebLifeCycle().onDestroy();
    }
}
