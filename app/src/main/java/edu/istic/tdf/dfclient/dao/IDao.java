package edu.istic.tdf.dfclient.dao;

import java.util.List;

import edu.istic.tdf.dfclient.domain.Entity;
import edu.istic.tdf.dfclient.repository.IRepository;
import edu.istic.tdf.dfclient.repository.Repository;
import edu.istic.tdf.dfclient.rest.IRestClient;
import edu.istic.tdf.dfclient.rest.RestClient;

/**
 * A base DAO for all domain Entities
 *
 * @author maxime
 *
 * @param <E> Entity to manage
 * @param <R> Repository of this entity
 * @param <C> Rest Client of this entity
 */
public interface IDao<E extends Entity, R extends IRepository<E>, C extends IRestClient<E>> {

    /**
     * Finds all entities and returns them as a list
     * @param result List of entities
     */
    void findAll(IDaoCallback<List<E>> result);

    /**
     * Finds one entity and returns it
     * @param id Id of the entity to find
     * @param result The found entity
     */
    void find(String id, IDaoCallback<E> result);

    /**
     * Persists an entity and returns its new version
     * @param entity The entity to persist
     * @param result The new version of entity
     */
    void persist(E entity, IDaoCallback<E> result);

    /**
     * Deletes an entity
     * @param entity The entity to delete
     */
    void delete(E entity);
}
