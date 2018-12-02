package com.example.lorebase.contain_const;

/**
 * Api接口地址
 */
public class UrlContainer {
    public static final String baseUrl = "http://wanandroid.com/";
    /**
     * 登录  ok
     */
    public static final String LOGIN = "user/login";

    /**
     * 登出  ok
     */
    public static final String LOGOUT= "user/logout/json";
    /**
     * 注册  ok
     */
    public static final String REGISTER = "user/register";

    /**
     * 首页文章列表  ok
     */
    public static final String HOME_LIST = "article/list/{page}/json";

    /*
        微信公众号chapter   ok
     */
    public static final String WX_ARTICLE_CHAPTER = "wxarticle/chapters/json";

    /*
        微信公众号文章  ok
     */
    public static final String WX_ARTICLE_LIST = "wxarticle/list/{id}/{page}/json";


    /**
     * 首页广告   ok
     */
    public static final String MAIN_BANNER = "banner/json";

    /**
     * 首页最近项目 ok
     */
    public static final String LATEST_PROJECT = "article/listproject/0/json";
    /**
     * 收藏文章 ok
     */
    public static final String COLLECT_ARTICLE = "lg/collect/{id}/json";

    /**
     * 收藏站内文章 ok
     */
    public static final String COLLECT_INSIDE_ARTICLE = "lg/collect/{id}/json";

    /**
     * 取消收藏的文章 ok
     */
    public static final String UNCOLLECT_ARTICLE = "lg/uncollect_originId/{id}/json";

    /**
     * 删除收藏的文章  ok  收藏界面取消收藏
     */
    public static final String DELETE_COLLECT_ARTICLE = "lg/uncollect/{id}/json";

    /**
     * 知识体系  ok
     */
    public static final String TREE = "tree/json";

    /**
     * 知识体系文章列表  ok
     */
    public static final String TREE_LIST = "article/list/{page}/json?";
    /**
     * 收藏的文章列表  ok
     */
    public static final String COLLECT_ARTICLE_LIST = "lg/collect/list/{page}/json";

    /**
     * 搜索  ok
     */
    public static final String SEARCH = "article/query/{page}/json";

    /**
     * 搜索热词  ok
     */
    public static final String HOT_KEYWORD = "/hotkey/json";

    /*
      反馈平台  ok
     */
    public static final String FEED_BACK = "http://support.qq.com/product/41496";


    /**
     * 常用网站 ok
     */
    public static final String FRIEND = "friend/json";

    /*
    导航
    */
    public static final String NAVI = "navi/json";

    /*
    * 项目分类 ok
    * */
    public static final String PROJECT = "project/tree/json";

    /*
    * 项目列表 ok
    * */
    public static final String PROJECT_LIST = "project/list/{page}/json?cid=id";
}
