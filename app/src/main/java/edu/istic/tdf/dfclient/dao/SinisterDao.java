package edu.istic.tdf.dfclient.dao;

import edu.istic.tdf.dfclient.domain.Sinister;
import edu.istic.tdf.dfclient.repository.SinisterRepository;
import edu.istic.tdf.dfclient.rest.SinisterRestClient;

/**
 * Created by maxime on 21/04/2016.
 */
public class SinisterDao extends Dao<Sinister, SinisterRepository, SinisterRestClient> {

    public SinisterDao() {
        super(new SinisterRepository(), new SinisterRestClient());
    }
}
