package com.firex.media.weatherapp.Activities;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.firex.media.weatherapp.Models.WeatherDetails;
import com.firex.media.weatherapp.R;
import com.firex.media.weatherapp.Utils.MainHelper;
import com.firex.media.weatherapp.Utils.MyVolleySingleton;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class WeatherDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView tvInCelciusLarge, tvWeatherDescription, tvCelsiusAndFarenheight, tvLocation;
    private ImageView imgIcon;
    private SupportMapFragment mapFragment;
    private WeatherDetails item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgIcon = findViewById(R.id.imgIcon);
        tvInCelciusLarge = findViewById(R.id.tvInCelciusLarge);
        tvWeatherDescription = findViewById(R.id.tvWeatherDescription);
        tvCelsiusAndFarenheight = findViewById(R.id.tvCelsiusAndFarenheight);
        tvLocation = findViewById(R.id.tvLocation);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
        initializeData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void gotoMap(View view) {
    }

    private void initializeData() {
        item = MainHelper.getGson().fromJson(getIntent().getStringExtra("WeatherDetail"), WeatherDetails.class);
        if (item != null) {
            String icon_url = String.format(getString(R.string.api_weather_icon), item.getWeather().get(0).getIcon());
            ImageRequest iconRequest = new ImageRequest(icon_url,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            imgIcon.setImageBitmap(response);
                        }
                    }, 0, 0, ImageView.ScaleType.FIT_CENTER, null,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            imgIcon.setImageDrawable(getResources().getDrawable(android.R.drawable.stat_sys_warning));
                        }
                    });
            MyVolleySingleton.getInstance(this).addToRequestQueue(iconRequest);

            tvInCelciusLarge.setText(String.format(getString(R.string.label_degrees), MainHelper.getDecimalFormat(item.getMain().getTemp())));
            tvWeatherDescription.setText(item.getWeather().get(0).getDescription());
            tvCelsiusAndFarenheight.setText(String.format(getString(R.string.label_celsiua_and_farenheight), MainHelper.getDecimalFormat(item.getMain().getTemp()), MainHelper.getDecimalFormat(MainHelper.Celcius_to_Farenheight(item.getMain().getTemp()))));
            tvLocation.setText(item.getName() + ", " + item.getSys().getCountry());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (item == null)
            return;
        LatLng location = new LatLng(item.getCoord().getLat(), item.getCoord().getLon());
        googleMap.addMarker(new MarkerOptions().position(location).title(item.getName() + ", " + item.getSys().getCountry()));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 10));
        googleMap.getUiSettings().setScrollGesturesEnabled(false);
    }
}
