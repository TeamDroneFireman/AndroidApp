package edu.istic.tdf.dfclient.repository.domain.element;


import edu.istic.tdf.dfclient.domain.element.mean.drone.Drone;
import edu.istic.tdf.dfclient.domain.element.mean.interventionMean.InterventionMean;
import edu.istic.tdf.dfclient.repository.IRepository;
import edu.istic.tdf.dfclient.repository.Repository;

/**
 * Intervention repository
 */
public class DroneRepository extends Repository<Drone> implements IRepository<Drone> {
    public DroneRepository() {
        super(Drone.class);
    }
}
