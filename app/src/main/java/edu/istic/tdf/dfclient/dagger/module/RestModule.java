package edu.istic.tdf.dfclient.dagger.module;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import edu.istic.tdf.dfclient.auth.AuthHelper;
import edu.istic.tdf.dfclient.http.TdfHttpClient;
import edu.istic.tdf.dfclient.http.TdfHttpClientPort12345;
import edu.istic.tdf.dfclient.http.TdfHttpClientPort12346;
import edu.istic.tdf.dfclient.rest.serializer.RestSerializerBuilder;
import edu.istic.tdf.dfclient.rest.service.login.LoginRestService;
import okhttp3.OkHttpClient;

/**
 * Created by maxime on 24/04/2016.
 *
 * Dagger modules for injecting REST services with their dependencies
 */
@Module
public class RestModule {

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder().build();
    }

    @Provides
    @Singleton
    Gson provideSerializer() {
        return RestSerializerBuilder.build();
    }

    @Provides
    @Singleton
    AuthHelper provideAuthHelper() {
        return new AuthHelper();
    }

    @Provides
    @Singleton
    TdfHttpClientPort12345 provideTdfHttpClient12345(OkHttpClient client) {
        return new TdfHttpClientPort12345(client);
    }

    @Provides
    @Singleton
    TdfHttpClientPort12346 provideTdfHttpClient12346(OkHttpClient client) {
        return new TdfHttpClientPort12346(client);
    }

    @Provides
    @Singleton
    LoginRestService provideLoginRestService(TdfHttpClientPort12346 httpClient, Gson serializer) {
        return new LoginRestService(httpClient, serializer);
    }

}