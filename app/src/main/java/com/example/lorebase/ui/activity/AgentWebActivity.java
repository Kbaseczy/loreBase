package com.example.lorebase.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.lorebase.BaseActivity;
import com.example.lorebase.R;
import com.example.lorebase.bean.Article;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.http.RetrofitUtil;
import com.example.lorebase.ui.fragment.ProjectFragment;
import com.example.lorebase.util.ActivityCollector;
import com.example.lorebase.util.PreferencesUtil;
import com.example.lorebase.util.TagFilter;
import com.example.lorebase.util.ToastUtil;
import com.just.agentweb.AgentWeb;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

/*
    界面数据没有显示：因为用了CoordinatorLayout 将界面占据完了，没其余位置给展示数据。
    TODO 解决：去掉CoordinatorLayout，原来准备加“回到顶部功能”，也是不行的，这个需要ScrollView,而这里是直接获取的数据，没有布局效果。

    todo 注意，儅返回到loreActivity時，由於這一界面的數據來自LoreTreActivity,如果沒有數據則報錯
      --> activity 状态在 onPause时期还存在，返回时状态自动恢复
 */
public class AgentWebActivity extends BaseActivity {

    LinearLayout linearLayout;
    Toolbar toolbar;
    AgentWeb agentWeb;
    boolean isCollect = false;
    MenuItem menuItem;
    boolean is_collect;
    int article_id, flag_activity;
    String author, url;
    String title;
    Article.DataBean.DatasBean article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_web);
        ActivityCollector.addActivtity(this);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            article = (Article.DataBean.DatasBean) bundle.get(ConstName.OBJ);
            if (article != null) {
                title = TagFilter.delHTMLTag(article.getTitle());
                article_id = article.getId();
                is_collect = article.isCollect();
                author = article.getAuthor();
                url = article.getLink();
            }
        }
        flag_activity = getIntent().getIntExtra(ConstName.ACTIVITY, 0);//獲取標志位-由哪個activity（界面）進入的
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.web_toolbar);
        toolbar.inflateMenu(R.menu.menu_agent_web);

        toolbar.setTitle(title);
        toolbar.setSubtitle(author);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icon_back);
        }

        linearLayout = findViewById(R.id.web_parent);
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(linearLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator()
                .setWebViewClient(new WebViewClient())
                .setWebChromeClient(new WebChromeClient())
                .createAgentWeb()
                .ready()
                .go(String.valueOf(url));
        FrameLayout frameLayout = agentWeb.getWebCreator().getWebParentLayout();
        frameLayout.setBackgroundColor(getColor(R.color.viewBackground));
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
        agentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        agentWeb.getWebLifeCycle().onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_agent_web, menu);
        menuItem = menu.findItem(R.id.web_collect);
        if (is_collect)
            menuItem.setTitle(R.string.nav_my_uncollect);
        else
            menuItem.setTitle(R.string.nav_my_collect);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.web_collect:
                if (PreferencesUtil.getIsLogin(this)) {
                    //收藏接口  ,  根据isCollect(默认是false),false则调用收藏，true则调用取消收藏

                    if (!is_collect) {
                        RetrofitUtil.collectArticle(article, this);
                        isCollect = true;
                    } else {
                        RetrofitUtil.unCollectArticle(article, this);
                        isCollect = false;
                    }
                } else {
                    startActivity(new Intent(AgentWebActivity.this, LoginActivity.class)
                            .putExtra(ConstName.ACTIVITY, ConstName.activity.AGENTWEB));
                    ToastUtil.showShortToastCenter("请登录", this);
                }
                break;

            case R.id.web_share:
                Intent intent_share = new Intent();
                intent_share.setAction(Intent.ACTION_SEND);
                intent_share.setType("text/plain");
//                    intent_share.putExtra(Intent.EXTRA_SUBJECT,ConstName.LORE_BASE+":"
//                            +getIntent().getStringExtra("title"));
                intent_share.putExtra(Intent.EXTRA_TEXT, title + ":" + String.valueOf(getIntent().getData()));
                startActivity(intent_share);
                break;

            case R.id.web_browser:
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
                intent.setClass(AgentWebActivity.this, MainActivity.class);
                break;
            case ConstName.activity.ABOUT_US:
                intent.setClass(AgentWebActivity.this, AboutUsActivity.class);
                break;
            case ConstName.activity.LORE:
                intent.setClass(AgentWebActivity.this, LoreActivity.class);
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
                intent.setClass(AgentWebActivity.this, ProjectFragment.class);
                break;
            case ConstName.activity.NAVIGATION:
                intent.setClass(AgentWebActivity.this, NavigationActivity.class);
                break;

            default:
                intent.setClass(AgentWebActivity.this, MainActivity.class);
                break;
        }
        startActivity(intent);
        overridePendingTransition(R.animator.go_in, R.animator.go_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        agentWeb.getWebLifeCycle().onDestroy();
    }
}
