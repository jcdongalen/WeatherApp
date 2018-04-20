package com.firex.media.weatherapp.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.firex.media.weatherapp.BuildConfig;
import com.firex.media.weatherapp.Models.Constans;
import com.firex.media.weatherapp.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BuildConfig.ENVIRONMENT.equalsIgnoreCase(Constans.BUILD_PRODUCTION))
            setContentView(R.layout.activity_splash_screen_production);
        else
            setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(() -> {
            finish();
            Intent intent = new Intent(SplashScreen.this, MainActivityUsingFragments.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }, Constans.SPLASH_SCREEN_DELAY);
    }
}
