package com.example.lorebase.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.lorebase.contain_const.ConstName;

public class PreferencesUtil{

    public static void putIsLogin(Context context,boolean isLogin){
        SharedPreferences sp = context.getSharedPreferences(ConstName.LOGIN_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(ConstName.IS_LOGIN,isLogin);
        editor.apply();
    }

    public static boolean getIsLogin(Context context){
        SharedPreferences sp = context.getSharedPreferences(ConstName.LOGIN_DATA, Context.MODE_PRIVATE);
        return sp.getBoolean(ConstName.IS_LOGIN, false);
    }

    public static boolean getIsAuto(Context context){
        SharedPreferences sp = context.getSharedPreferences(ConstName.LOGIN_DATA, Context.MODE_PRIVATE);
        return sp.getBoolean(ConstName.IS_AUTO_LOGIN, false);
    }


}

