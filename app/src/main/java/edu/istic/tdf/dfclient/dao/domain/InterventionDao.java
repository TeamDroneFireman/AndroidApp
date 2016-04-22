package edu.istic.tdf.dfclient.dao.domain;

import edu.istic.tdf.dfclient.dao.Dao;
import edu.istic.tdf.dfclient.dao.IDao;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;
import edu.istic.tdf.dfclient.domain.sinister.Sinister;
import edu.istic.tdf.dfclient.repository.domain.InterventionRepository;
import edu.istic.tdf.dfclient.repository.domain.SinisterRepository;
import edu.istic.tdf.dfclient.rest.domain.InterventionRestClient;
import edu.istic.tdf.dfclient.rest.domain.SinisterRestClient;

/**
 * The DAO for Sinister
 *
 * {@inheritDoc}
 */
public class InterventionDao extends Dao<Intervention, InterventionRepository, InterventionRestClient>
        implements IDao<Intervention, InterventionRepository, InterventionRestClient> {

    public InterventionDao() {
        super(new InterventionRepository(), new InterventionRestClient());
    }
}
