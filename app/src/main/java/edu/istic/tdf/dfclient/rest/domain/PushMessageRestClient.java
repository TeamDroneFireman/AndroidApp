package edu.istic.tdf.dfclient.rest.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import edu.istic.tdf.dfclient.dao.DaoSelectionParameters;
import edu.istic.tdf.dfclient.http.TdfHttpClient;
import edu.istic.tdf.dfclient.domain.PushMessage;
import edu.istic.tdf.dfclient.http.configuration.TdfHttpClientConf;
import edu.istic.tdf.dfclient.http.handler.RestHttpResponseHandler;
import edu.istic.tdf.dfclient.rest.IRestClient;
import edu.istic.tdf.dfclient.rest.RestClient;
import edu.istic.tdf.dfclient.rest.RestEndpoints;
import edu.istic.tdf.dfclient.rest.handler.IRestReturnHandler;
import edu.istic.tdf.dfclient.rest.serializer.ArrayListParameterizedType;

/**
 * Created by tremo on 01/06/16.
 */
public class PushMessageRestClient extends RestClient<PushMessage> implements IRestClient<PushMessage> {
    /**
     * Constructs a REST Client
     *
     * @param httpClient
     */

    public PushMessageRestClient(TdfHttpClient httpClient) {
        super(PushMessage.class, httpClient);
        httpClient.setConf(new TdfHttpClientConf(RestEndpoints.PushMessage));
    }
    /**
     * Finds all push messages by intervention and date
     */
    public void findMessagesByDate( String interventionId, Date date, DaoSelectionParameters selectionParameters,
                                                   final IRestReturnHandler<ArrayList<PushMessage>> callback) {
        // Query filters
        HashMap<String, String> queryParameters = selectionParameters.getFilters();

        // Response handler
        RestHttpResponseHandler<ArrayList<PushMessage>> handler =
                new RestHttpResponseHandler<>(callback, new ArrayListParameterizedType(entityClass));


        //http://devprojetm2gla.istic.univ-rennes1.fr/api/interventions/register/
        //http://devprojetm2gla.istic.univ-rennes1.fr/pushMessages/intervention/574e8a67c4138e7a024cf48a?timestamp=Wed%20Jun%2001%2016:20:42%20HAEC%202016&idIntervention=574e8a67c4138e7a024cf48a (205ms)


        // Make request
        httpClient.get(getResourceUri("intervention/"+interventionId), queryParameters, new HashMap<String, String>(), handler);
    }
}
