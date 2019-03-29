package com.example.lorebase.util;

import android.content.Context;
import android.util.Log;

import java.io.UnsupportedEncodingException;

public class HttpUtil {
    public static int TEST_FLAG = 1;
//  public static int TEST_FLAG = 0;
    /**
     * 仅仅用来判断是否有网络连接
     */
    public static boolean isNetworkAvailable(Context context) {
        if (TEST_FLAG == 0)
        {
            return true;
        } else {
            return NetWorkHelper.isNetworkAvailable(context);
        }
    }

    /**
     * 判断是否有可用的网络连接
     * */
    public static boolean isNetworkConnected(Context context) {
        if (TEST_FLAG == 0)
        {
            return true;
        } else {
            return NetWorkHelper.isNetworkConnected(context);
        }
    }

    /**
     * 判断mobile网络是否可用
     */
    public static boolean isMobileDataEnable(Context context) {
        String TAG = "httpUtils.isMobileDataEnable()";
        try {
            return NetWorkHelper.isMobileDataEnable(context);
        } catch (Exception e) {
            L.e(TAG, e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 判断wifi网络是否可用
     */
    public static boolean isWifiDataEnable(Context context) {
        String TAG = "httpUtils.isWifiDataEnable()";
        try {
            return NetWorkHelper.isWifiDataEnable(context);
        } catch (Exception e) {
            L.e(TAG, e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 判断是否为漫游
     */
    public static boolean isNetworkRoaming(Context context) {
        return NetWorkHelper.isNetworkRoaming(context);
    }
}
