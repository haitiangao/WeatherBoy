package com.example.weatherboy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.weatherboy.firebase.FirebaseEvents;
import com.example.weatherboy.model.Library;
import com.example.weatherboy.network.WeatherRetrofitService;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WeatherViewModel extends AndroidViewModel {
    private WeatherRetrofitService weatherRetrofitService;
    private FirebaseEvents firebaseEvent;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        weatherRetrofitService= new WeatherRetrofitService();
        firebaseEvent = new FirebaseEvents();

    }

    public Observable<Library> getWeatherRx(String query) {
        return  weatherRetrofitService
                .getWeatherRx(query)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<String>> getFavLocations() {
        return firebaseEvent.getLocations()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }



    public void sendNewLocation(String location) {
        firebaseEvent.sendNewLocation(location);
    }

}
