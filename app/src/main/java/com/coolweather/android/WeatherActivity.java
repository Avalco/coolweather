package com.coolweather.android;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coolweather.android.gson.Daily_forecast;
import com.coolweather.android.gson.Weather;
import com.coolweather.android.util.HttpUtil;
import com.coolweather.android.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private ImageView weather_image;
    private TextView weatherInfoText;
    private TextView wind_sc;
    private  TextView win_dir;
    private TextView vis;
    private LinearLayout forecastLayout;
    private TextView comfort;
    private TextView drsg;
    private TextView flu;
   private TextView sport;
   private TextView trav;
   private  TextView uv;
   private  TextView cw;
   private  TextView air;
   private ImageView bingPicImg;
   private List<Integer> pic=new ArrayList<>();
    private static final String TAG = "WeatherActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT>=21)
        {
            View decorView =getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        bingPicImg=(ImageView)findViewById(R.id.bing_pic_img);
        weatherLayout =(ScrollView)findViewById(R.id.weather_layout);
        titleCity=(TextView)findViewById(R.id.title_city);
        titleUpdateTime=(TextView)findViewById(R.id.title_update_time);
        degreeText=(TextView)findViewById(R.id.degree_text);
        weatherInfoText=(TextView)findViewById(R.id.weather_info_text);
        weather_image=(ImageView)findViewById(R.id.weather_info_image);
        win_dir=(TextView)findViewById(R.id.wind_dir_text);
        wind_sc=(TextView)findViewById(R.id.wind_sc_text);
        vis=(TextView)findViewById(R.id.vis_text);
        forecastLayout=(LinearLayout)findViewById(R.id.forecast_layout);
        comfort=(TextView)findViewById(R.id.comfort_text);
        drsg=(TextView)findViewById(R.id.drsg_text);
        flu=(TextView)findViewById(R.id.flu_text);
        sport=(TextView)findViewById(R.id.sport_text);
        trav=(TextView)findViewById(R.id.trav_text);
        uv=(TextView)findViewById(R.id.uv_text);
        cw=(TextView)findViewById(R.id.cw_text);
        air=(TextView)findViewById(R.id.air_text);
        pic.add(R.drawable.d100);
        pic.add(R.drawable.d101);
        pic.add(R.drawable.d102);
        pic.add(R.drawable.d103);
        pic.add(R.drawable.d104);
        pic.add(R.drawable.d300);
        pic.add(R.drawable.d301);
        pic.add(R.drawable.d302);
        pic.add(R.drawable.d303);
        pic.add(R.drawable.d304);
        pic.add(R.drawable.d305);
        pic.add(R.drawable.d306);
        pic.add(R.drawable.d307);
        pic.add(R.drawable.d308);
        pic.add(R.drawable.d309);
        pic.add(R.drawable.d310);
        pic.add(R.drawable.d311);
        pic.add(R.drawable.d312);
        pic.add(R.drawable.d313);
        pic.add(R.drawable.d314);
        pic.add(R.drawable.d315);
        pic.add(R.drawable.d316);
        pic.add(R.drawable.d317);
        pic.add(R.drawable.d318);
        pic.add(R.drawable.d399);
        pic.add(R.drawable.d400);
        pic.add(R.drawable.d401);
        pic.add(R.drawable.d402);
        pic.add(R.drawable.d403);
        pic.add(R.drawable.d404);
        pic.add(R.drawable.d405);
        pic.add(R.drawable.d406);
        pic.add(R.drawable.d407);
        pic.add(R.drawable.d408);
        pic.add(R.drawable.d409);
        pic.add(R.drawable.d410);
        pic.add(R.drawable.n100);
        pic.add(R.drawable.n103);
        pic.add(R.drawable.n104);
        pic.add(R.drawable.n300);
        pic.add(R.drawable.n301);
        pic.add(R.drawable.n406);
        pic.add(R.drawable.d407);

        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString =prefs.getString("weather",null);
        String bingPic=prefs.getString("bing_pic",null);
        if (bingPic!=null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }
        else {
            loadBingPic();
        }
        if (weatherString!=null){
            Weather weather= Utility.handleweatherResponse(weatherString);

            showWeatherInfo(weather);
        }else{
            String weatherId=getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }
    }

    private void loadBingPic() {
        String requestBingPic ="http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkhttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final  String bingPic =response.body().string();
                SharedPreferences.Editor editor =PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }

    private void requestWeather(final String weatherId) {
        String weatherurl ="https://free-api.heweather.net/s6/weather/?location="+weatherId+"&key=221a8e06e0b8433eaf1fd9fca404ca68";
        HttpUtil.sendOkhttpRequest(weatherurl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"网络连接失败",Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
              final String responsetext=response.body().string();
                Log.d(TAG, "onCreate: "+responsetext);
              final  Weather weather=Utility.handleweatherResponse(responsetext);
              runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                      if (weather !=null&&"ok".equals(weather.status))
                      {
                          SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                          editor.putString("weather",responsetext);
                          editor.apply();showWeatherInfo(weather);
                      }
                      else {
                          Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                      }
                  }
              });
            }
        });
    }

    private void showWeatherInfo(Weather weather) {
        String cityname=weather.basic.cityname;
        String updatetime=weather.update.loc.split(" ")[1];
        String degree=weather.now.tmp+"℃";
        String weatherinfo=weather.now.cond_txt;
        titleCity.setText(cityname);
        int i=Integer.parseInt(weather.now.cond_code);
        if (i<200)i=i%10;
        else if (i<399)i=5+i%100;
        else if(i==399)i=24;
        else if(i<500)i=25+i%100;
       if (i<36)weather_image.setImageResource(pic.get(i));
       else weather_image.setVisibility(View.GONE);
        titleUpdateTime.setText("上次更新时间："+updatetime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherinfo);
        wind_sc.setText("风力："+weather.now.wind_sc);
        win_dir.setText("风向："+weather.now.wind_dir);
        vis.setText("能见度："+weather.now.vis);
        forecastLayout.removeAllViews();
        for (Daily_forecast forecast :weather.daily_forecast){
            View view= LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView datetext=(TextView)view.findViewById(R.id.date_text);
            TextView infotext=(TextView)view.findViewById(R.id.info_text);
            TextView mxmin=(TextView)view.findViewById(R.id.max_min_text);
            ImageView infoimage=(ImageView)view.findViewById(R.id.info_image);
            ImageView infoimage2=(ImageView)view.findViewById(R.id.info_image2);
            datetext.setText(forecast.date);
            infotext.setText(forecast.cond_txt_d+"/"+forecast.cond_txt_n);
            mxmin.setText(forecast.tmp_max+"/"+forecast.tmp_min);
           i=Integer.parseInt(forecast.cond_code_d);
            if (i<200)i=i%10;
            else if (i<399)i=5+i%100;
            else if(i==399)i=24;
            else if(i<500)i=25+i%100;
            if (i<36)infoimage2.setImageResource(pic.get(i));
            else infoimage2.setVisibility(View.GONE);
            pic.add(R.drawable.n100);
            pic.add(R.drawable.n103);
            pic.add(R.drawable.n104);
            pic.add(R.drawable.n300);
            pic.add(R.drawable.n301);
            pic.add(R.drawable.n406);
            pic.add(R.drawable.d407);
            i=Integer.parseInt(forecast.cond_code_n);
            if (i==100)i=36;
            else if (i==103)i=37;
            else if (i==104)i=38;
            else if (i==300)i=39;
            else if (i==301)i=40;
            else if (i==406)i=41;
            else if (i==407)i=42;
            else if (i<200)i=i%10;
            else if (i<399)i=5+i%100;
            else if(i==399)i=24;
            else if(i<500)i=25+i%100;
            if (i<43)infoimage.setImageResource(pic.get(i));
            else infoimage.setVisibility(View.GONE);
            forecastLayout.addView(view);
        }
       comfort.setText("舒适度："+weather.lifestyle.get(0).txt);
        drsg.setText("穿衣指数："+weather.lifestyle.get(1).txt);
        flu.setText("感冒指数："+weather.lifestyle.get(2).txt);
        sport.setText("运动指数："+weather.lifestyle.get(3).txt);
        trav.setText("旅游指数："+weather.lifestyle.get(4).txt);
        uv.setText("紫外线指数："+weather.lifestyle.get(5).txt);
        cw.setText("洗车指数："+weather.lifestyle.get(6).txt);
        air.setText("空气污染扩散条件指数："+weather.lifestyle.get(7).txt);
        weatherLayout.setVisibility(View.VISIBLE);
    }
}
