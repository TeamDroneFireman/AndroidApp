package edu.istic.tdf.dfclient.dao.domain;

import javax.inject.Inject;

import edu.istic.tdf.dfclient.auth.Credentials;
import edu.istic.tdf.dfclient.dao.Dao;
import edu.istic.tdf.dfclient.dao.IDao;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;
import edu.istic.tdf.dfclient.http.TdfHttpClient;
import edu.istic.tdf.dfclient.repository.domain.InterventionRepository;
import edu.istic.tdf.dfclient.rest.domain.InterventionRestClient;

/**
 * The DAO for Sinister
 *
 * {@inheritDoc}
 */
public class InterventionDao extends Dao<Intervention, InterventionRepository, InterventionRestClient>
        implements IDao<Intervention, InterventionRepository, InterventionRestClient> {

    public InterventionDao(TdfHttpClient httpClient) {
        super(new InterventionRepository(), new InterventionRestClient(httpClient));
    }
}