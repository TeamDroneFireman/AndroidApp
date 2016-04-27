package edu.istic.tdf.dfclient.rest.domain.element;

import edu.istic.tdf.dfclient.domain.element.mean.drone.Drone;
import edu.istic.tdf.dfclient.domain.element.mean.interventionMean.InterventionMean;
import edu.istic.tdf.dfclient.http.TdfHttpClient;
import edu.istic.tdf.dfclient.http.configuration.TdfHttpClientConfDrone;
import edu.istic.tdf.dfclient.http.configuration.TdfHttpClientConfIntervention;
import edu.istic.tdf.dfclient.rest.IRestClient;
import edu.istic.tdf.dfclient.rest.RestClient;


public class DroneRestClient extends RestClient<Drone> implements IRestClient<Drone> {

    public DroneRestClient(TdfHttpClient httpClient) {
        super(Drone.class, httpClient);
        httpClient.setConf(new TdfHttpClientConfDrone()); // Hack because no load balancer

    }

    @Override
    public String getRestEndpoint() {
        return "drones/";
    }
}
