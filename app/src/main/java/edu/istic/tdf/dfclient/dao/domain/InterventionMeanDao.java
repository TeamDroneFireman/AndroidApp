package edu.istic.tdf.dfclient.dao.domain;

import edu.istic.tdf.dfclient.dao.Dao;
import edu.istic.tdf.dfclient.dao.IDao;
import edu.istic.tdf.dfclient.domain.element.mean.interventionMean.InterventionMean;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;
import edu.istic.tdf.dfclient.http.TdfHttpClientPort12345;
import edu.istic.tdf.dfclient.repository.domain.InterventionMeanRepository;
import edu.istic.tdf.dfclient.repository.domain.InterventionRepository;
import edu.istic.tdf.dfclient.rest.domain.InterventionMeanRestClient;
import edu.istic.tdf.dfclient.rest.domain.InterventionRestClient;

/**
 * The DAO for Sinister
 *
 * {@inheritDoc}
 */
public class InterventionMeanDao extends ElementDao<InterventionMean, InterventionMeanRepository, InterventionMeanRestClient>
        implements IDao<InterventionMean, InterventionMeanRepository, InterventionMeanRestClient> {

    public InterventionMeanDao(TdfHttpClientPort12345 httpClient) {
        super(new InterventionMeanRepository(), new InterventionMeanRestClient(httpClient));
    }
}