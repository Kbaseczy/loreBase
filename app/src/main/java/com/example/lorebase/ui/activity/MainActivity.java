package com.example.lorebase.ui.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lorebase.R;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.contain_const.UrlContainer;
import com.example.lorebase.ui.fragment.CollectFragment;
import com.example.lorebase.ui.fragment.HomeFragment;
import com.example.lorebase.ui.fragment.RelaxFragment;
import com.example.lorebase.ui.fragment.LoreTreeFragment;
import com.example.lorebase.util.L;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.app.Fragment;
import okhttp3.Call;
import okhttp3.Request;

/*
    todo 再次说明：MainActivity只是一个整体结构（it's a container）-几乎没有逻辑，具体逻辑实现在Fragment中
    ☆ Lambda 里面不能intent 定义，使用需要在外部定义，在里面用new Intent().setClass()
 */
public class MainActivity extends Activity implements BottomNavigationView.OnNavigationItemSelectedListener
        , NavigationView.OnNavigationItemSelectedListener{
    RadioButton radioMain, radioSpace, radioMBase, radioKBase, radioSort;

    BottomNavigationView bottomNavigationView;
    FloatingActionButton fab;
    NavigationView navigationView;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    TextView login_username;

    HomeFragment homeFragment;
    LoreTreeFragment loreTreeFragment;
    RelaxFragment relaxFragment;
    CollectFragment collectFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        homeFragment = new HomeFragment();
        loreTreeFragment = new LoreTreeFragment();
        relaxFragment = new RelaxFragment();
        collectFragment = new CollectFragment();
        initView();
        //todo 设置进入后显示的第一个界面
        goFragment(homeFragment);
        indicateFrag();
        //拉伸，收起，隐藏 底部菜单栏
//        expandBottomSheet(bottomNavigationView);
//        hideBottomSheet(bottomNavigationView);
//        collapseBottomSheet(bottomNavigationView);
    }

    private void indicateFrag() {
        int fragId = getIntent().getIntExtra(ConstName.FRAGMENT,0);
        switch ( fragId){
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

        }
    }

    public void goFragment(Fragment fragment) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content_layout,fragment);
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

        //todo 获取登陆状态，设置Logout显示或隐藏    只有登陆成功后才会true，默认false
        SharedPreferences sp = getSharedPreferences(ConstName.LOGIN_DATA,MODE_PRIVATE);//获取sharedPreferences的对象并指向文件login_data
        navigationView.getMenu().findItem(R.id.nav_logout)
                .setVisible(sp.getBoolean(ConstName.IS_LOGIN,false));  //获取login_data 文件的isLogin 数据，然后设置logout可见性

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
        login_username.setOnClickListener(v -> startActivity(new Intent(getBaseContext(),LoginActivity.class)));

        SharedPreferences getLogin = getSharedPreferences(ConstName.LOGIN_DATA,MODE_PRIVATE);
        boolean isLogin = getLogin.getBoolean(ConstName.IS_LOGIN,false);
        String get_username = getLogin.getString(ConstName.USER_NAME,"");
        if(isLogin){
            login_username.setText(get_username);
            login_username.setFocusable(false);
        }else{
            login_username.setText(R.string.login);
            login_username.setFocusable(true);
        }

        fab.setOnClickListener(v->{
            Toast.makeText(this, "to top", Toast.LENGTH_SHORT).show();
                }
        );
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
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
                break;
            case R.id.action_lore_tree:
                toolbar.setTitle(R.string.tree);
                transaction.setCustomAnimations(
                        R.animator.fragment_slide_left_enter,
                        R.animator.fragment_slide_left_exit,
                        R.animator.fragment_slide_right_exit,
                        R.animator.fragment_slide_right_enter).
                        replace(R.id.content_layout, loreTreeFragment);
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

                break;
            case R.id.nav_setting:

                break;
            case R.id.nav_about_us:
                startActivity(new Intent(getBaseContext(),AboutUsActivity.class));
                break;
            case R.id.nav_logout:
                final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                View dialog_view = LayoutInflater.from(this).inflate(R.layout.logout_dialog,null);
                alertDialog.setView(dialog_view);
                LinearLayout ok = dialog_view.findViewById(R.id.logout_ok);
                LinearLayout cancel = dialog_view.findViewById(R.id.logout_cancel);
                ok.setOnClickListener(v-> {
                    logout();
                    alertDialog.dismiss();});
                /*
                dismiss/cancel  区别，当回调setOnCancelListener的监听事件就得用cancel，cancel方法中含有dismiss方法
                 */
                cancel.setOnClickListener(v-> alertDialog.dismiss());

                alertDialog.show();
                Toast.makeText(this, "test click logout", Toast.LENGTH_SHORT).show();
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

    private void logout(){
        String url = UrlContainer.baseUrl+UrlContainer.LOGOUT;
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
                        L.v(response+"logout");
                        try {
                            if(new JSONObject(response).getInt("errCode") == 0){
                                //發送請求，獲得響應，為true則在服務器清除成功 --> 更新isLogin的值
                                SharedPreferences.Editor sp = getSharedPreferences(ConstName.LOGIN_DATA,MODE_PRIVATE).edit();
                                sp.putBoolean(ConstName.IS_LOGIN,false);//重新写入isLogin覆盖掉原来的值
                                sp.apply();
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


//    public void InitView() {
//        radioMain = findViewById(R.id.radio0);
//        radioSort = findViewById(R.id.radio1);
//        radioKBase = findViewById(R.id.radio2);
//        radioMBase = findViewById(R.id.radio3);
//        radioSpace = findViewById(R.id.radio4);
//
//        radioMain.setOnClickListener(this);
//        radioSort.setOnClickListener(this);
//        radioKBase.setOnClickListener(this);
//        radioMBase.setOnClickListener(this);
//        radioSpace.setOnClickListener(this);
//
//    }

//    @Override
//    public void onClick(View view) {
//        transaction =manager.beginTransaction();
//        switch (view.getId()){
//            case R.id.radio0:
//                transaction.setCustomAnimations(
//                                R.animator.fragment_slide_left_enter,
//                                R.animator.fragment_slide_left_exit,
//                                R.animator.fragment_slide_right_exit,
//                                R.animator.fragment_slide_right_enter).
//                                replace(R.id.content_layout, new HomeFragment());
//
//                break;
//            case R.id.radio1:
//               transaction.setCustomAnimations(
//                                R.animator.fragment_slide_left_enter,
//                                R.animator.fragment_slide_left_exit,
//                                R.animator.fragment_slide_right_exit,
//                                R.animator.fragment_slide_right_enter).
//                                replace(R.id.content_layout, new LoreTreeFragment());
//                break;
//            case R.id.radio2:
//                transaction.setCustomAnimations(
//                                R.animator.fragment_slide_left_enter,
//                                R.animator.fragment_slide_left_exit,
//                                R.animator.fragment_slide_right_exit,
//                                R.animator.fragment_slide_right_enter).
//                                replace(R.id.content_layout, new RelaxFragment());
//                break;
//            case R.id.radio3:
//                transaction.setCustomAnimations(
//                                R.animator.fragment_slide_left_enter,
//                                R.animator.fragment_slide_left_exit,
//                                R.animator.fragment_slide_right_exit,
//                                R.animator.fragment_slide_right_enter).
//                                replace(R.id.content_layout, new CollectFragment());
//                break;
//
//        }
//        transaction.commit();
//    }


