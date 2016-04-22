package edu.istic.tdf.dfclient.rest;

import java.util.List;

import dagger.Provides;
import edu.istic.tdf.dfclient.domain.Sinister;

/**
 * Created by maxime on 20/04/2016.
 */
public class SinisterRestClient extends RestClient<Sinister> implements ISinisterRestClient {

    public SinisterRestClient() {
        super(Sinister.class);
    }

    @Override
    public String getRestEndpoint() {
        return "VDLeBzjP/";
    }
}
