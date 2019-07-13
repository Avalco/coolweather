package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

public class Basic {
    public  String cid;
    @SerializedName("location")
    public  String cityname;
    public String parent_city;
    public String admin_area;
    public String cnty;
    public String lat;
    public String lon;
    public String tz;
}
