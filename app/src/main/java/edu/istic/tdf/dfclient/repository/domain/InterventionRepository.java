package edu.istic.tdf.dfclient.repository.domain;


import edu.istic.tdf.dfclient.domain.intervention.Intervention;
import edu.istic.tdf.dfclient.repository.IRepository;
import edu.istic.tdf.dfclient.repository.Repository;

/**
 * Intervention repository
 */
public class InterventionRepository extends Repository<Intervention> implements IRepository<Intervention> {
    public InterventionRepository() {
        super(Intervention.class);
    }
}
