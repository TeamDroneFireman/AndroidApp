package edu.istic.tdf.dfclient.repository.domain.element;


import edu.istic.tdf.dfclient.domain.element.mean.drone.Drone;
import edu.istic.tdf.dfclient.domain.element.pointOfInterest.PointOfInterest;
import edu.istic.tdf.dfclient.repository.IRepository;
import edu.istic.tdf.dfclient.repository.Repository;

/**
 * Intervention repository
 */
public class PointOfInterestRepository extends Repository<PointOfInterest> implements IRepository<PointOfInterest> {
    public PointOfInterestRepository() {
        super(PointOfInterest.class);
    }
}
