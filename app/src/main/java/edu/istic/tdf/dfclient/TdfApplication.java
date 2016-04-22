package edu.istic.tdf.dfclient;

import android.app.Application;
import android.content.Context;

import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by maxime on 21/04/2016.
 */
public class TdfApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);

        TdfApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return TdfApplication.context;
    }
}
