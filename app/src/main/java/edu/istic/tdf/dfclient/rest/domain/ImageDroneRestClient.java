package edu.istic.tdf.dfclient.rest.domain;

import java.util.ArrayList;
import java.util.HashMap;

import edu.istic.tdf.dfclient.dao.DaoSelectionParameters;
import edu.istic.tdf.dfclient.domain.image.ImageDrone;
import edu.istic.tdf.dfclient.http.TdfHttpClient;
import edu.istic.tdf.dfclient.http.configuration.TdfHttpClientConf;
import edu.istic.tdf.dfclient.http.handler.RestHttpResponseHandler;
import edu.istic.tdf.dfclient.rest.IRestClient;
import edu.istic.tdf.dfclient.rest.RestClient;
import edu.istic.tdf.dfclient.rest.RestEndpoints;
import edu.istic.tdf.dfclient.rest.handler.IRestReturnHandler;
import edu.istic.tdf.dfclient.rest.serializer.ArrayListParameterizedType;

/**
 * Created by btessiau on 30/05/16.
 */
public class ImageDroneRestClient extends RestClient<ImageDrone> implements IRestClient<ImageDrone> {

    public ImageDroneRestClient(TdfHttpClient httpClient) {
        super(ImageDrone.class, httpClient);
        httpClient.setConf(new TdfHttpClientConf(RestEndpoints.ImageDrone)); // Hack because no load balancer
    }

    /**
     * Finds all elements by intervention id
     */
    public void findByIntervention(String interventionId, DaoSelectionParameters selectionParameters,
                                   final IRestReturnHandler<ArrayList<ImageDrone>> callback) {
        // Query filters
        HashMap<String, String> queryParameters = selectionParameters.getFilters();

        // Response handler
        RestHttpResponseHandler<ArrayList<ImageDrone>> handler =
                new RestHttpResponseHandler<>(callback, new ArrayListParameterizedType(entityClass));

        // Make request
        httpClient.get(getResourceUri("intervention/" + interventionId), queryParameters, new HashMap<String, String>(), handler);
    }
}
