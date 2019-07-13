package com.coolweather.android.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.coolweather.android.gson.Weather;
import com.coolweather.android.util.HttpUtil;
import com.coolweather.android.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {
    public AutoUpdateService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBingPi();
        AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);
        int anhour=8*60*60*1000;
        long triggerAtTime= SystemClock.elapsedRealtime()+anhour;
        Intent i=new Intent(this,AutoUpdateService.class);
        PendingIntent pi=PendingIntent.getService(this,0,i,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateBingPi() {
        String requestBingPic="http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkhttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
              String bingPic =response.body().string();
              SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
              editor.putString("bing_pic",bingPic);
              editor.apply();
            }
        });
    }

    private void updateWeather() {
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString=prefs.getString("weather",null);
        if (weatherString!=null)
        {
            Weather weather= Utility.handleweatherResponse(weatherString);
            String weatherId=weather.basic.cid;
            String weatherurl="https://free-api.heweather.net/s6/weather/?location="+weatherId+"&key=221a8e06e0b8433eaf1fd9fca404ca68";
            HttpUtil.sendOkhttpRequest(weatherurl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                   String responseText=response.body().string();
                   Weather weather=Utility.handleweatherResponse(responseText);
                   if (weather!=null&&"ok".equals(weather.status))
                   {
                       SharedPreferences.Editor editor=PreferenceManager.
                               getDefaultSharedPreferences(AutoUpdateService.this).
                               edit();
                       editor.putString("weather",responseText);
                       editor.apply();
                   }
                }
            });
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
