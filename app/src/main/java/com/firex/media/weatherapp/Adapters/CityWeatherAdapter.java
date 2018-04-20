package com.firex.media.weatherapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.firex.media.weatherapp.Activities.WeatherDetailActivity;
import com.firex.media.weatherapp.Models.Constans;
import com.firex.media.weatherapp.Models.WeatherDetails;
import com.firex.media.weatherapp.R;
import com.firex.media.weatherapp.Utils.MainHelper;
import com.firex.media.weatherapp.Utils.MyVolleySingleton;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CityWeatherAdapter extends RecyclerView.Adapter<CityWeatherAdapter.ViewHolder> {

    private Context mContext;
    private List<WeatherDetails> mWeatherDetails;

    public CityWeatherAdapter(Context context, List<WeatherDetails> weatherDetails) {
        this.mContext = context;
        this.mWeatherDetails = weatherDetails;
        Collections.sort(this.mWeatherDetails, new Comparator<WeatherDetails>() {
            @Override
            public int compare(WeatherDetails o1, WeatherDetails o2) {
                return Integer.compare(o1.getId(), o2.getId());
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherDetails item = mWeatherDetails.get(position);
        holder.tvInCelcius.setText(String.format(mContext.getString(R.string.label_celcius), MainHelper.getDecimalFormat(item.getMain().getTemp())));
        holder.tvInFarenheight.setText(String.format(mContext.getString(R.string.label_farengeight), MainHelper.getDecimalFormat(MainHelper.Celcius_to_Farenheight(item.getMain().getTemp()))));
        holder.tvWeatherDescription.setText(item.getWeather().get(0).getMain());
        holder.tvLocation.setText(item.getName() + ", " + item.getSys().getCountry());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WeatherDetailActivity.class);
                intent.putExtra("WeatherDetail", MainHelper.getGson().toJson(item));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mWeatherDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgIcon;
        public TextView tvInCelcius, tvInFarenheight, tvWeatherDescription, tvLocation;

        public ViewHolder(View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            tvInCelcius = itemView.findViewById(R.id.tvInCelcius);
            tvInFarenheight = itemView.findViewById(R.id.tvInFarenheight);
            tvWeatherDescription = itemView.findViewById(R.id.tvWeatherDescription);
            tvLocation = itemView.findViewById(R.id.tvLocation);
        }
    }
}
