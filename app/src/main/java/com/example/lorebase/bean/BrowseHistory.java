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

    @Generated(hash = 1864434839)
    public BrowseHistory(Long id_browse, String title, String link, String date) {
        this.id_browse = id_browse;
        this.title = title;
        this.link = link;
        this.date = date;
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
}
