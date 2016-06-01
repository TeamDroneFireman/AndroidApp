package edu.istic.tdf.dfclient.rest.domain.element;

import android.util.Log;

import java.io.IOException;
import java.net.URL;

import edu.istic.tdf.dfclient.domain.element.mean.drone.Drone;
import edu.istic.tdf.dfclient.domain.element.mean.drone.mission.Mission;
import edu.istic.tdf.dfclient.http.TdfHttpClient;
import edu.istic.tdf.dfclient.http.configuration.TdfHttpClientConf;
import edu.istic.tdf.dfclient.rest.IRestClient;
import edu.istic.tdf.dfclient.rest.RestEndpoints;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * The REST client for Drones
 */
public class DroneRestClient extends ElementRestClient<Drone> implements IRestClient<Drone> {

    /**
     * The Push subscription endpoint URL
     */

    private final static String MISSION_URL = "/mission";
    protected static final String TAG = "DroneRestClient";

    public DroneRestClient(TdfHttpClient httpClient) {
        super(Drone.class, httpClient);
        httpClient.setConf(new TdfHttpClientConf(RestEndpoints.Drone)); // Hack because no load balancer
    }

    public void startMission(final Drone drone, Callback callback) {
        final String jsonMission = this.serializer.toJson(drone.getMission());
        this.httpClient.put(
                drone.getId() + MISSION_URL,
                jsonMission,
                callback
        );
    }

}
