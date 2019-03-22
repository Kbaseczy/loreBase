package com.example.lorebase;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.preference.PreferenceManager;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.example.lorebase.contain_const.UrlContainer;
import com.example.lorebase.greenDao.DaoMaster;
import com.example.lorebase.greenDao.DaoSession;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import skin.support.SkinCompatManager;
import skin.support.app.SkinCardViewInflater;
import skin.support.constraint.app.SkinConstraintViewInflater;
import skin.support.design.app.SkinMaterialViewInflater;

public class MyApplication extends Application {
    private static DaoSession daoSession;
    public static Retrofit retrofit;

    public static MyApplication getAppContext() {
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
        //Map SDK init.
        SDKInitializer.initialize(this);
        SDKInitializer.setCoordType(CoordType.BD09LL);

        okHttpCookie();
        initGreenDao();
        manageAlarm(); //定时通知

        SkinCompatManager.withoutActivity(this)                         // 基础控件换肤初始化
                .addInflater(new SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
                .addInflater(new SkinConstraintViewInflater())          // ConstraintLayout 控件换肤初始化[可选]
                .addInflater(new SkinCardViewInflater())                // CardView v7 控件换肤初始化[可选]
//                .setSkinStatusBarColorEnable(false)                     // 关闭状态栏换肤，默认打开[可选]
                .setSkinWindowBackgroundEnable(false)                   // 关闭windowBackground换肤，默认打开[可选]
                .loadSkin();

    }

    private void manageAlarm() {
        boolean isOpen = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext())
                .getBoolean("setting_switch", true);   //获取general文件中该key的值

        boolean nightMode = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext())
                .getBoolean("setting_switch_skin", true);   //获取general文件中该key的值
//        if (isOpen)
//            startService(new Intent(this, AlarmService.class));
//        else
//            stopService(new Intent(this, AlarmService.class));
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
        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15000L, TimeUnit.MILLISECONDS)
                .readTimeout(15000L, TimeUnit.MILLISECONDS)
                .cookieJar(cookieJar)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(UrlContainer.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }


}
