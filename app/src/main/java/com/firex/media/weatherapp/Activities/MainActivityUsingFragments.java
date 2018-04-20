package com.firex.media.weatherapp.Activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.firex.media.weatherapp.Fragments.ButtonRefresh;
import com.firex.media.weatherapp.Fragments.CityList;
import com.firex.media.weatherapp.Models.Constans;
import com.firex.media.weatherapp.Models.WeatherDetails;
import com.firex.media.weatherapp.R;
import com.firex.media.weatherapp.Utils.MainHelper;
import com.firex.media.weatherapp.Utils.MyVolleySingleton;

public class MainActivityUsingFragments extends AppCompatActivity implements ButtonRefresh.OnClickListener {

    private static final int REQUEST_PERMISSION_LOCATION = 1000;

    private FragmentTransaction mFragmentTransaction;
    private FragmentManager mFragmentManager;
    private ButtonRefresh fragButtonRefresh;
    private CityList fragCityList;

    private FloatingActionButton fabGotoMyWeather;

    private LocationManager mLocationManager;
    private Location mLocation;

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mLocation = location;
            if(fabGotoMyWeather.getVisibility() != View.VISIBLE)
                fabGotoMyWeather.setVisibility(View.VISIBLE);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            if (provider.equalsIgnoreCase(LocationManager.GPS_PROVIDER))
                fabGotoMyWeather.setVisibility(View.VISIBLE);
        }

        @Override
        public void onProviderDisabled(String provider) {
            if (provider.equalsIgnoreCase(LocationManager.GPS_PROVIDER))
                fabGotoMyWeather.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_using_fragments);

        fabGotoMyWeather = findViewById(R.id.fabGotoMyWeather);
        fabGotoMyWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLocation != null) {
                    String url = String.format(getString(R.string.api_get_weather_by_latlon), String.valueOf(mLocation.getLatitude()), String.valueOf(mLocation.getLongitude()), getString(R.string.openweather_api_key));
                    StringRequest myCurrentWeatherRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    WeatherDetails item = MainHelper.getGson().fromJson(response, WeatherDetails.class);
                                    if (item != null) {
                                        Intent intent = new Intent(MainActivityUsingFragments.this, WeatherDetailActivity.class);
                                        intent.putExtra("WeatherDetail", MainHelper.getGson().toJson(item));
                                        startActivity(intent);
                                    } else
                                        MainHelper.showToast(MainActivityUsingFragments.this, "No weather details found.", false);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    MainHelper.showToast(MainActivityUsingFragments.this, "Failed to get your weather details. Please try again later.", false);
                                }
                            });
                    MyVolleySingleton.getInstance(MainActivityUsingFragments.this).addToRequestQueue(myCurrentWeatherRequest);
                } else
                    MainHelper.showToast(MainActivityUsingFragments.this, "Please wait while LocationService is fetching your data.", true);
            }
        });

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();

        fragButtonRefresh = new ButtonRefresh();
        fragButtonRefresh.setOnClickListener(this);
        fragCityList = new CityList();

        mFragmentTransaction.add(R.id.frmButton, fragButtonRefresh, "BUTTON");
        mFragmentTransaction.add(R.id.frmCityList, fragCityList, "CITY_LIST");
        mFragmentTransaction.commit();

        setUpLocationService();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onRefreshButtonClicked() {
        fragCityList.startSeatching();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage("Do you wanna close the app?")
                .setTitle("Exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do Nothing
                    }
                })
                .show();
    }

    private void setUpLocationService() {
        //Prepare for location request
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION_LOCATION);
            return;
        }
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, Constans.LOCATION_REQUEST_DELAY, 10, locationListener);
        if (mLocation != null) {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setUpLocationService();
            } else {
                MainHelper.showToast(this, "Locaiton service permission has been declined", false);
                fabGotoMyWeather.setVisibility(View.GONE);
            }
        }
    }
}
