package edu.istic.tdf.dfclient.dao.domain;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.istic.tdf.dfclient.dao.Dao;
import edu.istic.tdf.dfclient.dao.DaoSelectionParameters;
import edu.istic.tdf.dfclient.dao.IDao;
import edu.istic.tdf.dfclient.dao.handler.IDaoSelectReturnHandler;
import edu.istic.tdf.dfclient.domain.PushMessage;
import edu.istic.tdf.dfclient.domain.image.ImageDrone;
import edu.istic.tdf.dfclient.http.TdfHttpClient;
import edu.istic.tdf.dfclient.repository.domain.ImageDroneRepository;
import edu.istic.tdf.dfclient.repository.domain.PushMessageRepository;
import edu.istic.tdf.dfclient.rest.domain.ImageDroneRestClient;
import edu.istic.tdf.dfclient.rest.domain.PushMessageRestClient;
import edu.istic.tdf.dfclient.rest.handler.IRestReturnHandler;

/**
 * Created by tremo on 01/06/16.
 */
public class PushMessageDao extends Dao<PushMessage, PushMessageRepository, PushMessageRestClient>
    implements IDao<PushMessage, PushMessageRepository, PushMessageRestClient> {

    public PushMessageDao(PushMessageRepository repository, PushMessageRestClient restClient) {
        super(repository, restClient);
    }

    public PushMessageDao(TdfHttpClient httpClient){
        super(new PushMessageRepository(), new PushMessageRestClient(httpClient));
    }

    public void findMessageByInterventionAndDate(String interventionId, Date date, DaoSelectionParameters selectionParameters, final IDaoSelectReturnHandler<List<PushMessage>> handler) {
        // Get data from REST service
        restClient.findMessagesByDate(interventionId, date, selectionParameters, new IRestReturnHandler<ArrayList<PushMessage>>() {

            @Override
            public void onSuccess(ArrayList<PushMessage> r) {
                handler.onRestResult(r);
            }

            @Override
            public void onError(Throwable error) {
                handler.onRestFailure(error);
            }
        });
    }
}
