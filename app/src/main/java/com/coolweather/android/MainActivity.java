package com.coolweather.android;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
   private ProgressDialog progressDialog;
    public LocationClient mLocationClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // locationService = new LocationService(getApplicationContext());
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: share");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getString("weather",null)!=null){
            Intent intent=new Intent(this,WeatherActivity.class);
            startActivity(intent);
            finish();
        }
         else{
             mLocationClient=new LocationClient(getApplicationContext());
            LocationClientOption option = new LocationClientOption();
            option.setCoorType("gcj02");
            option.setIsNeedAddress(true);
            option.setIgnoreKillProcess(true);
//可选，是否需要地址信息，默认为不需要，即参数为false
//如果开发者需要获得当前点的地址信息，此处必须为true

            mLocationClient.setLocOption(option);
            mLocationClient.registerLocationListener(new MyLocationListener());
            List<String>permissionList=new ArrayList<>();
            if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
                permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
                permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
            if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED)
                permissionList.add(Manifest.permission.READ_PHONE_STATE);
            if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
                permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (!permissionList.isEmpty())
            {
                String []permissions =permissionList.toArray(new String [permissionList.size()]);
                ActivityCompat.requestPermissions(MainActivity.this,permissions,1);
            }
            else {
                requestLocation();
            }

        }


        }
private  void requestLocation()
{
    showProgressDialog();
    mLocationClient.start();
}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case 1:
                if (grantResults.length>0)
                {
                    for (int result:grantResults){
                        if (result!=PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"定位权限未打开，请您手动选择城市",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    requestLocation();
                }else {
                    Toast.makeText(this,"发生未知错误，请您手动选择城市",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public class  MyLocationListener extends BDAbstractLocationListener {

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        Log.d(TAG, "onReceiveLocation: "+bdLocation.getDistrict());
        Location locat=new Location();
        Log.d(TAG, "onReceiveLocation: "+bdLocation.getLongitude()+","+bdLocation.getLatitude());
        Log.d(TAG, "onReceiveLocation: "+bdLocation.getCoorType()+"    "+bdLocation.getLocType());
        Intent intent=new Intent(MainActivity.this,WeatherActivity.class);
        if (bdLocation.getLocType()==161||bdLocation.getLocType()==61)
        {intent.putExtra("weather_id",bdLocation.getLongitude()+","+bdLocation.getLatitude());
        intent.putExtra("air_id",bdLocation.getCity());
        closeProgressDialog();
        startActivity(intent);
        finish();}
        else {
            Toast.makeText(MainActivity.this,"定位失败，请您手动选择城市",Toast.LENGTH_SHORT).show();
            closeProgressDialog();
        }
    }
}
    private void showProgressDialog() {
        if (progressDialog==null)
        {
            progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("正在定位中...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    private void closeProgressDialog() {
        if (progressDialog!=null)progressDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
