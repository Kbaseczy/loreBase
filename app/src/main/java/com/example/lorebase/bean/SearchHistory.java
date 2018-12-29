package com.example.lorebase.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

@Entity
public class SearchHistory {
    @Id(autoincrement = true)
    private
    Long id;
    @Unique
    private
    String key_word;

    @Generated(hash = 1449914964)
    public SearchHistory(Long id, String key_word) {
        this.id = id;
        this.key_word = key_word;
    }

    @Generated(hash = 1905904755)
    public SearchHistory() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey_word() {
        return this.key_word;
    }

    public void setKey_word(String key_word) {
        this.key_word = key_word;
    }
}
