package com.example.lorebase.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lorebase.BaseActivity;
import com.example.lorebase.MapService;
import com.example.lorebase.MyApplication;
import com.example.lorebase.R;
import com.example.lorebase.bean.User;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.http.RetrofitApi;
import com.example.lorebase.recog.ActivityUiDialog;
import com.example.lorebase.ui.fragment.HomeFragment;
import com.example.lorebase.ui.fragment.LoreTreeFragment;
import com.example.lorebase.ui.fragment.ProjectFragment;
import com.example.lorebase.ui.fragment.WeChatFragment;
import com.example.lorebase.ui.fragment.subFragment.WeChatArticleFragment;
import com.example.lorebase.util.ActivityCollector;
import com.example.lorebase.util.L;
import com.example.lorebase.util.PreferencesUtil;
import com.example.lorebase.util.ToastUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Callback;
import retrofit2.Response;

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

      todo viewPager.setCurrentItem(position) 解决了底部导航图标状态不变化
      todo change: Fragment容器由 container 换为 viewPager ，因此之前报错不能改变片段容器的ID，即是container与viewPager冲突了
                                                                    -> 将container 在布局文件中去掉即可
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
    ViewPager viewPager;

    HomeFragment homeFragment;
    LoreTreeFragment loreTreeFragment;
    ProjectFragment projectFragment;
    WeChatFragment weChatFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCollector.addActivtity(this);

        homeFragment = HomeFragment.getInstance();
        loreTreeFragment = LoreTreeFragment.getInstance();
        projectFragment = ProjectFragment.getInstance();
        weChatFragment = WeChatFragment.getInstance();

        initView();

        checkPermission();
        startService(new Intent(this, MapService.class));

        L.v("onCreateMain");
    }

    // todo ：activity只要不走onCreate方法，状态就不会被刷新

    private void initView() {
        drawerLayout = findViewById(R.id.drawer_layout);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fab = findViewById(R.id.btn_fab);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        //加载头布局文件中的组件
        login_username = navigationView.inflateHeaderView(R.layout.nav_header_main)
                .findViewById(R.id.login_username);
//        nav_header_portrait = navigationView.inflateHeaderView(R.layout.nav_header_main)
//                .findViewById(R.id.nav_header_portrait);//导致2个头布局
        /*---------------------------------------------------------------------*/
        initViewpager();
        /*---------------------------------------------------------------------*/
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView.setLayoutMode(BottomNavigationView.MEASURED_HEIGHT_STATE_SHIFT); //可在配置在布局文件中
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
    }

    private void initViewpager() {
        viewPager = findViewById(R.id.viewpager_main);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        List<Fragment> list = new ArrayList<>();
        list.add(homeFragment);
        list.add(loreTreeFragment);
        list.add(projectFragment);
        list.add(weChatFragment);
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            // FragmentStatePagerAdapter/FragmentPagerAdapter 注意区别
            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }

            @Override
            public int getCount() {
                return list.size();
            }
        };

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
    }

    @Override
    protected void onResume() {
        //这里存在典型的离开MainActivity -> LoginActivity （即离开一个activity前往另一个activity）
        // 需要在此方法（重新获取用户焦点），进行刷新UI操作（刷新sign in/logout的显示与隐藏）
        super.onResume();
        //在重新進入MainActivity時，刷新登陸/注銷圖標
        refreshSign();
        L.v("onResumeMain");
    }

    @Override
    protected void onPause() {
        super.onPause();
        L.v("onPauseMain");
    }

    @Override
    protected void onStop() {
        super.onStop();
        L.v("onStopMain");
    }

    private void refreshSign() {
        //界面内注銷后再登陸，此段代碼未執行  - - > 需要在生命周期onResume()中執行，回到視圖時刷新圖標
        sp = getSharedPreferences(ConstName.LOGIN_DATA, MODE_PRIVATE);
        boolean isLogin = PreferencesUtil.getIsLogin(this);
        boolean isAuto = PreferencesUtil.getIsAuto(this);
        if (isAuto && !isLogin) autoLogin(sp);//自动登陆，当登陆cookie失效时执行

//        String get_username = sp.getString(ConstName.USER_NAME, "");
        String get_username = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext())
                .getString("username", "");
//        navigationView.getMenu().findItem(R.id.nav_logout).setVisible(isLogin);
        navigationView.getMenu().findItem(R.id.nav_collect).setVisible(isLogin);
        navigationView.getMenu().findItem(R.id.nav_todo).setVisible(isLogin);
        L.v(isLogin + "登陸狀態");
        L.v(isAuto + " 自动登陆");
        //如果是登陸狀態(麽有點擊事件),文本設爲"用戶名".如果是未登錄狀態(有點擊事件),文本設爲"login".
        if (isLogin) {
            login_username.setText(get_username);
            //不进行跳转，貌似解决了登录状态用户名可点击
            login_username.setClickable(false);
//            login_username.setOnClickListener(v -> new Intent(MainActivity.this, MyselfActivity.class));
        } else {
            login_username.setText(R.string.login);
            login_username.setOnClickListener(v ->
                    startActivity(new Intent(this, LoginActivity.class)));
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_home:
                viewPager.setCurrentItem(0);
                fab.setOnClickListener(v -> HomeFragment.recyclerView.scrollToPosition(0));
                break;
            case R.id.action_lore_tree:
                viewPager.setCurrentItem(1);
                fab.setOnClickListener(v ->
                        LoreTreeFragment.recyclerView_loreTree.scrollToPosition(0));
                break;
            case R.id.action_project:
                viewPager.setCurrentItem(2);
                break;
            case R.id.action_we_chat:
                viewPager.setCurrentItem(3);
                fab.setOnClickListener(v ->
                        WeChatArticleFragment.nestedScrollView.post(() -> WeChatArticleFragment.nestedScrollView.fullScroll(View.FOCUS_UP)));
                break;

            //TODO 侧滑栏navigationView 监听
            case R.id.nav_collect:
                startActivity(new Intent(MainActivity.this, MyselfActivity.class));
                overridePendingTransition(R.animator.go_in, R.animator.go_out);
                break;
            case R.id.nav_todo:
                startActivity(new Intent(this, TODOActivity.class));
                overridePendingTransition(R.animator.go_in, R.animator.go_out);
                break;
            case R.id.nav_position:
                startActivity(new Intent(this, LocationActivity.class));
                overridePendingTransition(R.animator.go_in, R.animator.go_out);
                break;
            case R.id.nav_browser:
                startActivity(new Intent(this, BrowseHistoryActivity.class));
                overridePendingTransition(R.animator.go_in, R.animator.go_out);
                break;
            case R.id.nav_speech:
                startActivity(new Intent(this, ActivityUiDialog.class));
                overridePendingTransition(R.animator.go_in, R.animator.go_out);
                break;
            case R.id.nav_setting:
                startActivity(new Intent(this, MySettingActivity.class));
                overridePendingTransition(R.animator.go_in, R.animator.go_out);
                finish();//main进入设置界面时已经设置销毁，重新进入main强制走OnCreate方法-实现日夜间模式切换
                //note: 注意数据的恢复  onSavedBundles
                break;
        }
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
                ActivityCollector.finishAll();
            } else {
                exit_time = System.currentTimeMillis();
                ToastUtil.showLongToastCenter("再次返回退出", this);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void autoLogin(SharedPreferences sp) {

        String userName = sp.getString(ConstName.USER_NAME, "");
        String password = sp.getString(ConstName.PASS_WORD, "");
        RetrofitApi api = MyApplication.retrofit.create(RetrofitApi.class);
        retrofit2.Call<User> loginCall = api.login(userName, password);
        loginCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(retrofit2.Call<User> call, Response<User> response) {
                if (response.body() != null) {
                    if (response.body().getErrorCode() == 0) {
//                        editor = sp.edit();
//                        editor.putBoolean(ConstName.IS_LOGIN, true); //自動登陸后，登陸狀態改爲true
//                        editor.apply(); //提交保存数据
                        PreferencesUtil.putIsLogin(MainActivity.this,true);
                        refreshSign();  //自动登陆后刷新界面
                        ToastUtil.showShortToastTop("已登陆", MainActivity.this);
                    } else {
                        Toast.makeText(MainActivity.this, response.body().getErrorMsg(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<User> call, Throwable t) {
                Log.v("sdfasdf", t.getMessage() + "  yhujg");
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        homeFragment = null;
        loreTreeFragment = null;
        projectFragment = null;
        weChatFragment = null;
        //退出程序應該自動注銷,登陸狀態改爲false
        stopService(new Intent(this, MapService.class));
        L.v("onDestroyMain");
    }

    //actionBar的处理方式，ToolBar的处理直接用ToolBar.链式
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.action_search_main:
                Intent search_action = new Intent(this, SearchActivity.class);
                startActivity(search_action);
                overridePendingTransition(R.animator.go_in, R.animator.go_out);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            ToastUtil.showShortToast("必须同意所有权限才能使用该程序", this);
                            finish();
                            return;
                        }
                    }
                } else {
                    ToastUtil.showShortToast("未知错误", this);
                    finish();
                }
        }
    }

    private void checkPermission() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[0]); //todo 将permissionList转为Array
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
    }
}



