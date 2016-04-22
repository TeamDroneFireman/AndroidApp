package edu.istic.tdf.dfclient;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by maxime on 21/04/2016.
 */
public class TdfApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
    }
}
