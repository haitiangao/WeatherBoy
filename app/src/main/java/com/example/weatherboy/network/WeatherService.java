package com.example.weatherboy.network;


import com.example.weatherboy.model.Library;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.example.weatherboy.util.Constants.GET_URL_POSTFIX;


public interface WeatherService {

    //api.openweathermap.org/data/2.5/weather?q={city name}&appid={your api key}
    @GET(GET_URL_POSTFIX)
    Observable<Library> getWeatherRx(@Query("q") String query, @Query("appid") String apiKey);

}
