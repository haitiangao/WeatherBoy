package com.example.weatherboy.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;

import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.weatherboy.R;
import com.example.weatherboy.adapter.WeatherAdapter;
import com.example.weatherboy.model.Library;
import com.example.weatherboy.model.Weather;
import com.example.weatherboy.util.Constants;
import com.example.weatherboy.util.DebugLogger;
import com.example.weatherboy.viewmodel.WeatherViewModel;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;
import kotlin.jvm.internal.Intrinsics;

import static com.example.weatherboy.util.Constants.SEARCHHINT;


/*
HomeWork Week 7 Weekend
Create an application of your choice. Application can be developed in Java or in Kotlin, MVVM architecture, should use either RxJava or LiveData.

+ No raw strings
+ BroadcastReceiver
+ MVVM Architecture
+ RxJAva/LiveData/Coroutines
+Glide/Picasso
+ REST API of your choice
+Multithreading
+Clean code
+ SOLID principles
+ Presentable UI
+ Dagger2, Retrofit
+ Room DB
+ Runtime Permissions
+ DataBindning
+ Firebase Realtime DB, Storage etc
 */


public class MainActivity extends AppCompatActivity implements LocationListener {
    private WeatherViewModel viewModel;
    private int REQUEST_CODE = 707;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Handler timeHandler;
    private LocationManager locationManager;
    private List<Library> initialList = new ArrayList<>();
    private List<Library> currentList = new ArrayList<>();

    @BindView(R.id.dateView)
    TextView dateView;
    @BindView(R.id.temperatureView)
    TextView temperatureView;
    @BindView(R.id.feelsLikeView)
    TextView feelsLikeView;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.weatherView)
    TextView weatherView;
    @BindView(R.id.weatherSearchView)
    SearchView weatherSearcher;
    @BindView(R.id.weatherFavRecycle)
    RecyclerView favWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        viewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

        weatherSearcher.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                doMySearch(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, RecyclerView.VERTICAL);
        favWeather.setLayoutManager(new LinearLayoutManager(this));
        favWeather.setAdapter(new WeatherAdapter(initialList,this));
        favWeather.addItemDecoration(itemDecoration);

        handleTime();
        delayPopulate();
    }

    private void delayPopulate(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                populateRecycler();
            }

        }, 2000);
    }

    private void doMySearch(String query) {
        DebugLogger.logDebug("Searching");
        compositeDisposable.add(viewModel.getWeatherRx(query).subscribe(result->{
            String weatherString="";
            for (Weather weather: result.getWeather()){
                weatherString = weather.getDescription()+", "+weatherString;
            }
            weatherString=weatherString.substring(0,weatherString.length()-2);

            weatherView.setText(weatherString);
            String weatherImageUri = "https://openweathermap.org/img/wn/"
                    +result.getWeather().get(0).getIcon()+"@2x.png";
            DebugLogger.logDebug("weather: "+ weatherImageUri);

            Glide.with(this)
                    .asBitmap()
                    .load(weatherImageUri)
                    .placeholder(R.drawable.ic_broken_image_black_24dp)
                    .into(imageView);

            temperatureView.setText(String.format("%.2f°C",result.getMain().getTemp()-273.15));
            feelsLikeView.setText(String.format("Feels like %.2f°C",result.getMain().getFeelsLike()-273.15));

        },throwable -> {
            DebugLogger.logError(throwable);

        }));
    }


    private void handleTime(){
        timeHandler = new Handler(getMainLooper());
        timeHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dateView.setText(new SimpleDateFormat("HH:mm", Locale.US).format(new Date()));
                timeHandler.postDelayed(this, 1000);
            }
        }, 10);

    }

    private void populateRecycler(){
        compositeDisposable.add(viewModel.getFavLocations().subscribe(locations->{

            DebugLogger.logDebug("Locations:" +locations.size());
            currentList.clear();
            getAllWeather(locations);
            delayRecycle();

        },throwable -> {
            DebugLogger.logError(throwable);

        }));
    }

    private void delayRecycle(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                WeatherAdapter recycleAdaptor = new WeatherAdapter(currentList,getBaseContext());
                favWeather.setAdapter(null);
                favWeather.setAdapter(recycleAdaptor);
                recycleAdaptor.notifyDataSetChanged();
            }
        }, 1000);
    }

    private void getAllWeather(List<String> queryList){
        for(String query:queryList) {
            compositeDisposable.add(viewModel.getWeatherRx(query).subscribe(locations -> {

            currentList.add(locations);
            }, throwable -> {
                DebugLogger.logError(throwable);

            }));
        }

    }

    @OnClick(R.id.favouriteLocationButton)
    public void createNewFavourite(){
        String location =weatherSearcher.getQuery().toString();
        viewModel.sendNewLocation(location);
        populateRecycler();
    }



    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this,
                Constants.LOCATION_PERMISSION) != 0) {
            this.requestPermissions();
        }
        else{
            setUpLocation();
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Constants.LOCATION_PERMISSION}, this.REQUEST_CODE);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions,
                                           @NonNull @NotNull int[] grantResults) {
        Intrinsics.checkParameterIsNotNull(permissions, "permissions");
        Intrinsics.checkParameterIsNotNull(grantResults, "grantResults");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == this.REQUEST_CODE && Intrinsics.areEqual(permissions[0],
                Constants.LOCATION_PERMISSION) && grantResults[0] != 0
                && ActivityCompat.shouldShowRequestPermissionRationale(this,
                Constants.LOCATION_PERMISSION)) {
            this.requestPermissions();
        }
        else{
            setUpLocation();
        }

    }

    @SuppressLint("MissingPermission")
    private void setUpLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        assert locationManager != null;
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,this,null);

    }


    @Override
    public void onLocationChanged(Location location) {
        DebugLogger.logDebug("changed locations");
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
            DebugLogger.logDebug("inside try: "+addresses.get(0).getLocality());

            String cityName = addresses.get(0).getLocality();

            weatherSearcher.setQuery(cityName,false);
            weatherSearcher.setQueryHint(SEARCHHINT);
            weatherSearcher.clearFocus();
            doMySearch(cityName);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        timeHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleTime();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeHandler.removeCallbacksAndMessages(null);
        compositeDisposable.dispose();
    }
}
