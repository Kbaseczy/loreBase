package com.example.lorebase.ui.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lorebase.BaseActivity;
import com.example.lorebase.R;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.contain_const.UrlContainer;
import com.example.lorebase.ui.fragment.CollectFragment;
import com.example.lorebase.ui.fragment.HomeFragment;
import com.example.lorebase.ui.fragment.LoreTreeFragment;
import com.example.lorebase.ui.fragment.RelaxFragment;
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
import okhttp3.Call;
import okhttp3.Request;

/*
    ☆ Lambda 里面不能intent 定义，使用需要在外部定义，在里面用new Intent().setClass()  個別

    生命周期onResume(),持久化sharesPreference,

    ☆注销：1.点击logout 2.退出app（双击back按钮||点击ExitNow）
         1.1本地：会进行对用户信息清空操作（login_data文本清空） + 网络：请求服务器注销
         2.1本地：仅将登陆状态isLogin改为false（login_data中的isLogin修改） + 网络：请求服务器注销

    ☆对于注销后sign in / logout 图标变化情况：在界面内sign in 进如入LoginActivity登陆，
    然后再回到MainActivity进行刷新这2者的代码应在 onResume()中执行（详见onResume()处的注释）

    ☆自动登陆：获取login_data中字段isAutoLogin的值，由此判断是否登陆。如果为true,则在onCreate()中初始化数据并进行登陆
       涉及到登录状态isLogin变化，均需要刷新UI，  todo 与isLogin相关的有  sign in头布局/ logout / 收藏
      a.isLogin变为true  b.isAutoLogin的值由LoginActivity最初设置 c.登陆后应refreshSign()均在
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

        //根據自動登陸boolean去做登陸操作 ， 也是在二次及以後進入app所需要的。 初始值在LoginActivity中
        boolean isAuto = sp.getBoolean(ConstName.IS_AUTO_LOGIN,false);
        if(isAuto) autoLogin();

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
        login_username = navigationView.inflateHeaderView(R.layout.nav_header_main)
                .findViewById(R.id.login_username);
        navigationView.setNavigationItemSelectedListener(this);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.inflateMenu(R.menu.menu_activity_main);
        toolbar.setNavigationOnClickListener(v ->    //点击home按钮拉出侧滑栏
                drawerLayout.openDrawer(GravityCompat.START)
        );
        // 那个menu按钮在下面监听无效，需要在上面监听（TODO 原因待考察 - > need to setSupportActionbar(toolbar)）
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_search_main:
                    Intent search_action = new Intent(getBaseContext(), SearchActivity.class);
                    startActivity(search_action);
            }
            return false;
        });

        refreshSign();
        fab.setOnClickListener(v -> {
            Toast.makeText(this, "to top", Toast.LENGTH_SHORT).show();
            HomeFragment.project_recycler.scrollToPosition(0);
        });
    }

    @Override
    protected void onResume() {
        //这里存在典型的离开MainActivity -> LoginActivity （即离开一个activity前往另一个activity）
        // 需要在此方法（重新获取用户焦点），进行刷新UI操作（刷新sign in/logout的显示与隐藏）
        super.onResume();
        //在重新進入MainActivity時，刷新登陸/注銷圖標
        refreshSign();
    }

    private void refreshSign() {
        //界面内注銷后再登陸，此段代碼未執行  - - > 需要在生命周期onResume()中執行，回到視圖時刷新圖標
        SharedPreferences getLogin = getSharedPreferences(ConstName.LOGIN_DATA, MODE_PRIVATE);
        boolean isLogin = getLogin.getBoolean(ConstName.IS_LOGIN, false);
        String get_username = getLogin.getString(ConstName.USER_NAME, "");
        navigationView.getMenu().findItem(R.id.nav_logout).setVisible(isLogin);
        L.v(isLogin+"登陸狀態");
        //如果是登陸狀態(麽有點擊事件),文本設爲"用戶名".如果是未登錄狀態(有點擊事件),文本設爲"login".
        if (isLogin) {
            login_username.setText(get_username);
        } else {
            login_username.setText(R.string.login);
            login_username.setOnClickListener(v ->
                    startActivity(new Intent(getBaseContext(), LoginActivity.class)));
        }
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
                            logout(1);
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

    private void logout(int flag) {
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
                            //question:這個if判斷沒有執行下去 -> 字段打錯了 errCode -errorCode

                            if (new JSONObject(response).getInt("errorCode") == 0) {
                                //發送請求，獲得響應，為true則在服務器清除成功 --> 更新isLogin的值
                                editor = sp.edit();

                                if(flag == 1){
                                    //界面内點擊注銷 ， 清除用戶信息，無保留
                                    editor.clear();
                                }else{
                                    //未注銷，直接退出應用 ， 保留用戶名/密碼
                                    editor.putBoolean(ConstName.IS_LOGIN, false);//重新写入isLogin覆盖掉原来的值
                                }
                                editor.apply();
                                refreshSign();
                        }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void autoLogin() {
        String userName = sp.getString(ConstName.USER_NAME,"");
        String password = sp.getString(ConstName.PASS_WORD,"");
        String url = UrlContainer.baseUrl + UrlContainer.LOGIN;
        OkHttpUtils
                .post()
                .url(url)
                .addParams("username", userName)
                .addParams("password", password)
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
                        L.e(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getInt("errorCode") == 0) {
                                editor = sp.edit();
                                editor.putBoolean(ConstName.IS_LOGIN, true); //自動登陸后，登陸狀態改爲true
                                editor.apply(); //提交保存数据

                                refreshSign();  //自动登陆后刷新界面
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
        //退出程序應該自動注銷,登陸狀態改爲false
        logout(2);
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



