package edu.istic.tdf.dfclient.dao;

import java.util.ArrayList;
import java.util.List;

import edu.istic.tdf.dfclient.database.IDbReturnHandler;
import edu.istic.tdf.dfclient.domain.Entity;
import edu.istic.tdf.dfclient.repository.IRepository;
import edu.istic.tdf.dfclient.rest.handler.IRestReturnHandler;
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
    public void findAll(DaoSelectionParameters selectionParameters, final IDaoReturnHandler<List<E>> handler) {
        // Make a first query on local data
        //try {
            repository.findAll(selectionParameters.getLimit(), selectionParameters.getOffset(), new IDbReturnHandler<List<E>>() {
                @Override
                public void onSuccess(List<E> r) {
                    handler.onRepositoryResult(r);
                }

                @Override
                public void onError(Throwable error) {
                    handler.onRepositoryFailure(error);
                }
            });

        //} catch(Exception e) { // TODO : More fine exception
            //handler.onFailure(e);
        //}

        // Get data from REST service
        restClient.findAll(new IRestReturnHandler<ArrayList<E>>() {
            @Override
            public void onSuccess(ArrayList<E> result) {
                for(E e : result) {
                    repository.persist(e);
                }
                handler.onRestResult(result);
            }

            @Override
            public void onError(Throwable error) {
                handler.onRestFailure(error);
            }
        });
    }

    @Override
    public void find(String id, final IDaoReturnHandler<E> handler) {
        repository.find(id, new IDbReturnHandler<E>() {
            @Override
            public void onSuccess(E r) {
                handler.onRepositoryResult(r);
            }

            @Override
            public void onError(Throwable error) {
                handler.onRepositoryFailure(error);
            }
        });

        restClient.find(id, new IRestReturnHandler<E>() {
            @Override
            public void onSuccess(E r) {
                repository.persist(r);
                handler.onRestResult(r);
            }

            @Override
            public void onError(Throwable error) {
                handler.onRestFailure(error);
            }
        });
    }

    @Override
    public void persist(final E entity, final IDaoReturnHandler<E> handler) {

    }

    @Override
    public void delete(E entity) {

    }
}
