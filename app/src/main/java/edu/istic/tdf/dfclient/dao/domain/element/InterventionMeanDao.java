package edu.istic.tdf.dfclient.dao.domain.element;

import edu.istic.tdf.dfclient.dao.IDao;
import edu.istic.tdf.dfclient.domain.element.mean.interventionMean.InterventionMean;
import edu.istic.tdf.dfclient.http.TdfHttpClient;
import edu.istic.tdf.dfclient.repository.domain.element.InterventionMeanRepository;
import edu.istic.tdf.dfclient.rest.domain.element.InterventionMeanRestClient;

/**
 * The DAO for Sinister
 *
 * {@inheritDoc}
 */
public class InterventionMeanDao extends ElementDao<InterventionMean, InterventionMeanRepository, InterventionMeanRestClient>
        implements IDao<InterventionMean, InterventionMeanRepository, InterventionMeanRestClient> {

    public InterventionMeanDao(TdfHttpClient httpClient) {
        super(new InterventionMeanRepository(), new InterventionMeanRestClient(httpClient));
    }
}