package edu.istic.tdf.dfclient.rest.domain.element;

import edu.istic.tdf.dfclient.domain.element.mean.interventionMean.InterventionMean;
import edu.istic.tdf.dfclient.http.TdfHttpClient;
import edu.istic.tdf.dfclient.http.configuration.TdfHttpClientConf;
import edu.istic.tdf.dfclient.rest.IRestClient;
import edu.istic.tdf.dfclient.rest.RestEndpoints;

/**
 * The REST client for InterventionMeans
 */
public class InterventionMeanRestClient extends ElementRestClient<InterventionMean> implements IRestClient<InterventionMean> {

    public InterventionMeanRestClient(TdfHttpClient httpClient) {
        super(InterventionMean.class, httpClient);
        httpClient.setConf(new TdfHttpClientConf(RestEndpoints.Mean)); // Hack because no load balancer
    }
}
