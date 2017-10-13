package com.galihpw.bakingapp;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by hidayatasep43 on 8/13/2017.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

    }

}
