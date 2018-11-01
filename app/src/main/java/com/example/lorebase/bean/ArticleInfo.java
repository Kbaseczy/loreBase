package com.example.lorebase.bean;

import java.util.List;

public class ArticleInfo {

    /**
     * status : 0
     * data : {"pageNum":1,"pageSize":6,"size":6,"orderBy":null,"startRow":0,"endRow":5,"total":6,"pages":1,"list":[{"id":1,"categoryId":1,"name":"213","author":"123"},{"id":2,"categoryId":5,"name":"java测试","author":"jayne"},{"id":3,"categoryId":6,"name":"123","author":"123"},{"id":4,"categoryId":1,"name":"123","author":"123"},{"id":5,"categoryId":6,"name":"1111222","author":"123123"},{"id":7,"categoryId":1,"name":"123","author":"1123"}],"firstPage":1,"prePage":0,"nextPage":0,"lastPage":1,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":false,"hasNextPage":false,"navigatePages":8,"navigatepageNums":[1]}
     */
    private DataBean data;
    public DataBean getData() {
        return data;
    }
    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * list : [{"id":1,"categoryId":1,"name":"213","author":"123"},{"id":2,"categoryId":5,"name":"java测试","author":"jayne"},{"id":3,"categoryId":6,"name":"123","author":"123"},{"id":4,"categoryId":1,"name":"123","author":"123"},{"id":5,"categoryId":6,"name":"1111222","author":"123123"},{"id":7,"categoryId":1,"name":"123","author":"1123"}]
         */
        private int size;
        private List<ListBean> list;
        public int getSize() {
            return size;
        }
        public void setSize(int size) {
            this.size = size;
        }
        public List<ListBean> getList() {
            return list;
        }
        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 1
             * categoryId : 1
             * name : 213
             * author : 123
             */
            private int id;
            private int categoryId;
            private String name;
            private String author;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
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

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }
        }
    }
}
