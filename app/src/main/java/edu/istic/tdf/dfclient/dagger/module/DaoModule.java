package edu.istic.tdf.dfclient.dagger.module;

import dagger.Module;
import dagger.Provides;
import edu.istic.tdf.dfclient.dao.domain.InterventionDao;
import edu.istic.tdf.dfclient.dao.domain.SinisterDao;
import edu.istic.tdf.dfclient.dao.domain.element.DroneDao;
import edu.istic.tdf.dfclient.dao.domain.element.InterventionMeanDao;
import edu.istic.tdf.dfclient.dao.domain.element.PointOfInterestDao;
import edu.istic.tdf.dfclient.http.TdfHttpClient;

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
    InterventionDao provideInterventionDao(TdfHttpClient httpClient){
        return new InterventionDao(httpClient);
    }

    @Provides
    InterventionMeanDao provideInterventionMeanDao(TdfHttpClient httpClient) {
        return new InterventionMeanDao(httpClient);
    }

    @Provides
    DroneDao provideDroneDao(TdfHttpClient httpClient) {
        return new DroneDao(httpClient);
    }

    @Provides
    PointOfInterestDao providePointOfInterestDao(TdfHttpClient httpClient) {
        return new PointOfInterestDao(httpClient);
    }

    @Provides
    SinisterDao provideSinisterDao(TdfHttpClient httpClient) {
        return new SinisterDao(httpClient);
    }
}
