package edu.istic.tdf.dfclient.rest;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import edu.istic.tdf.dfclient.auth.Credentials;
import edu.istic.tdf.dfclient.dao.DaoSelectionParameters;
import edu.istic.tdf.dfclient.domain.Entity;
import edu.istic.tdf.dfclient.http.TdfHttpClient;
import edu.istic.tdf.dfclient.rest.handler.IRestReturnHandler;
import edu.istic.tdf.dfclient.http.handler.RestHttpResponseHandler;
import edu.istic.tdf.dfclient.rest.serializer.ArrayListParameterizedType;
import edu.istic.tdf.dfclient.rest.serializer.RestSerializerBuilder;

/**
 * {@inheritDoc}
 */
public abstract class RestClient<E extends Entity> implements IRestClient<E> {

    /**
     * The HTTP client
     */
    protected TdfHttpClient httpClient;

    protected Gson serializer;

    /**
     * The class of entity
     */
    protected final Class<E> entityClass;

    /**
     * Constructs a REST Client
     * @param entityClass The class of the entity
     */
    public RestClient(Class<E> entityClass, TdfHttpClient httpClient) {
        this.entityClass = entityClass;
        this.httpClient = httpClient;
        //this.httpClient = new TdfHttpClient();
        this.serializer = RestSerializerBuilder.build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void findAll(DaoSelectionParameters selectionParameters, final IRestReturnHandler<ArrayList<E>> callback) {
        // Query filters
        HashMap<String, String> queryParameters = selectionParameters.getFilters();

        // Response handler
        RestHttpResponseHandler<ArrayList<E>> handler =
                new RestHttpResponseHandler<>(callback, new ArrayListParameterizedType(entityClass));

        // Make request
        httpClient.get(getResourceUri(""), queryParameters, new HashMap<String,String>(), handler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void find(String id, IRestReturnHandler<E> callback) {
        // Response handler
        RestHttpResponseHandler<E> handler =
                new RestHttpResponseHandler<>(callback, entityClass);

        // Make request
        httpClient.get(getResourceUri(id + "/"), handler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void persist(E entity, IRestReturnHandler<E> callback) {
        // Response handler
        RestHttpResponseHandler<E> handler =
                new RestHttpResponseHandler<>(callback, entityClass);

        String body = serializer.toJson(entity);

        // If entity has an ID, its a PATCH, else its a POST
        if(entity.getId() != null){
            httpClient.put(getResourceUri(entity.getId() + "/"), body, handler);
        } else {
            httpClient.post(getResourceUri(""), body, handler);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(E entity, final IRestReturnHandler<Void> callback) {

        // Response handler
        RestHttpResponseHandler<Void> handler =
                new RestHttpResponseHandler<>(callback, null);

        // Make request
        httpClient.delete(getResourceUri(entity.getId() + "/"), handler);
    }

    /**
     * Gets the URL of the REST endpoint
     * @return The endpoint URL
     */
    public abstract String getRestEndpoint();

    protected String getResourceUri(String relativeUri) {
        return getRestEndpoint() + relativeUri;
    }
}
