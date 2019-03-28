package com.example.lorebase.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ShareHistory {
    @Id(autoincrement = true)
    private
    Long id_share;
    @Unique
    private
    String title;
    private
    String link;
    private
    String date;
    private
    String shareMan;
    @Generated(hash = 1102481411)
    public ShareHistory(Long id_share, String title, String link, String date,
            String shareMan) {
        this.id_share = id_share;
        this.title = title;
        this.link = link;
        this.date = date;
        this.shareMan = shareMan;
    }
    @Generated(hash = 1939105911)
    public ShareHistory() {
    }
    public Long getId_share() {
        return this.id_share;
    }
    public void setId_share(Long id_share) {
        this.id_share = id_share;
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
    public String getShareMan() {
        return this.shareMan;
    }
    public void setShareMan(String shareMan) {
        this.shareMan = shareMan;
    }
}
