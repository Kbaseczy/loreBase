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

    @Generated(hash = 102129849)
    public BrowseHistory(Long id_browse, String title, String link, String date,
            boolean is_colloct, double latidude, double longitude, boolean is_out) {
        this.id_browse = id_browse;
        this.title = title;
        this.link = link;
        this.date = date;
        this.is_colloct = is_colloct;
        this.latidude = latidude;
        this.longitude = longitude;
        this.is_out = is_out;
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
}
