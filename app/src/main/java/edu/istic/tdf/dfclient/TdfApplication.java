package edu.istic.tdf.dfclient;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.raizlabs.android.dbflow.config.FlowManager;

import edu.istic.tdf.dfclient.dagger.component.ApplicationComponent;
import edu.istic.tdf.dfclient.dagger.component.DaggerApplicationComponent;
import edu.istic.tdf.dfclient.dagger.module.ActivitiesModule;
import edu.istic.tdf.dfclient.dagger.module.DaoModule;
import edu.istic.tdf.dfclient.dagger.module.FragmentsModule;
import edu.istic.tdf.dfclient.dagger.module.RestModule;
import eu.inloop.easygcm.GcmListener;

/**
 * Created by maxime on 21/04/2016.
 */
public class TdfApplication extends Application implements GcmListener {

    private static final String TAG = "App";

    /**
     * Dagger component
     */
    private ApplicationComponent applicationComponent;

    /**
     * Application context
     */
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        // Dagger
        initializeInjector();

        // ORM
        FlowManager.init(this);

        // Context hack
        TdfApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return TdfApplication.context;
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    /**
     * Initializes dagger loading the right modules
     */
    private void initializeInjector() {
        applicationComponent = DaggerApplicationComponent.builder()
                .daoModule(new DaoModule())
                .restModule(new RestModule())
                .fragmentsModule(new FragmentsModule())
                .activitiesModule(new ActivitiesModule())
                .build();
    }

    @Override
    public void onMessage(String from, Bundle data) {
        Log.v(TAG, "### message from: " + from);
        Log.v(TAG, "### data: " + from);
        for (String key : data.keySet()) {
            Log.v(TAG, "> " + key + ": " + data.get(key));
        }
    }

    @Override
    public void sendRegistrationIdToBackend(String registrationId) {
        Log.v(TAG, "### registration id: " + registrationId);
    }
}
