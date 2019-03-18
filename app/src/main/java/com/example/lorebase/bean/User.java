package com.example.lorebase.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {
    /**
     * data : {"chapterTops":[],"collectIds":[3358,7369,6727,3403,7357,2795,3204,3244,7388,4259,4668,7414,4362,3467,7453,5566,7461,3292,7470,7475,7490,3461,1614,7518,4494,7562,7574,3085,7599,7592,2899,7630,7484,7608,7315,7590,7555,7553,7632,3372,7633,7508,7647,7687,7702,7712,7703,3632,4153,7410,7411,7929,3108,7959,7997,8038,4065],"email":"","icon":"","id":11685,"password":"","token":"","type":0,"username":"15541125277"}
     * errorCode : 0
     * errorMsg :
     */

    private DataBean data;
    private int errorCode;
    private String errorMsg;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }


    public static class DataBean {
        /**
         * chapterTops : []
         * collectIds : [3358,7369,6727,3403,7357,2795,3204,3244,7388,4259,4668,7414,4362,3467,7453,5566,7461,3292,7470,7475,7490,3461,1614,7518,4494,7562,7574,3085,7599,7592,2899,7630,7484,7608,7315,7590,7555,7553,7632,3372,7633,7508,7647,7687,7702,7712,7703,3632,4153,7410,7411,7929,3108,7959,7997,8038,4065]
         * email :
         * icon :
         * id : 11685
         * password :
         * token :
         * type : 0
         * username : 15541125277
         */

        private String email;
        private String icon;
        private int id;
        @SerializedName("password")
        private String passwordX;
        private String token;
        private int type;
        @SerializedName("username")
        private String usernameX;
        private List<?> chapterTops;
        private List<Integer> collectIds;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPasswordX() {
            return passwordX;
        }

        public void setPasswordX(String passwordX) {
            this.passwordX = passwordX;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUsernameX() {
            return usernameX;
        }

        public void setUsernameX(String usernameX) {
            this.usernameX = usernameX;
        }

        public List<?> getChapterTops() {
            return chapterTops;
        }

        public void setChapterTops(List<?> chapterTops) {
            this.chapterTops = chapterTops;
        }

        public List<Integer> getCollectIds() {
            return collectIds;
        }

        public void setCollectIds(List<Integer> collectIds) {
            this.collectIds = collectIds;
        }
    }
}
