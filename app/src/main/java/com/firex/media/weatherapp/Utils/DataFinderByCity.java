package com.firex.media.weatherapp.Utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.firex.media.weatherapp.Activities.MainActivity;
import com.firex.media.weatherapp.Models.WeatherDetails;
import com.firex.media.weatherapp.R;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DataFinderByCity {

    public interface DataFinder {
        void onDataFinderStarted();

        void onDataFinderCompleted(List<WeatherDetails> fetched);

        void onDataError(String ErrorMessage);
    }

    private String[] mDatatoFetch;
    private Context mContext;
    private List<WeatherDetails> mFetchedWeatherDetails;
    private DataFinder mDataFinder;
    private int mPendingRequest = 0;

    public DataFinderByCity(@NonNull Context context, @NonNull DataFinder dataFinder) {
        this.mContext = context;
        this.mDataFinder = dataFinder;
    }

    public void startDownloadingData(String[] data) {
        mDataFinder.onDataFinderStarted();
        this.mDatatoFetch = data;
        mFetchedWeatherDetails = new ArrayList<>();
        for (int i = 0; i < mDatatoFetch.length; i++) {
            final int position = i + 1;
            String url = String.format(mContext.getString(R.string.api_get_weather_by_city), mDatatoFetch[i], mContext.getString(R.string.openweather_api_key));
            StringRequest cityRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            mPendingRequest--;
                            WeatherDetails item = MainHelper.getGson().fromJson(response, WeatherDetails.class);
                            if (item != null)
                                mFetchedWeatherDetails.add(item);

                            if (mPendingRequest == 0)
                                mDataFinder.onDataFinderCompleted(mFetchedWeatherDetails);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            mPendingRequest--;
                            mDataFinder.onDataError("Error in fetching data with: " + url);
                        }
                    });
            MyVolleySingleton.getInstance(mContext).addToRequestQueue(cityRequest);
            mPendingRequest++;
        }
    }

    public boolean isRunning() {
        return mPendingRequest > 0;
    }
}
