package edu.istic.tdf.dfclient.repository;


import edu.istic.tdf.dfclient.domain.sinister.Sinister;

/**
 * Created by maxime on 21/04/2016.
 */
public class SinisterRepository extends Repository<Sinister> implements IRepository<Sinister> {
    public SinisterRepository() {
        super(Sinister.class);
    }
}
