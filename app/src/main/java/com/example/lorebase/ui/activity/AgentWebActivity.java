package com.example.lorebase.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.lorebase.BaseActivity;
import com.example.lorebase.R;
import com.example.lorebase.contain_const.ConstName;
import com.just.agentweb.AgentWeb;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_web);

        String title = getIntent().getStringExtra(ConstName.TITLE);
        int flag_activity = getIntent().getIntExtra(ConstName.ACTIVITY,1);//獲取標志位-由哪個activity（界面）進入的

        toolbar = findViewById(R.id.web_toolbar);
        toolbar.inflateMenu(R.menu.menu_agent_web);

        toolbar.setTitle(title);
        toolbar.setSubtitle(getIntent().getStringExtra(ConstName.PROJECT_AUTHOR));
        toolbar.setNavigationOnClickListener(v->{
            //根據flag標志位判斷  返回到那個activity
            Intent intent = new Intent();
            switch (flag_activity){
                case 1:
                    intent.setClass(AgentWebActivity.this,MainActivity.class);
                    break;
                case 2:
                    intent.setClass(AgentWebActivity.this,AboutUsActivity.class);
                    break;
                case 3:
                    intent.setClass(AgentWebActivity.this,LoreActivity.class); //这里会报错,需要持久化存储数据,选择LitePal
                    break;
                case 4:
                    intent.setClass(AgentWebActivity.this,SearchActivity.class);
                default:
                    intent.setClass(AgentWebActivity.this,MainActivity.class);
                    break;
            }
            startActivity(intent);
                }

        );
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.web_collect:
                    Toast.makeText(this, "collect action", Toast.LENGTH_SHORT).show();

                    break;

                case R.id.web_share:
                    Toast.makeText(this, "share action", Toast.LENGTH_SHORT).show();
                    Intent intent_share = new Intent();
                    intent_share.setAction(Intent.ACTION_SEND);
                    intent_share.setType("text/plain");
//                    intent_share.putExtra(Intent.EXTRA_SUBJECT,ConstName.LORE_BASE+":"
//                            +getIntent().getStringExtra("title"));

                    intent_share.putExtra(Intent.EXTRA_TEXT,title+":"+String.valueOf(getIntent().getData()));
                    startActivity(intent_share);
                    break;

                case R.id.web_browser:
                    Toast.makeText(this, "browser action", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri uri = Uri.parse(String.valueOf(getIntent().getData()));
                    intent.setData(uri);
                    startActivity(intent);
                    break;
            }
            return false;
        });

        linearLayout = findViewById(R.id.web_parent);
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(linearLayout, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .setWebViewClient(new WebViewClient())
                .setWebChromeClient(new WebChromeClient())
                .createAgentWeb()
                .ready()
                .go(String.valueOf(getIntent().getData()));

//        if (!agentWeb.back()){
//            finish();   //返回上一页
//        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (agentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        agentWeb.getWebLifeCycle().onDestroy();
    }
}
