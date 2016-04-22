package edu.istic.tdf.dfclient.repository.domain;


import edu.istic.tdf.dfclient.domain.sinister.Sinister;
import edu.istic.tdf.dfclient.repository.IRepository;
import edu.istic.tdf.dfclient.repository.Repository;

/**
 * Sinister repository
 */
public class SinisterRepository extends Repository<Sinister> implements IRepository<Sinister> {
    public SinisterRepository() {
        super(Sinister.class);
    }
}
