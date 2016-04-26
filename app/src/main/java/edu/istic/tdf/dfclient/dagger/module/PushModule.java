package edu.istic.tdf.dfclient.dagger.module;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import edu.istic.tdf.dfclient.http.TdfHttpClientPort12345;
import edu.istic.tdf.dfclient.http.TdfHttpClientPort12346;
import edu.istic.tdf.dfclient.push.PushHandler;
import edu.istic.tdf.dfclient.rest.serializer.RestSerializerBuilder;
import edu.istic.tdf.dfclient.rest.service.login.LoginRestService;
import okhttp3.OkHttpClient;

/**
 * Created by maxime on 24/04/2016.
 *
 * Dagger modules for injecting REST services with their dependencies
 */
@Module
public class PushModule {

    @Provides
    @Singleton
    PushHandler providePushHandler() {
        return new PushHandler();
    }

}