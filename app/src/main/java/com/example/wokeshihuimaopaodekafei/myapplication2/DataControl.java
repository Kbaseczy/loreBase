package com.example.wokeshihuimaopaodekafei.myapplication2;

import java.util.HashMap;


public class DataControl {
    public static HashMap<String,String>getHashMap(String ImageID,String storeName,String address,String telephone){
        HashMap<String,String>map=new HashMap<>();
        map.put("img",ImageID);
        map.put("storeName",storeName);
        map.put("address",address);
        map.put("telephone",telephone);

        return map;
    }
    public static HashMap<String,String> getCar(String imageID,String carName,String info,String price,String rating){
        HashMap<String,String>map= new HashMap<>();
        map.put("img",imageID);
        map.put("carName",carName);
        map.put("info",info);
        map.put("price",price);
        map.put("rating",rating);

        return map;
    }
}
