package com.example.lorebase.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

@Entity
public class BrowseHistory {
    @Id(autoincrement = true)
    private
    Long id_browse;
    @Unique
    private
    String title;
    private
    String link;
    private
    String date;
    private
    boolean is_colloct;
    private
    double latidude;
    private
    double longitude;
    private
    boolean is_out;
    private
    String country;
    private
    String province;
    private
    String city;
    private
    String district;
    private
    String street;

    @Generated(hash = 306101295)
    public BrowseHistory(Long id_browse, String title, String link, String date,
            boolean is_colloct, double latidude, double longitude, boolean is_out,
            String country, String province, String city, String district,
            String street) {
        this.id_browse = id_browse;
        this.title = title;
        this.link = link;
        this.date = date;
        this.is_colloct = is_colloct;
        this.latidude = latidude;
        this.longitude = longitude;
        this.is_out = is_out;
        this.country = country;
        this.province = province;
        this.city = city;
        this.district = district;
        this.street = street;
    }
    @Generated(hash = 772159025)
    public BrowseHistory() {
    }

    public Long getId_browse() {
        return this.id_browse;
    }

    public void setId_browse(Long id_browse) {
        this.id_browse = id_browse;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean getIs_colloct() {
        return this.is_colloct;
    }

    public void setIs_colloct(boolean is_colloct) {
        this.is_colloct = is_colloct;
    }

    public double getLatidude() {
        return this.latidude;
    }

    public void setLatidude(double latidude) {
        this.latidude = latidude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public boolean getIs_out() {
        return this.is_out;
    }
    public void setIs_out(boolean is_out) {
        this.is_out = is_out;
    }
    public String getCountry() {
        return this.country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getProvince() {
        return this.province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getDistrict() {
        return this.district;
    }
    public void setDistrict(String district) {
        this.district = district;
    }
    public String getStreet() {
        return this.street;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    public String getCity() {
        return this.city;
    }
    public void setCity(String city) {
        this.city = city;
    }
}
