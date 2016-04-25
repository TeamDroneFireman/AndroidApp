package edu.istic.tdf.dfclient.repository;

import java.util.List;

import edu.istic.tdf.dfclient.database.IDbReturnHandler;
import edu.istic.tdf.dfclient.domain.Entity;

/**
 * An entity repository
 *
 * @author maxime
 */
public interface IRepository<E extends Entity> {
    /**
     * Finds all entities and returns them as a list
     */
    void findAll(int limit, int offset, final IDbReturnHandler<List<E>> handler);

    /**
     * Finds one entity and returns it
     * @param id Id of the entity to find
     */
    void find(String id, final IDbReturnHandler<E> handler);

    /**
     * Persists an entity and returns its new version
     * @param entity The entity to persist
     */
    void persist(E entity, final IDbReturnHandler<E> handler);

    /**
     * Persists an entity without callback
     * @param entity The entity to persist
     */
    void persist(E entity);

    /**
     * Deletes an entity
     * @param entity The entity to delete
     */
    void delete(E entity);

    /**
     * Deletes an entity
     * @param entity The entity to delete
     */
    void delete(E entity, final IDbReturnHandler<Void> handler);
}
