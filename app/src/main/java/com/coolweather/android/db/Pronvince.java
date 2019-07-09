package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

public class Pronvince extends DataSupport {
    private int id;
    private String pronvinceName;
    private int pronvinceCode;

    public void setId(int id) {
        this.id = id;
    }

    public void setPronvinceCode(int pronvinceCode) {
        this.pronvinceCode = pronvinceCode;
    }

    public void setPronvinceName(String pronvinceName) {
        this.pronvinceName = pronvinceName;
    }

    public int getId() {
        return id;
    }

    public int getPronvinceCode() {
        return pronvinceCode;
    }

    public String getPronvinceName() {
        return pronvinceName;
    }
}
