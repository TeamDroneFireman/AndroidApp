package edu.istic.tdf.dfclient.rest.domain;

import edu.istic.tdf.dfclient.domain.sinister.Sinister;
import edu.istic.tdf.dfclient.rest.IRestClient;
import edu.istic.tdf.dfclient.rest.RestClient;

/**
 * Created by maxime on 20/04/2016.
 */
public class SinisterRestClient extends RestClient<Sinister> implements IRestClient<Sinister> {

    public SinisterRestClient() {
        super(Sinister.class);
    }

    @Override
    public String getRestEndpoint() {
        return "VDLeBzjP/";
    }
}
