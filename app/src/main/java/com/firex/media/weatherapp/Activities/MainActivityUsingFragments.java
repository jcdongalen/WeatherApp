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
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_using_fragments);

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
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            MainHelper.showToast(this, "Please declined permmission for Location.", false);
            return;
        }
        //Prepare for location request
        mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, Constans.LOCATION_REQUEST_DELAY, 10, locationListener);
        if (mLocation != null) {

        }
    }

    public void gotoWeatherDetails(View view) {
        if (mLocation != null) {
            String url = String.format(getString(R.string.api_get_weather_by_latlon), String.valueOf(mLocation.getLatitude()), String.valueOf(mLocation.getLongitude()), getString(R.string.openweather_api_key));
            StringRequest myCurrentWeatherRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            WeatherDetails item = MainHelper.getGson().fromJson(response, WeatherDetails.class);
                            if(item!=null){
                                Intent intent = new Intent(MainActivityUsingFragments.this, WeatherDetailActivity.class);
                                intent.putExtra("WeatherDetail", MainHelper.getGson().toJson(item));
                                startActivity(intent);
                            }
                            else
                                MainHelper.showToast(MainActivityUsingFragments.this, "No weather details found.", false);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            MainHelper.showToast(MainActivityUsingFragments.this, "Failed to get your weather details. Please try again later.", false);
                        }
                    });
            MyVolleySingleton.getInstance(this).addToRequestQueue(myCurrentWeatherRequest);
        } else
            MainHelper.showToast(this, "Please wait while LocationService is fetching your data.", true);
    }
}
