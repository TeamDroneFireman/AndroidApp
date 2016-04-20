package edu.istic.tdf.dfclient.dao;

import java.util.List;

import edu.istic.tdf.dfclient.domain.Entity;
import edu.istic.tdf.dfclient.repository.Repository;
import edu.istic.tdf.dfclient.rest.RestClient;

/**
 * {@inheritDoc}
 */
public abstract class Dao<E extends Entity, R extends Repository<E>, C extends RestClient<E>> implements IDao<E,R,C> {

    @Override
    public void findAll(IDaoCallback<List<E>> result) {

    }

    @Override
    public void find(String id, IDaoCallback<E> result) {

    }

    @Override
    public void persist(E entity, IDaoCallback<E> result) {

    }

    @Override
    public void delete(E entity) {

    }
}
