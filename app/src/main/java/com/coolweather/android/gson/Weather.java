package com.coolweather.android.gson;

import android.widget.ListView;

import java.util.List;

public class Weather {
    public Basic basic;
    public Update update;
    public  String status;
    public Now now;
   public List<Daily_forecast> daily_forecast;
   public List<Lifestyle> lifestyle;
}
