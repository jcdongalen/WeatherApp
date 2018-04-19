package com.firex.media.weatherapp;

import android.app.Application;

import com.firex.media.weatherapp.Utils.MyVolleySingleton;

public class WeatherApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MyVolleySingleton.getInstance(this);
    }
}
