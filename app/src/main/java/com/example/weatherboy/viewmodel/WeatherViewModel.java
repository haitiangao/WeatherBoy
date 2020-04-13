package com.example.weatherboy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.weatherboy.model.Library;
import com.example.weatherboy.network.WeatherRetrofitService;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WeatherViewModel extends AndroidViewModel {
    WeatherRetrofitService weatherRetrofitService;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        weatherRetrofitService= new WeatherRetrofitService();
    }

    public Observable<Library> getWeatherRx(String query) {
        return  weatherRetrofitService
                .getWeatherRx(query)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}
