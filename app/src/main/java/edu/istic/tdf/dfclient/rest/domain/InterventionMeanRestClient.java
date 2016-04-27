package edu.istic.tdf.dfclient.rest.domain;

import edu.istic.tdf.dfclient.domain.element.mean.interventionMean.InterventionMean;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;
import edu.istic.tdf.dfclient.http.TdfHttpClient;
import edu.istic.tdf.dfclient.rest.IRestClient;
import edu.istic.tdf.dfclient.rest.RestClient;


public class InterventionMeanRestClient extends RestClient<InterventionMean> implements IRestClient<InterventionMean> {

    public InterventionMeanRestClient(TdfHttpClient httpClient) {
        super(InterventionMean.class, httpClient);
    }

    @Override
    public String getRestEndpoint() {
        return "interventionmeans/";
    }
}
