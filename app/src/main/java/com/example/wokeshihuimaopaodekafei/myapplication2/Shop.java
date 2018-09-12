package com.example.wokeshihuimaopaodekafei.myapplication2;

class Shop {
    public String shop_id;
    public String shop_name;
    public String shop_address;
    public String tel;
    public String shop_info;
    public int img_id;
    public String Img_name;

    public Shop() {
        super();
    }

    public Shop(String shop_name, String shop_address, String tel, String shop_info, int img_id, String Img_name, String shop_id) {
        super();
        this.shop_name = shop_name;
        this.shop_address = shop_address;
        this.tel = tel;
        this.shop_info = shop_info;
        this.img_id = img_id;
        this.Img_name = Img_name;
        this.shop_id = shop_id;
    }

    public String getImg_name() {
        return Img_name;
    }

    public void setImg_name(String img_name) {
        Img_name = img_name;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setShop_info(String shop_info) {
        this.shop_info = shop_info;
    }

    public int getImg_id() {
        return img_id;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public void setShop_address(String shop_address) {
        this.shop_address = shop_address;
    }

    public void setImg_id(int img_id) {
        this.img_id = img_id;
    }

    public String getShop_name() {

        return shop_name;
    }

    public String getShop_address() {
        return shop_address;
    }


    public String getShop_info() {
        return shop_info;
    }

}
