package edu.istic.tdf.dfclient.rest;

import com.google.gson.Gson;

import java.util.ArrayList;

import edu.istic.tdf.dfclient.domain.Entity;
import edu.istic.tdf.dfclient.http.TdfHttpClient;
import edu.istic.tdf.dfclient.rest.handler.RestHttpResponseHandler;
import edu.istic.tdf.dfclient.rest.serializer.ArrayListParameterizedType;

/**
 * {@inheritDoc}
 */
public abstract class RestClient<E extends Entity> implements IRestClient<E> {

    /**
     * The HTTP client
     */
    // TODO : Dependency injection
    TdfHttpClient httpClient;

    Gson serializer;

    /**
     * The class of entity
     */
    final Class<E> entityClass;

    /**
     * Constructs a REST Client
     * @param entityClass The class of the entity
     */
    public RestClient(Class<E> entityClass) {
        this.entityClass = entityClass;
        this.httpClient = new TdfHttpClient();
        this.serializer = new Gson();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void findAll(final IRestReturnHandler<ArrayList<E>> callback) {
        // Response handler
        RestHttpResponseHandler<ArrayList<E>> handler =
                new RestHttpResponseHandler<>(callback, new ArrayListParameterizedType(entityClass));

        // Make request
        httpClient.get(getRestEndpoint(), handler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void find(String id, IRestReturnHandler<E> callback) {
        // Response handler
        RestHttpResponseHandler<E> handler =
                new RestHttpResponseHandler<E>(callback, entityClass);

        // Make request
        httpClient.get(getRestEndpoint(), handler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void persist(E entity, IRestReturnHandler<E> callback) {
        // Response handler
        RestHttpResponseHandler<E> handler =
                new RestHttpResponseHandler<E>(callback, entityClass);

        String body = serializer.toJson(entity);

        httpClient.post(getRestEndpoint(), body, handler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(E entity) {

        // No callback
        IRestReturnHandler<E> callback = new IRestReturnHandler<E>() {
            @Override
            public void onSuccess(E r) {

            }

            @Override
            public void onError(Throwable error) {

            }
        };

        // Response handler
        RestHttpResponseHandler<E> handler =
                new RestHttpResponseHandler<E>(callback, entityClass);

        // Make request
        httpClient.delete(getRestEndpoint(), handler);
    }

    /**
     * Gets the URL of the REST endpoint
     * @return The endpoint URL
     */
    public abstract String getRestEndpoint();
}
