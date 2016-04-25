package edu.istic.tdf.dfclient;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.raizlabs.android.dbflow.config.FlowManager;

import edu.istic.tdf.dfclient.dagger.component.ApplicationComponent;
import edu.istic.tdf.dfclient.dagger.component.DaggerApplicationComponent;
import edu.istic.tdf.dfclient.dagger.module.ActivitiesModule;
import edu.istic.tdf.dfclient.dagger.module.DaoModule;
import edu.istic.tdf.dfclient.dagger.module.FragmentsModule;
import edu.istic.tdf.dfclient.dagger.module.RestModule;
import edu.istic.tdf.dfclient.push.PushListener;
import eu.inloop.easygcm.EasyGcm;
import eu.inloop.easygcm.GcmListener;

/**
 * Created by maxime on 21/04/2016.
 */
public class TdfApplication extends Application {

    /**
     * Dagger component
     */
    private ApplicationComponent applicationComponent;

    /**
     * Push listener
     */
    private PushListener pushListener;

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

        // Push
        pushListener = new PushListener();
        EasyGcm.setGcmListener(pushListener);
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
}
