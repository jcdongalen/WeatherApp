package com.firex.media.weatherapp.Activities;

import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.firex.media.weatherapp.Fragments.ButtonRefresh;
import com.firex.media.weatherapp.Fragments.CityList;
import com.firex.media.weatherapp.R;

public class MainActivityUsingFragments extends AppCompatActivity implements ButtonRefresh.OnClickListener {

    private FragmentTransaction mFragmentTransaction;
    private FragmentManager mFragmentManager;
    private ButtonRefresh fragButtonRefresh;
    private CityList fragCityList;

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
}
