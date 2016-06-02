package edu.istic.tdf.dfclient.dagger.module;

import com.google.gson.Gson;

import dagger.Module;
import dagger.Provides;
import edu.istic.tdf.dfclient.auth.AuthHelper;
import edu.istic.tdf.dfclient.http.TdfHttpClient;
import edu.istic.tdf.dfclient.rest.serializer.RestSerializerBuilder;
import edu.istic.tdf.dfclient.rest.service.login.LoginRestService;
import edu.istic.tdf.dfclient.rest.service.logout.LogoutRestService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by maxime on 24/04/2016.
 *
 * Dagger modules for injecting REST services with their dependencies
 */
@Module
public class RestModule
{
    @Provides
    //@Singleton
    OkHttpClient provideOkHttpClient()
    {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
    }

    @Provides
    //@Singleton
    Gson provideSerializer()
    {
        return RestSerializerBuilder.build();
    }

    @Provides
    //@Singleton
    AuthHelper provideAuthHelper()
    {
        return new AuthHelper();
    }

    @Provides
    //@Singleton
    TdfHttpClient provideTdfHttpClient(OkHttpClient client)
    {
        return new TdfHttpClient(client);
    }

    @Provides
    //@Singleton
    LoginRestService provideLoginRestService(TdfHttpClient httpClient, Gson serializer)
    {
        return new LoginRestService(httpClient, serializer);
    }

    @Provides
    //@Singleton
    LogoutRestService provideLogoutRestService(TdfHttpClient httpClient, Gson serializer)
    {
        return new LogoutRestService(httpClient);
    }
}