package edu.istic.tdf.dfclient.dao.domain.element;

import edu.istic.tdf.dfclient.dao.IDao;
import edu.istic.tdf.dfclient.domain.element.pointOfInterest.PointOfInterest;
import edu.istic.tdf.dfclient.http.TdfHttpClient;
import edu.istic.tdf.dfclient.repository.domain.element.PointOfInterestRepository;
import edu.istic.tdf.dfclient.rest.domain.element.PointOfInterestRestClient;

/**
 * The DAO for Points Of Interest
 *
 * {@inheritDoc}
 */
public class PointOfInterestDao extends ElementDao<PointOfInterest, PointOfInterestRepository, PointOfInterestRestClient>
        implements IDao<PointOfInterest, PointOfInterestRepository, PointOfInterestRestClient> {

    public PointOfInterestDao(TdfHttpClient httpClient) {
        super(new PointOfInterestRepository(), new PointOfInterestRestClient(httpClient));
    }
}