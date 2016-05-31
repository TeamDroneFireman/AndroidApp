package edu.istic.tdf.dfclient.dao.domain;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import edu.istic.tdf.dfclient.dao.Dao;
import edu.istic.tdf.dfclient.dao.DaoSelectionParameters;
import edu.istic.tdf.dfclient.dao.IDao;
import edu.istic.tdf.dfclient.dao.handler.IDaoSelectReturnHandler;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;
import edu.istic.tdf.dfclient.http.TdfHttpClient;
import edu.istic.tdf.dfclient.repository.domain.InterventionRepository;
import edu.istic.tdf.dfclient.rest.domain.InterventionRestClient;
import edu.istic.tdf.dfclient.rest.handler.IRestReturnHandler;

/**
 * The DAO for Interventions
 *
 * {@inheritDoc}
 */
public class InterventionDao extends Dao<Intervention, InterventionRepository, InterventionRestClient>
        implements IDao<Intervention, InterventionRepository, InterventionRestClient> {

    public InterventionDao(TdfHttpClient httpClient, Gson gson) {
        super(new InterventionRepository(), new InterventionRestClient(httpClient, gson));
    }

    /**
     * Subscribes a registrationId of a device to an intervention
     * @param intervention The intervention to subscribe to
     * @param registrationId The registration id to subscribe
     */
    public void subscribe(Intervention intervention, String registrationId){
        this.restClient.subscribe(registrationId, intervention);
    }

    /**
     * get intervention with asked means
     * @param selectionParameters
     * @param handler
     */
    public void findAllWithAskedElement(DaoSelectionParameters selectionParameters, final IDaoSelectReturnHandler<List<Intervention>> handler){
        restClient.findInterventionsWithAskedElement( selectionParameters, new IRestReturnHandler<ArrayList<Intervention>>() {
            @Override
            public void onSuccess(ArrayList<Intervention> result) {
                for (Intervention e : result) {
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