package com.firex.media.weatherapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.firex.media.weatherapp.R;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvCity;
    private RelativeLayout rlLoadingView;
    private Button btnRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvCity = findViewById(R.id.rvCity);
        rlLoadingView = findViewById(R.id.rlLoadingView);
        btnRefresh = findViewById(R.id.btnRefresh);
    }
}
