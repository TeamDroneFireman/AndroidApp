package edu.istic.tdf.dfclient.dagger.module;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import edu.istic.tdf.dfclient.dao.domain.InterventionDao;
import edu.istic.tdf.dfclient.dao.domain.InterventionMeanDao;
import edu.istic.tdf.dfclient.http.TdfHttpClient;
import edu.istic.tdf.dfclient.http.TdfHttpClientPort12345;

/**
 * Created by maxime on 24/04/2016.
 *
 * Dagger modules for injecting DAOs with their dependencies
 */
@Module
public class DaoModule {
    public DaoModule() {
    }

    @Provides
    //@Singleton
    InterventionDao provideInterventionDao(TdfHttpClientPort12345 httpClient){
        return new InterventionDao(httpClient);
    }

    @Provides
    InterventionMeanDao provideInterventionMeanDao(TdfHttpClientPort12345 httpClient) {
        return new InterventionMeanDao(httpClient);
    }
}
