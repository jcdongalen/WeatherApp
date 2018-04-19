package com.firex.media.weatherapp.Utils;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;

public class MainHelper {

    private Context mContext;
    private static Gson gson = new Gson();

    public MainHelper(Context context) {
        this.mContext = context;
    }

    public static float Celcius_to_Farenheight(float celcius) {
        return celcius * (9 / 5) + 32;
    }

    public static Gson getGson() {
        return gson == null ? new Gson() : gson;
    }

    public static void showToast(Context context, String message, boolean shortDuration) {
        Toast.makeText(context, message, shortDuration ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
    }

}
