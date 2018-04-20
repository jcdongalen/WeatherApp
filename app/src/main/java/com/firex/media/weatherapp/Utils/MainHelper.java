package com.firex.media.weatherapp.Utils;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.DecimalFormat;

public class MainHelper {

    private Context mContext;
    private static Gson gson = new Gson();
    private static DecimalFormat DecimalFormatTemperature = new DecimalFormat("##.#");

    public MainHelper(Context context) {
        this.mContext = context;
    }

    public static double Celcius_to_Farenheight(float celcius) {
        return (celcius * 1.8) + 32;
    }

    public static Gson getGson() {
        return gson == null ? new Gson() : gson;
    }

    public static void showToast(Context context, String message, boolean shortDuration) {
        Toast.makeText(context, message, shortDuration ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
    }

    public static String getDecimalFormat(float number){
        return DecimalFormatTemperature.format(number);
    }

    public static String getDecimalFormat(double number){
        return DecimalFormatTemperature.format(number);
    }
}
