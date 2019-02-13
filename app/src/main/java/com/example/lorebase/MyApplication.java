package com.example.lorebase;

import android.app.Application;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.preference.PreferenceManager;

import com.bolex.autoEx.AutoEx;
import com.example.lorebase.greenDao.DaoMaster;
import com.example.lorebase.greenDao.DaoSession;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.MemoryCookieStore;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import static com.baidu.mapapi.BMapManager.getContext;

public class MyApplication extends Application {
    private static DaoSession daoSession;

    public static MyApplication instance() {
        return MyApplicationHolder.MY_APPLICATION;
    }

    private static class MyApplicationHolder {
        private static final MyApplication MY_APPLICATION = new MyApplication();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //解决android N（>=24）系统以上分享 路径为file://时的 android.os.FileUriExposedException异常
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        okHttpCookie();
        initGreenDao();
        AutoEx.apply(); // autoEx 异常堆栈

        boolean isOpen = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext())
                .getBoolean("example_switch", true);
        if (isOpen)
            startService(new Intent(this, AlarmService.class));//测试
    }

    private void initGreenDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "search_history.db");
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    private void okHttpCookie() {
        CookieJarImpl cookieJar1 = new CookieJarImpl(new MemoryCookieStore());
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15000L, TimeUnit.MILLISECONDS)
                .readTimeout(15000L, TimeUnit.MILLISECONDS)
                .addInterceptor(new LoggerInterceptor("jankin"))
                .cookieJar(cookieJar1)
                .hostnameVerifier((hostname, session) -> true)
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }
}
