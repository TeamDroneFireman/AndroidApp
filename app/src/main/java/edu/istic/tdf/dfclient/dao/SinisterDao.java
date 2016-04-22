package edu.istic.tdf.dfclient.dao;

import edu.istic.tdf.dfclient.domain.sinister.Sinister;
import edu.istic.tdf.dfclient.repository.domain.SinisterRepository;
import edu.istic.tdf.dfclient.rest.domain.SinisterRestClient;

/**
 * The DAO for Sinister
 *
 * {@inheritDoc}
 */
public class SinisterDao extends Dao<Sinister, SinisterRepository, SinisterRestClient> implements IDao<Sinister, SinisterRepository, SinisterRestClient> {

    public SinisterDao() {
        super(new SinisterRepository(), new SinisterRestClient());
    }
}
