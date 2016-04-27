package edu.istic.tdf.dfclient.rest.domain.element;

import edu.istic.tdf.dfclient.domain.element.mean.drone.Drone;
import edu.istic.tdf.dfclient.domain.element.pointOfInterest.PointOfInterest;
import edu.istic.tdf.dfclient.http.TdfHttpClient;
import edu.istic.tdf.dfclient.http.configuration.TdfHttpClientConfIntervention;
import edu.istic.tdf.dfclient.rest.IRestClient;
import edu.istic.tdf.dfclient.rest.RestClient;


public class PointOfInterestRestClient extends RestClient<PointOfInterest> implements IRestClient<PointOfInterest> {

    public PointOfInterestRestClient(TdfHttpClient httpClient) {
        super(PointOfInterest.class, httpClient);
        httpClient.setConf(new TdfHttpClientConfIntervention()); // Hack because no load balancer
    }

    @Override
    public String getRestEndpoint() {
        return "pointsofinterest/";
    }
}
