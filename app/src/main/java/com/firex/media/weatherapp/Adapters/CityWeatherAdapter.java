package com.firex.media.weatherapp.Adapters;

import android.content.Context;
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
import com.firex.media.weatherapp.Models.Constans;
import com.firex.media.weatherapp.Models.WeatherDetails;
import com.firex.media.weatherapp.R;
import com.firex.media.weatherapp.Utils.MainHelper;
import com.firex.media.weatherapp.Utils.MyVolleySingleton;

import java.util.List;

public class CityWeatherAdapter extends RecyclerView.Adapter<CityWeatherAdapter.ViewHolder> {

    private Context mContext;
    private List<WeatherDetails> mWeatherDetails;

    public CityWeatherAdapter(Context context, List<WeatherDetails> weatherDetails) {
        this.mContext = context;
        this.mWeatherDetails = weatherDetails;
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
        String icon_url = String.format(mContext.getString(R.string.api_weather_icon), item.getWeather().get(0).getIcon());
        ImageRequest iconRequest = new ImageRequest(icon_url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        holder.imgIcon.setImageBitmap(response);
                    }
                },0, 0, ImageView.ScaleType.CENTER, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        holder.imgIcon.setImageDrawable(mContext.getResources().getDrawable(android.R.drawable.stat_sys_warning));
                    }
                });
        MyVolleySingleton.getInstance(mContext).addToRequestQueue(iconRequest);

        holder.tvInCelcius.setText(String.format(mContext.getString(R.string.label_celcius), item.getMain().getTemp()));
        holder.tvInFarenheight.setText(String.format(mContext.getString(R.string.label_farengeight), MainHelper.Celcius_to_Farenheight(item.getMain().getTemp())));
        holder.tvWeatherDescription.setText(item.getWeather().get(0).getMain());
        holder.tvLocation.setText(item.getName() + ", " + item.getSys().getCountry());
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
