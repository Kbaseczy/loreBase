package com.example.lorebase.contain_const;

/**
 * Api接口地址
 */
public class UrlContainer {
    public static final String baseUrl = "https://wanandroid.com/";
    /**
     * 登录  ok
     */
    public static final String LOGIN = "user/login";

    /**
     * 登出  ok
     */
    public static final String LOGOUT = "user/logout/json";
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
     *   ok
     */
    public static final String HOT_KEYWORD = "/hotkey/json";

    /*
      反馈平台  ok
     */
    public static final String FEED_BACK = "http://support.qq.com/product/41496";

    /*
    github  ok
     */
    public static final String GITHUB = "https://github.com/Kbaseczy";

    /**
     * 常用网站 ok
     */
    public static final String FRIEND = "friend/json";

    /*
    导航  ok
    */
    public static final String NAVI = "navi/json";

    /*
     * 项目分类 ok
     * */
    public static final String PROJECT = "project/tree/json";

    /*
     * 项目列表 ok
     * */
    public static final String PROJECT_LIST = "project/list/{page}/json";

    /*
     * TODO 未完成列表
     * */
    public static final String TODO_UNCOMPLETE = "lg/todo/v2/list/{page}/json?status=0";

    /*
     * TODO 完成列表
     * */
    public static final String TODO_COMPLETE = "lg/todo/v2/list/{page}/json?status=1&orderby=2";

    /*
     * TODO 删除一个todo
     * */
    public static final String TODO_DELETE = "lg/todo/delete/{id}/json";

    /*
     * TODO 修改一个todo
     *  方法：POST
        参数：
	    id: 拼接在链接上，为唯一标识，列表数据返回时，每个todo 都会有个id标识 （必须）
	    title: 更新标题 （必须）
	    content: 新增详情（必须）
	    date: 2018-08-01（必须）
	    status: 0 // 0为未完成，1为完成
	    type: ；
	    priority: ；
     * */
    public static final String TODO_UPDATE = "lg/todo/update/{id}/json";

    /*
     * TODO 添加一个TODO
     * 方法：POST
        参数：
	    title: 新增标题（必须）
	    content: 新增详情（必须）
	    date: 2018-08-01 预定完成时间（不传默认当天，建议传）
	    type: 大于0的整数（可选）；
	    priority 大于0的整数（可选）；
     * */
    public static final String TODO_ADD = "lg/todo/add/json";

    /*
      TODO 更新todo状态（待办0 已完成1）
     */

    public static final String TODO_STATUE = "lg/todo/done/{id}/json";
    /*
        必应  ok
    */
    public static final String BI_YING_BASE = "https://cn.bing.com/";
    public static final String BI_YING = "HPImageArchive.aspx?format=js&idx=0&n=1";


}
