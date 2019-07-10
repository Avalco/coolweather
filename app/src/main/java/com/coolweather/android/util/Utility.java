package com.coolweather.android.util;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.coolweather.android.db.County;
import com.coolweather.android.db.City;
import com.coolweather.android.db.Province;
import com.coolweather.android.gson.Weather;
import com.google.gson.Gson;

import static android.content.ContentValues.TAG;


public class Utility {
    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response))
        {
            try {
                JSONArray allPronvences = new  JSONArray(response);
                for (int i=0;i<allPronvences.length();i++)
                {
                    JSONObject provinceObject = allPronvences.getJSONObject(i);
                    Province province= new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } return  false;
    }

    public static boolean handleCityResponse(String response, int PronvenceId){
        if(!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCity = new JSONArray(response);
                for (int i = 0; i < allCity.length(); i++) {
                    JSONObject jsonObject = allCity.getJSONObject(i);
                    City city = new City();
                    city.setCityName(jsonObject.getString("name"));
                    city.setCityCode(jsonObject.getInt("id"));
                    city.setProvinceId(PronvenceId);
                    city.save();
            }
                return  true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }return false;

    }

    public static boolean handleCountyResponse(String response,int CityId)
    {
        if (!TextUtils.isEmpty(response))
        {
            try {
                JSONArray allCounty= new JSONArray(response);
                for(int i=0;i<allCounty.length();i++)
                {
                    JSONObject CountyObject= allCounty.getJSONObject(i);
                    County county =new County();
                    county.setCountyName(CountyObject.getString("name"));
                    county.setCityId(CityId);
                    county.setWeatherId(CountyObject.getString("weather_id"));
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public static Weather handleweatherResponse(String response)  {
        try {
            JSONObject jsonObject =new JSONObject(response);
            JSONArray jsonArray =jsonObject.getJSONArray("HeWeather6");
            String weatherContent =jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
