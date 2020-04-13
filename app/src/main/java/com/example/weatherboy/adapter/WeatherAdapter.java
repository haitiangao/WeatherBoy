package com.example.weatherboy.adapter;

import android.content.Context;
import android.os.Debug;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weatherboy.R;
import com.example.weatherboy.model.Library;
import com.example.weatherboy.model.Weather;
import com.example.weatherboy.util.DebugLogger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>  {
    private List<Library> locations;
    private Context context;

    public WeatherAdapter(List<Library> locations, Context context) {
        this.locations = locations;
        this.context = context;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favourite_location_item,parent,false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        DebugLogger.logDebug("Binding view holder");
        String weatherString="";
        holder.locationName.setText(locations.get(position).getName());
        Library result = locations.get(position);
        for (Weather weather: result.getWeather()){
            weatherString = weather.getDescription()+", "+weatherString;
        }
        weatherString=weatherString.substring(0,weatherString.length()-2);

        holder.weatherView.setText(weatherString);
        String weatherImageUri = "https://openweathermap.org/img/wn/"
                +result.getWeather().get(0).getIcon()+"@2x.png";
        DebugLogger.logDebug("weather: "+ weatherImageUri);

        Glide.with(context)
                .asBitmap()
                .load(weatherImageUri)
                .placeholder(R.drawable.ic_broken_image_black_24dp)
                .into(holder.imageView);

        holder.temperatureView.setText(String.format("%.2f°C",result.getMain().getTemp()-273.15));
        holder.feelsLikeView.setText(String.format("Feels like %.2f°C",result.getMain().getFeelsLike()-273.15));

    }

    @Override
    public int getItemCount() {
        return locations.size();
    }




    public class WeatherViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.locationName)
        TextView locationName;
        @BindView(R.id.temperatureView2)
        TextView temperatureView;
        @BindView(R.id.feelsLikeView2)
        TextView feelsLikeView;
        @BindView(R.id.imageView2)
        ImageView imageView;
        @BindView(R.id.weatherView2)
        TextView weatherView;


        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
