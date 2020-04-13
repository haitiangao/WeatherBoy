package com.example.weatherboy.util;

import android.util.Log;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.weatherboy.util.Constants.ERROR_PREFIX;


public class DebugLogger {

    public static void logError(Throwable throwable) {
        Log.d(TAG, ERROR_PREFIX + throwable.getLocalizedMessage());
    }

    public static void logDebug(String message) {
        Log.d(TAG, message);
    }
}
