package com.example.lorebase.bean;

public class DetailInfo {

    /**
     * status : 0
     * data : {"id":11,"userId":4,"categoryId":1,"name":"1231","author":"1231","status":1,"createTime":"2018-09-04T01:28:52.000+0000","updateTime":"2018-09-04T01:28:52.000+0000","content":"<p>123123<\/p>"}
     */

    private int status;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 11
         * userId : 4
         * categoryId : 1
         * name : 1231
         * author : 1231
         * status : 1
         * createTime : 2018-09-04T01:28:52.000+0000
         * updateTime : 2018-09-04T01:28:52.000+0000
         * content : <p>123123</p>
         */

        private int id;
        private int userId;
        private int categoryId;
        private String name;
        private String author;
        private int status;
        private String createTime;
        private String updateTime;
        private String content;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public  String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
