package com.example.lorebase.contain_const;

import android.os.Environment;

import com.example.lorebase.util.TimeUtils;

import java.util.Date;

public class ConstName {
    public static final String TITLE = "title";
    public static final String ID = "id";
    public static final String IS_COLLECT = "is_collect";
    public static final String IS_OUT = "is_out";

    public static final String LOGIN_DATA = "login_data";
    public static final String IS_LOGIN = "isLogin";
    public static final String USER_NAME = "username";
    public static final String PASS_WORD = "password";
    public static final String IS_REMEMBER = "isRemember";
    public static final String IS_AUTO_LOGIN = "isAutoLogin";

    public static final String LORE_BASE = "LoreBase分享";

    //LoreTree
    public static final String CHAPTER_CID = "cid";
    public static final String OBJ = "obj";

    //treeActivity
    public static final String ACTIVITY = "activity";

    public static class activity {
        public static final int MAIN = 1;
        public static final int ABOUT_US = 2;
        public static final int LORE = 3;
        public static final int SEARCH = 4;
        public static final int SEARCH_LIST = 5;
        public static final int MYSELF = 6;
        public static final int BROWSE_HOSTORY = 7;
        public static final int PROJECT = 8;
        public static final int NAVIGATION = 9;
        public static final int AGENTWEB = 0;
    }

    public static final String KEY_WORD = "k";

    public static final int BANNER_HOME = 0;
    public static final int TAB_HOME = 1;
    public static final int FLIPPER_HOME = 2;
    public static final int ARTICLE_HOME = 3;

    public static final String IS_DONE = "is_done";
    public static final String TODO_BEAN = "todo_bean";
    public static final String TODO_BEAN_NAME = "todo_bean_name";

    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String COUNTRY = "country";
    public static final String PROVINCE = "province";
    public static final String CITY = "city";
    public static final String DISTRICT = "district";
    public static final String STREET = "street";



    public static final String IMAGE_PATH_PRE =
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/loreBaseImage/" ;
    public static final String IMAGE_NAME =  "loreBase"+
            TimeUtils.date2String(new Date(System.currentTimeMillis()),TimeUtils.geFormatYMD())+".jpg";
}
