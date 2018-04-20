package com.firex.media.weatherapp;

import android.app.Application;

import com.firex.media.weatherapp.Models.Constans;
import com.firex.media.weatherapp.Utils.MyVolleySingleton;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class WeatherApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MyVolleySingleton.getInstance(this);


        if (BuildConfig.ENVIRONMENT.equalsIgnoreCase(Constans.BUILD_PRODUCTION))
            Fabric.with(this, new Crashlytics());
    }
}
