package edu.istic.tdf.dfclient.rest.domain.element;

import android.util.Log;

import edu.istic.tdf.dfclient.domain.element.mean.drone.Drone;
import edu.istic.tdf.dfclient.http.TdfHttpClient;
import edu.istic.tdf.dfclient.http.configuration.TdfHttpClientConf;
import edu.istic.tdf.dfclient.rest.IRestClient;
import edu.istic.tdf.dfclient.rest.RestEndpoints;


public class DroneRestClient extends ElementRestClient<Drone> implements IRestClient<Drone> {

    public DroneRestClient(TdfHttpClient httpClient) {
        super(Drone.class, httpClient);
        httpClient.setConf(new TdfHttpClientConf(RestEndpoints.Drone)); // Hack because no load balancer
    }
}
