package com.firex.media.weatherapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firex.media.weatherapp.Adapters.CityWeatherAdapter;
import com.firex.media.weatherapp.Models.WeatherDetails;
import com.firex.media.weatherapp.R;
import com.firex.media.weatherapp.Utils.DataFinderByCity;
import com.firex.media.weatherapp.Utils.MainHelper;
import com.firex.media.weatherapp.Views.CustomDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class CityList extends Fragment implements DataFinderByCity.DataFinder{

    public CityList() {
        // Required empty public constructor
    }

    private static final String[] cities = new String[]{"London", "Prague", "San Francisco"};

    private View view;
    private RecyclerView rvCity;
    private RelativeLayout rlLoadingView;
    private CityWeatherAdapter mCityWeatherAdapter;
    private List<WeatherDetails> mFetchedWeatherDetails;
    private DataFinderByCity mDataFinder;

    private static final String TAG ="CITY_LIST";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_city_list, container, false);
        mFetchedWeatherDetails = new ArrayList<>();

        rvCity = view.findViewById(R.id.rvCity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvCity.setLayoutManager(mLayoutManager);
        rvCity.setItemAnimator(new DefaultItemAnimator());
        rvCity.addItemDecoration(new CustomDividerItemDecoration(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, 8));

        rlLoadingView = view.findViewById(R.id.rlLoadingView);
        mDataFinder = new DataFinderByCity(getActivity(), this);
        mDataFinder.startDownloadingData(cities);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDataFinderStarted() {
        Log.wtf(TAG, "onDataFinderStarted");
        rlLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDataFinderCompleted(List<WeatherDetails> fetched) {
        Log.wtf(TAG, "onDataFinderCompleted: ArraySize = " + fetched.size());
        rlLoadingView.setVisibility(View.GONE);
        mCityWeatherAdapter = new CityWeatherAdapter(getActivity(), fetched);
        rvCity.setAdapter(mCityWeatherAdapter);
        mCityWeatherAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDataError(String errorMessage) {
        Log.wtf(TAG, "onDataFinderStarted: " + errorMessage);
    }

    public void startSeatching(){
        if(!mDataFinder.isRunning())
            mDataFinder.startDownloadingData(cities);
        else{
            MainHelper.showToast(getActivity(), "Search is still ongoing. Please try again later.", true);
        }
    }
}
