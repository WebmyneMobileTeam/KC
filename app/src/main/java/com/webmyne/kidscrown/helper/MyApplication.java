package com.webmyne.kidscrown.helper;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;


import com.facebook.stetho.Stetho;
import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Application class that called once when application is installed for the first time on device.
 * This class includes the integration of Volly [third party framework for calling webservices]
 */
public class MyApplication extends MultiDexApplication {

    private static Retrofit retrofit;
    private static Gson gson;

    /**
     * Log or request TAG
     */
    public static final String TAG = "VolleyPatterns";

    /**
     * A singleton instance of the application class for easy access in other places
     */
    private static MyApplication sInstance;
    /**
     * A class that helps to store database file from assets to
     */
    static int halfscreenSize;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);

        // initialize the singleton
        sInstance = this;
        initGson();
        initRetrofit();
        initStetho();
        DatabaseHandler handler = new DatabaseHandler(getApplicationContext());
        try {
            handler.createDataBase();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initGson() {
        gson = new Gson();
    }

    public static Gson getGson() {
        return gson;
    }

    private void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(URLConstants.BASE_URL_V03)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void initStetho() {
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    /**
     * @return ApplicationController singleton instance
     */
    public static synchronized MyApplication getInstance() {
        return sInstance;
    }

}
