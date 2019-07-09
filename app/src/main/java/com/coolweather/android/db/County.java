package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

public class County extends DataSupport {
    private int id;
    private String countyName;
    private int cityId;
    private int weatherId;

    public int getId() {
        return id;
    }

    public int getCityId() {
        return cityId;
    }

    public int getWeatherId() {
        return weatherId;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }
}
