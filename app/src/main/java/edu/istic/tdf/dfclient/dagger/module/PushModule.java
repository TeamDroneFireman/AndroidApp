package edu.istic.tdf.dfclient.dagger.module;

import dagger.Module;
import dagger.Provides;
import edu.istic.tdf.dfclient.push.PushHandler;

/**
 * Created by maxime on 24/04/2016.
 *
 * Dagger modules for injecting REST services with their dependencies
 */
@Module
public class PushModule {

    @Provides
    //@Singleton
    PushHandler providePushHandler() {
        return new PushHandler();
    }

}