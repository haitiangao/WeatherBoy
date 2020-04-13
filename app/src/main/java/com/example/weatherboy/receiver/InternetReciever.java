package com.example.weatherboy.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.weatherboy.util.Constants;
import com.example.weatherboy.util.DebugLogger;




public class InternetReciever extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(Constants.PREFERENCE_KEY,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (checkInternet(context)){
            DebugLogger.logDebug("Internet is Connected");
            editor.putString(Constants.PREFERENCE_KEY, Constants.CONNECTED);
            editor.apply();
        }
        else{
            DebugLogger.logDebug("Internet is Disconnected");
            editor.putString(Constants.PREFERENCE_KEY, Constants.DISCONNECTED);
            editor.commit();
        }

    }

    private boolean checkInternet(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

}