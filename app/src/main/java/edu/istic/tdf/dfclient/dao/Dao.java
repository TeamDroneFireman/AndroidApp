package edu.istic.tdf.dfclient.dao;

import java.util.ArrayList;
import java.util.List;

import edu.istic.tdf.dfclient.database.IDbReturnHandler;
import edu.istic.tdf.dfclient.domain.Entity;
import edu.istic.tdf.dfclient.repository.IRepository;
import edu.istic.tdf.dfclient.rest.IRestReturnHandler;
import edu.istic.tdf.dfclient.rest.IRestClient;

/**
 * {@inheritDoc}
 */
public abstract class Dao<E extends Entity, R extends IRepository<E>, C extends IRestClient<E>> implements IDao<E,R,C> {

    R repository;

    C restClient;

    public Dao(R repository, C restClient) {
        this.repository = repository;
        this.restClient = restClient;
    }

    @Override
    public void findAll(final IDaoCallback<List<E>> handler) {
        // Make a first query on local data
        try {
            repository.findAll(new IDbReturnHandler<ArrayList<E>>() {
                @Override
                public void onSuccess(ArrayList<E> r) {
                    handler.onRepositoryResult(r);
                }

                @Override
                public void onError(Throwable error) {
                    handler.onFailure(error);
                }
            });

        } catch(Exception e) { // TODO : More fine exception
            handler.onFailure(e);
        }

        // Get data from REST service
        restClient.findAll(new IRestReturnHandler<ArrayList<E>>() {
            @Override
            public void onSuccess(ArrayList<E> result) {
                if(result != null) {
                    for(E e : result) {
                        repository.persist(e);
                    }
                    handler.onRestResult(result);
                } else {
                    handler.onFailure(new Exception("No result from REST client"));
                }
            }

            @Override
            public void onError(Throwable error) {
                handler.onFailure(error);
            }
        });
    }

    @Override
    public void find(final String id, final IDaoCallback<E> handler) {

    }

    @Override
    public void persist(final E entity, final IDaoCallback<E> handler) {

    }

    @Override
    public void delete(E entity) {

    }
}
