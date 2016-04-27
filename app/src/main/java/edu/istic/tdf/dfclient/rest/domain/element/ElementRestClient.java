package edu.istic.tdf.dfclient.rest.domain.element;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import edu.istic.tdf.dfclient.dao.DaoSelectionParameters;
import edu.istic.tdf.dfclient.domain.Entity;
import edu.istic.tdf.dfclient.http.TdfHttpClient;
import edu.istic.tdf.dfclient.http.handler.RestHttpResponseHandler;
import edu.istic.tdf.dfclient.rest.IRestClient;
import edu.istic.tdf.dfclient.rest.RestClient;
import edu.istic.tdf.dfclient.rest.handler.IRestReturnHandler;
import edu.istic.tdf.dfclient.rest.serializer.ArrayListParameterizedType;
import edu.istic.tdf.dfclient.rest.serializer.RestSerializerBuilder;

/**
 * {@inheritDoc}
 */
public abstract class ElementRestClient<E extends Entity> extends RestClient<E> implements IRestClient<E> {

    /**
     * Constructs a REST Client
     *
     * @param entityClass The class of the entity
     * @param httpClient
     */
    public ElementRestClient(Class<E> entityClass, TdfHttpClient httpClient) {
        super(entityClass, httpClient);
    }

    /**
     * Finds all elements by intervention id
     */
    public void findByIntervention(String interventionId, DaoSelectionParameters selectionParameters,
                                   final IRestReturnHandler<ArrayList<E>> callback) {
        // Query filters
        HashMap<String, String> queryParameters = selectionParameters.getFilters();

        // Response handler
        RestHttpResponseHandler<ArrayList<E>> handler =
                new RestHttpResponseHandler<>(callback, new ArrayListParameterizedType(entityClass));

        // Make request
        // TODO
        httpClient.get(getResourceUri("TODOHERE"), queryParameters, new HashMap<String,String>(), handler);
    }
}
