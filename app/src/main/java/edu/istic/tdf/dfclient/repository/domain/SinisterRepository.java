package edu.istic.tdf.dfclient.repository.domain;

import edu.istic.tdf.dfclient.domain.sinister.Sinister;
import edu.istic.tdf.dfclient.repository.IRepository;
import edu.istic.tdf.dfclient.repository.Repository;

/**
 * Created by tremo on 27/04/16.
 */
public class SinisterRepository extends Repository<Sinister> implements IRepository<Sinister> {
    public SinisterRepository() {
        super(Sinister.class);
    }
}
