package com.example.lorebase.ui.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lorebase.BaseActivity;
import com.example.lorebase.R;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.contain_const.UrlContainer;
import com.example.lorebase.ui.fragment.CollectFragment;
import com.example.lorebase.ui.fragment.HomeFragment;
import com.example.lorebase.ui.fragment.RelaxFragment;
import com.example.lorebase.ui.fragment.LoreTreeFragment;
import com.example.lorebase.ui.fragment.subFragment.LocationFragment;
import com.example.lorebase.util.ActivityCollector;
import com.example.lorebase.util.L;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Fragment;

import okhttp3.Call;
import okhttp3.Request;

/*
    ☆ Lambda 里面不能intent 定义，使用需要在外部定义，在里面用new Intent().setClass()
 */
public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener
        , NavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    FloatingActionButton fab;
    NavigationView navigationView;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    TextView login_username;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    HomeFragment homeFragment;
    LoreTreeFragment loreTreeFragment;
    RelaxFragment relaxFragment;
    CollectFragment collectFragment;
    LocationFragment locationFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        homeFragment = new HomeFragment();
        loreTreeFragment = new LoreTreeFragment();
        relaxFragment = new RelaxFragment();
        collectFragment = new CollectFragment();
        locationFragment = new LocationFragment();
        sp = getSharedPreferences(ConstName.LOGIN_DATA, MODE_PRIVATE);
        initView();
        //todo 设置进入后显示的第一个界面
        goFragment(homeFragment);
        indicateFrag();
        //拉伸，收起，隐藏 底部菜单栏
//        expandBottomSheet(bottomNavigationView);
//        hideBottomSheet(bottomNavigationView);
//        collapseBottomSheet(bottomNavigationView);
    }

    //返回MainActivity指定顯示的fragment,類似的用法在agentWeb也有體現.
    private void indicateFrag() {
        int fragId = getIntent().getIntExtra(ConstName.FRAGMENT, 1);
        switch (fragId) {
            case 1:
                goFragment(homeFragment);
                break;
            case 2:
                goFragment(loreTreeFragment);
                break;
            case 3:
                goFragment(relaxFragment);
                break;
            case 4:
                goFragment(collectFragment);
                break;
            default:
                goFragment(homeFragment);
                break;
        }
    }

    public void goFragment(Fragment fragment) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content_layout, fragment);
        transaction.commitAllowingStateLoss();
    }

    private void initView() {
        drawerLayout = findViewById(R.id.drawer_layout);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fab = findViewById(R.id.btn_fab);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        navigationView = findViewById(R.id.nav_view);
        //加载头布局文件中的组件
        login_username = navigationView.inflateHeaderView(R.layout.nav_header_main).findViewById(R.id.login_username);
        navigationView.setNavigationItemSelectedListener(this);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.inflateMenu(R.menu.menu_activity_main);
        toolbar.setNavigationOnClickListener(v ->    //点击home按钮拉出侧滑栏
                drawerLayout.openDrawer(GravityCompat.START)
        );
        // 那个menu按钮在下面监听无效，需要在上面监听（TODO 原因待考察 - > ?）
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_search_main:
                    Intent search_action = new Intent(getBaseContext(), SearchActivity.class);
                    startActivity(search_action);
            }
            return false;
        });

        SharedPreferences getLogin = getSharedPreferences(ConstName.LOGIN_DATA, MODE_PRIVATE);
        boolean isLogin = getLogin.getBoolean(ConstName.IS_LOGIN, false);
        String get_username = getLogin.getString(ConstName.USER_NAME, "");
        L.v("username", get_username + "|isLogin|" + isLogin + " ");

        //todo 获取登陆状态，设置Logout显示或隐藏    只有登陆成功后才会true，默认false
        //获取login_data 文件的isLogin 数据，设置logout可见性
        navigationView.getMenu().findItem(R.id.nav_logout).setVisible(isLogin);

        //如果是登陸狀態(麽有點擊事件),文本設爲"用戶名".如果是未登錄狀態(有點擊事件),文本設爲"login".
        //todo isLogin值不變化
        if (isLogin) {
            login_username.setText(get_username);
        } else {
            login_username.setText(R.string.login);
            login_username.setOnClickListener(v ->
                    startActivity(new Intent(getBaseContext(), LoginActivity.class)));
        }
        fab.setOnClickListener(v -> {
            Toast.makeText(this, "to top", Toast.LENGTH_SHORT).show();
            HomeFragment.project_recycler.scrollToPosition(0);
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        switch (menuItem.getItemId()) {
            case R.id.action_home:
                toolbar.setTitle(R.string.app_name);
                transaction.setCustomAnimations(
                        R.animator.fragment_slide_left_enter,
                        R.animator.fragment_slide_left_exit,
                        R.animator.fragment_slide_right_exit,
                        R.animator.fragment_slide_right_enter).
                        replace(R.id.content_layout, homeFragment);
                fab.setOnClickListener(v -> {
                            //bug:上拉加载后，最顶部item变化。
                            Toast.makeText(this, "to top", Toast.LENGTH_SHORT).show();
                            HomeFragment.project_recycler.scrollToPosition(0);
                        }
                );
                break;
            case R.id.action_lore_tree:
                toolbar.setTitle(R.string.tree);
                transaction.setCustomAnimations(
                        R.animator.fragment_slide_left_enter,
                        R.animator.fragment_slide_left_exit,
                        R.animator.fragment_slide_right_exit,
                        R.animator.fragment_slide_right_enter).
                        replace(R.id.content_layout, loreTreeFragment);
                fab.setOnClickListener(v ->
                        LoreTreeFragment.recyclerView_loreTree.scrollToPosition(0)
                );
                break;
            case R.id.action_relax:
                toolbar.setTitle(R.string.relax);
                transaction.setCustomAnimations(
                        R.animator.fragment_slide_left_enter,
                        R.animator.fragment_slide_left_exit,
                        R.animator.fragment_slide_right_exit,
                        R.animator.fragment_slide_right_enter).
                        replace(R.id.content_layout, relaxFragment);
                break;
            case R.id.action_mbase:
                toolbar.setTitle(R.string.collect);
                transaction.setCustomAnimations(
                        R.animator.fragment_slide_left_enter,
                        R.animator.fragment_slide_left_exit,
                        R.animator.fragment_slide_right_exit,
                        R.animator.fragment_slide_right_enter).
                        replace(R.id.content_layout, collectFragment);
                break;

            //TODO 侧滑栏navigationView 监听
            case R.id.nav_collect:
                Toast.makeText(this, "test click collect", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_todo:

                break;
            case R.id.nav_position:
                transaction.setCustomAnimations(
                        R.animator.fragment_slide_left_enter,
                        R.animator.fragment_slide_left_exit,
                        R.animator.fragment_slide_right_exit,
                        R.animator.fragment_slide_right_enter).
                        replace(R.id.content_layout, locationFragment);
                break;
            case R.id.nav_setting:
                Toast.makeText(this, "test click setting", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_about_us:
                startActivity(new Intent(getBaseContext(), AboutUsActivity.class));
                break;
            case R.id.nav_logout:
                //自定義佈局
//                final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
//                View dialog_view = LayoutInflater.from(this).inflate(R.layout.logout_dialog,null);
//                alertDialog.setView(dialog_view);
//                LinearLayout ok = dialog_view.findViewById(R.id.logout_ok);
//                LinearLayout cancel = dialog_view.findViewById(R.id.logout_cancel);
//                ok.setOnClickListener(v-> {
//                    logout();
//                    alertDialog.dismiss();});
//                /*
//                dismiss/cancel  区别，当回调setOnCancelListener的监听事件就得用cancel，cancel方法中含有dismiss方法
//                 */
//                cancel.setOnClickListener(v-> alertDialog.dismiss());

                //默認佈局
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle(R.string.tip)
                        .setIcon(R.mipmap.ic_launcher_round)
                        .setMessage(R.string.tip_content_logout)
                        .setPositiveButton(R.string.ok, (dialog, which) -> {
                            logout();
                            Toast.makeText(this, "Have logout", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(R.string.cancel, (dialog, which) ->
                                dialog.dismiss());
                alertDialog.create().show();
                Toast.makeText(this, "test click logout", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_exit:
                //todo 需要添加管理activity的類，統一關閉所有activity
                ActivityCollector.finishAll();
                finish();
                break;
        }
        transaction.commitAllowingStateLoss();
        return false;
    }

    private long exit_time;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }

            if (System.currentTimeMillis() - exit_time < 2000) {
                finish();
            } else {
                exit_time = System.currentTimeMillis();
                Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void logout() {
        String url = UrlContainer.baseUrl + UrlContainer.LOGOUT;
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        L.v("onErr");
                    }

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        L.v("onBefore");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        L.v(response + "logout");
                        try {
                            //todo question:這個if判斷沒有執行下去
                            if (new JSONObject(response).getInt("errCode") == 0) {
                                //發送請求，獲得響應，為true則在服務器清除成功 --> 更新isLogin的值
                                Boolean isLogin2 = sp.getBoolean(ConstName.IS_LOGIN, false);
                                L.v("null?" + isLogin2 + " test1 ");
                                L.v("if  null null 111111");
                                editor = sp.edit();
                                editor.putBoolean(ConstName.IS_LOGIN, false);//重新写入isLogin覆盖掉原来的值
//                                editor.clear();
                                editor.apply();
                                //todo 這裏無法進行鍵值對修改
                                L.v(isLogin2 + "test2 ");
//                                navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);//注销后，“注销按钮”不可见
//                                initView();  //boolean值刷新后，重新加载界面
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        homeFragment = null;
        loreTreeFragment = null;
        relaxFragment = null;
        collectFragment = null;
        locationFragment = null;
    }
}

//actionBar的处理方式，ToolBar的处理直接用ToolBar.链式
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_activity_main,menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case android.R.id.home:
//                drawerLayout.openDrawer(GravityCompat.START);
//            case R.id.action_search_main:
//                Intent search_action = new Intent(this,SearchActivity.class);
//                startActivity(search_action);
//        }
//        return super.onOptionsItemSelected(item);
//    }



