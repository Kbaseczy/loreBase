package com.example.lorebase.bean;

import java.util.List;

public class BiYing {

    /**
     * images : [{"startdate":"20190321","fullstartdate":"201903211600","enddate":"20190322","url":"/th?id=OHR.TashkurganGrasslands_ZH-CN1141881683_1920x1080.jpg&rf=NorthMale_1920x1080.jpg&pid=hp","urlbase":"/th?id=OHR.TashkurganGrasslands_ZH-CN1141881683","copyright":"塔什库尔干草原上的水车，中国新疆塔吉克自治县 (© Ratnakorn Piyasirisorost/Getty Images)","copyrightlink":"http://www.bing.com/search?q=%E6%B0%B4%E8%BD%A6&form=hpcapt&mkt=zh-cn","title":"","quiz":"/search?q=Bing+homepage+quiz&filters=WQOskey:%22HPQuiz_20190321_TashkurganGrasslands%22&FORM=HPQUIZ","wp":true,"hsh":"db27b33e167deea8553280ac3bbb6c7a","drk":1,"top":1,"bot":1,"hs":[]}]
     * tooltips : {"loading":"正在加载...","previous":"上一个图像","next":"下一个图像","walle":"此图片不能下载用作壁纸。","walls":"下载今日美图。仅限用作桌面壁纸。"}
     */

    private List<ImagesBean> images;

    public List<ImagesBean> getImages() {
        return images;
    }

    public void setImages(List<ImagesBean> images) {
        this.images = images;
    }

    public static class ImagesBean {
        /**
         * startdate : 20190321
         * fullstartdate : 201903211600
         * enddate : 20190322
         * url : /th?id=OHR.TashkurganGrasslands_ZH-CN1141881683_1920x1080.jpg&rf=NorthMale_1920x1080.jpg&pid=hp
         * urlbase : /th?id=OHR.TashkurganGrasslands_ZH-CN1141881683
         * copyright : 塔什库尔干草原上的水车，中国新疆塔吉克自治县 (© Ratnakorn Piyasirisorost/Getty Images)
         * copyrightlink : http://www.bing.com/search?q=%E6%B0%B4%E8%BD%A6&form=hpcapt&mkt=zh-cn
         * title :
         * quiz : /search?q=Bing+homepage+quiz&filters=WQOskey:%22HPQuiz_20190321_TashkurganGrasslands%22&FORM=HPQUIZ
         * wp : true
         * hsh : db27b33e167deea8553280ac3bbb6c7a
         * drk : 1
         * top : 1
         * bot : 1
         * hs : []
         */

        private String startdate;
        private String fullstartdate;
        private String enddate;
        private String url;
        private String urlbase;
        private String copyright;
        private String copyrightlink;
        private String title;
        private String quiz;
        private boolean wp;
        private String hsh;
        private int drk;
        private int top;
        private int bot;
        private List<?> hs;

        public String getStartdate() {
            return startdate;
        }

        public void setStartdate(String startdate) {
            this.startdate = startdate;
        }

        public String getFullstartdate() {
            return fullstartdate;
        }

        public void setFullstartdate(String fullstartdate) {
            this.fullstartdate = fullstartdate;
        }

        public String getEnddate() {
            return enddate;
        }

        public void setEnddate(String enddate) {
            this.enddate = enddate;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrlbase() {
            return urlbase;
        }

        public void setUrlbase(String urlbase) {
            this.urlbase = urlbase;
        }

        public String getCopyright() {
            return copyright;
        }

        public void setCopyright(String copyright) {
            this.copyright = copyright;
        }

        public String getCopyrightlink() {
            return copyrightlink;
        }

        public void setCopyrightlink(String copyrightlink) {
            this.copyrightlink = copyrightlink;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getQuiz() {
            return quiz;
        }

        public void setQuiz(String quiz) {
            this.quiz = quiz;
        }

        public boolean isWp() {
            return wp;
        }

        public void setWp(boolean wp) {
            this.wp = wp;
        }

        public String getHsh() {
            return hsh;
        }

        public void setHsh(String hsh) {
            this.hsh = hsh;
        }

        public int getDrk() {
            return drk;
        }

        public void setDrk(int drk) {
            this.drk = drk;
        }

        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
        }

        public int getBot() {
            return bot;
        }

        public void setBot(int bot) {
            this.bot = bot;
        }

        public List<?> getHs() {
            return hs;
        }

        public void setHs(List<?> hs) {
            this.hs = hs;
        }
    }
}
