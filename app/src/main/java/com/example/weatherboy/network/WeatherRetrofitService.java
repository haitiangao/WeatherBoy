package com.example.weatherboy.network;


import com.example.weatherboy.model.Library;
import com.example.weatherboy.util.Constants;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.weatherboy.util.Constants.API_KEY;


public class WeatherRetrofitService {
    private WeatherService weatherService;
    private OkHttpClient client;

    public WeatherRetrofitService(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        weatherService = createWeatherService(getRetrofitInstance());
    }


    private Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private WeatherService createWeatherService(Retrofit retrofitInstance) {
        return retrofitInstance.create(WeatherService.class);
    }

    public Observable<Library> getWeatherRx(String query) {
        return weatherService.getWeatherRx(query, API_KEY);
    }



}
