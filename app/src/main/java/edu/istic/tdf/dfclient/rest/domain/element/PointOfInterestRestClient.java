package edu.istic.tdf.dfclient.rest.domain.element;

import edu.istic.tdf.dfclient.domain.element.pointOfInterest.PointOfInterest;
import edu.istic.tdf.dfclient.http.TdfHttpClient;
import edu.istic.tdf.dfclient.http.configuration.TdfHttpClientConf;
import edu.istic.tdf.dfclient.rest.IRestClient;
import edu.istic.tdf.dfclient.rest.RestEndpoints;


/**
 * The REST client for Points of Interest
 */
public class PointOfInterestRestClient extends ElementRestClient<PointOfInterest> implements IRestClient<PointOfInterest> {

    public PointOfInterestRestClient(TdfHttpClient httpClient) {
        super(PointOfInterest.class, httpClient);
        httpClient.setConf(new TdfHttpClientConf(RestEndpoints.Sig)); // Hack because no load balancer
    }
}
