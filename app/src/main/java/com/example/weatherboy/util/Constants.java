package com.example.weatherboy.util;

public class Constants {

    //Error Messages
    static final String TAG = "TAG_H";
    static final String ERROR_PREFIX = "Error: ";
    public static final String RESULTS_NULL = "Results were null";

    //Retrofit call
    //api.openweathermap.org/data/2.5/weather?q={city name}&appid={your api key}
    public static final String API_KEY = "f27285f4f22eb7180ee715ae1b73a990";
    public static final String GET_URL_POSTFIX = "/data/2.5/weather"; //
    public static final String BASE_URL = "https://api.openweathermap.org/";
    public static final String PREFERENCE_KEY= "PREFERENCE_KEY";
    public static final String CONNECTED = "Connected";
    public static final String DISCONNECTED = "Disconnected";
    public static final String SEARCHHINT ="Search places";

    public static final String LOCATION_PERMISSION = "android.permission.ACCESS_FINE_LOCATION";
}
