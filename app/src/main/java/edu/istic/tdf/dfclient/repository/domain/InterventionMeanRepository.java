package edu.istic.tdf.dfclient.repository.domain;


import edu.istic.tdf.dfclient.domain.element.mean.interventionMean.InterventionMean;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;
import edu.istic.tdf.dfclient.repository.IRepository;
import edu.istic.tdf.dfclient.repository.Repository;

/**
 * Intervention repository
 */
public class InterventionMeanRepository extends Repository<InterventionMean> implements IRepository<InterventionMean> {
    public InterventionMeanRepository() {
        super(InterventionMean.class);
    }
}
