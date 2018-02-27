package com.example.jgchan.datemypet;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by jgchan on 26/02/18.
 */

public class MyApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);
    }
}
