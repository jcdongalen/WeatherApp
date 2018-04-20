package com.firex.media.weatherapp.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.crashlytics.android.Crashlytics;
import com.firex.media.weatherapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ButtonRefresh extends Fragment {

    public interface OnClickListener{
        void onRefreshButtonClicked();
    }

    private OnClickListener mListener;
    private View view;
    private Button btnRefresh;

    public ButtonRefresh() {
        // Required empty public constructor
    }

    public void setOnClickListener(OnClickListener listener){
        this.mListener = listener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_button_refresh, container, false);
        btnRefresh = view.findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRefreshButtonClicked();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
