package edu.istic.tdf.dfclient.dao.domain;

import java.util.ArrayList;
import java.util.List;

import edu.istic.tdf.dfclient.dao.Dao;
import edu.istic.tdf.dfclient.dao.DaoSelectionParameters;
import edu.istic.tdf.dfclient.dao.IDao;
import edu.istic.tdf.dfclient.dao.handler.IDaoSelectReturnHandler;
import edu.istic.tdf.dfclient.domain.image.ImageDrone;
import edu.istic.tdf.dfclient.http.TdfHttpClient;
import edu.istic.tdf.dfclient.repository.domain.ImageDroneRepository;
import edu.istic.tdf.dfclient.rest.domain.ImageDroneRestClient;
import edu.istic.tdf.dfclient.rest.handler.IRestReturnHandler;

/**
 * Created by btessiau on 30/05/16.
 */
public class ImageDroneDao extends Dao<ImageDrone, ImageDroneRepository, ImageDroneRestClient>
        implements IDao<ImageDrone, ImageDroneRepository, ImageDroneRestClient> {

    public ImageDroneDao(TdfHttpClient httpClient) {
        super(new ImageDroneRepository(), new ImageDroneRestClient(httpClient));
    }

    /**
     *
     * Finds a collection of elements from an intervention
     * @param interventionId The id of the intervention to get elements from
     * @param selectionParameters The selection parameters, such as limit or order by
     * @param handler The handler called at return
     */
    public void findByIntervention(String interventionId, DaoSelectionParameters selectionParameters, final IDaoSelectReturnHandler<List<ImageDrone>> handler) {
        // Get data from REST service
        restClient.findByIntervention(interventionId, selectionParameters, new IRestReturnHandler<ArrayList<ImageDrone>>() {
            @Override
            public void onSuccess(ArrayList<ImageDrone> result) {
                for (ImageDrone e : result) {
                    repository.persist(e);
                }
                handler.onRestResult(result);
            }

            @Override
            public void onError(Throwable error) {
                handler.onRestFailure(error);
            }
        });
    }
}

