package com.firex.media.weatherapp.Activities;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.firex.media.weatherapp.Adapters.CityWeatherAdapter;
import com.firex.media.weatherapp.Models.WeatherDetails;
import com.firex.media.weatherapp.R;
import com.firex.media.weatherapp.Utils.DataFinderByCity;
import com.firex.media.weatherapp.Utils.MainHelper;
import com.firex.media.weatherapp.Utils.MyVolleySingleton;
import com.firex.media.weatherapp.Views.CustomDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DataFinderByCity.DataFinder {

    private static final String[] cities = new String[]{"London", "Prague", "San Francisco"};
    private boolean isItmsLoaded;

    private RecyclerView rvCity;
    private RelativeLayout rlLoadingView;
    private Button btnRefresh;
    private CityWeatherAdapter mCityWeatherAdapter;
    private List<WeatherDetails> mFetchedWeatherDetails;
    private DataFinderByCity mDataFinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFetchedWeatherDetails = new ArrayList<>();

        rvCity = findViewById(R.id.rvCity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvCity.setLayoutManager(mLayoutManager);
        rvCity.setItemAnimator(new DefaultItemAnimator());
        rvCity.addItemDecoration(new CustomDividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL, 8));

        rlLoadingView = findViewById(R.id.rlLoadingView);
        btnRefresh = findViewById(R.id.btnRefresh);
        mDataFinder = new DataFinderByCity(this, this);
        mDataFinder.startDownloadingData(cities);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataFinder.startDownloadingData(cities);
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onDataFinderStarted() {
        rlLoadingView.setVisibility(View.VISIBLE);
        MainHelper.showToast(this, "Finding data started.", false);
    }

    @Override
    public void onDataFinderCompleted(List<WeatherDetails> fetched) {
        rlLoadingView.setVisibility(View.GONE);
        mCityWeatherAdapter = new CityWeatherAdapter(this, fetched);
        rvCity.setAdapter(mCityWeatherAdapter);
        mCityWeatherAdapter.notifyDataSetChanged();
        MainHelper.showToast(this, "Finding data completed.", false);
    }

    @Override
    public void onDataError(String errorMessage) {
        MainHelper.showToast(this, errorMessage, false);
    }
}
