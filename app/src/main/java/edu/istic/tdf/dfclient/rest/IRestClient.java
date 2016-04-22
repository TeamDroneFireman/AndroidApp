package edu.istic.tdf.dfclient.rest;

import java.util.ArrayList;

import edu.istic.tdf.dfclient.domain.Entity;
import edu.istic.tdf.dfclient.rest.handler.IRestReturnHandler;

/**
 * Rest client for an entity
 *
 * @author maxime
 */
public interface IRestClient<E extends Entity> {
    /**
     * Gets all entities from Rest service
     * @param callback A list of entities wrapped on a callback
     */
    void findAll(final IRestReturnHandler<ArrayList<E>> callback);

    /**
     * Gets one entity from Rest service
     * @param id Id of entity to get
     * @param callback The getted entity wrapped on a callback
     */
    void find(String id, IRestReturnHandler<E> callback);

    /**
     * Persists an entity to Rest service
     * @param entity The entity to persist
     * @param callback The persisted entity wrapped on a callback
     */
    void persist(E entity, IRestReturnHandler<E> callback);

    /**
     * Deletes an entity to Rest service
     * @param entity The entity to delete
     */
    void delete(E entity, IRestReturnHandler<Void> callback);
}
