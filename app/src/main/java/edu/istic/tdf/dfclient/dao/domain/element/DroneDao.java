package edu.istic.tdf.dfclient.dao.domain.element;

import edu.istic.tdf.dfclient.dao.IDao;
import edu.istic.tdf.dfclient.domain.element.mean.drone.Drone;
import edu.istic.tdf.dfclient.domain.element.mean.drone.mission.Mission;
import edu.istic.tdf.dfclient.http.TdfHttpClient;
import edu.istic.tdf.dfclient.repository.domain.element.DroneRepository;
import edu.istic.tdf.dfclient.rest.domain.element.DroneRestClient;
import okhttp3.Callback;

/**
 * The DAO for Drones
 *
 * {@inheritDoc}
 */
public class DroneDao extends ElementDao<Drone, DroneRepository, DroneRestClient>
        implements IDao<Drone, DroneRepository, DroneRestClient> {

    public DroneDao(TdfHttpClient httpClient) {
        super(new DroneRepository(), new DroneRestClient(httpClient));
    }

    public void startMission(Drone drone, Callback callback){
       this.restClient.startMission(drone, callback);
    }
}