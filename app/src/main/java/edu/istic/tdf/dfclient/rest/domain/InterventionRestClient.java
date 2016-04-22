package edu.istic.tdf.dfclient.rest.domain;

import edu.istic.tdf.dfclient.domain.intervention.Intervention;
import edu.istic.tdf.dfclient.rest.IRestClient;
import edu.istic.tdf.dfclient.rest.RestClient;

/**
 * Created by maxime on 20/04/2016.
 */
public class InterventionRestClient extends RestClient<Intervention> implements IRestClient<Intervention> {

    public InterventionRestClient() {
        super(Intervention.class);
    }

    @Override
    public String getRestEndpoint() {
        return "interventions/";
    }
}
