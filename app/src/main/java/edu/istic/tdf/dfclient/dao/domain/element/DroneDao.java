package edu.istic.tdf.dfclient.dao.domain.element;

import edu.istic.tdf.dfclient.dao.IDao;
import edu.istic.tdf.dfclient.domain.element.mean.drone.Drone;
import edu.istic.tdf.dfclient.http.TdfHttpClient;
import edu.istic.tdf.dfclient.repository.domain.element.DroneRepository;
import edu.istic.tdf.dfclient.rest.domain.element.DroneRestClient;

/**
 * The DAO for Sinister
 *
 * {@inheritDoc}
 */
public class DroneDao extends ElementDao<Drone, DroneRepository, DroneRestClient>
        implements IDao<Drone, DroneRepository, DroneRestClient> {

    public DroneDao(TdfHttpClient httpClient) {
        super(new DroneRepository(), new DroneRestClient(httpClient));
    }
}