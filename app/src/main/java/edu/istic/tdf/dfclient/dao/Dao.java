package edu.istic.tdf.dfclient.dao;

import com.raizlabs.android.dbflow.structure.InvalidDBConfiguration;

import java.util.ArrayList;
import java.util.List;

import edu.istic.tdf.dfclient.dao.handler.IDaoSelectReturnHandler;
import edu.istic.tdf.dfclient.dao.handler.IDaoWriteReturnHandler;
import edu.istic.tdf.dfclient.database.IDbReturnHandler;
import edu.istic.tdf.dfclient.domain.Entity;
import edu.istic.tdf.dfclient.repository.IRepository;
import edu.istic.tdf.dfclient.repository.exception.NoResultException;
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
    public void findAll(DaoSelectionParameters selectionParameters, final IDaoSelectReturnHandler<List<E>> handler) {
        // Make a first query on local data
        try {
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

        } catch(InvalidDBConfiguration e) { // TODO : More fine exception
            handler.onRepositoryFailure(e);
        }

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
    public void find(String id, final IDaoSelectReturnHandler<E> handler) {
        // Make a first query on local data
        repository.find(id, new IDbReturnHandler<E>() {
            @Override
            public void onSuccess(E r) {
                if(r == null) {
                    handler.onRepositoryFailure(new NoResultException());
                    return;
                }
                handler.onRepositoryResult(r);
            }

            @Override
            public void onError(Throwable error) {
                handler.onRepositoryFailure(error);
            }
        });

        // Request distant service
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
    public void persist(final E entity, final IDaoWriteReturnHandler handler) {
        // Persist distant
        restClient.persist(entity, new IRestReturnHandler<E>() {
            @Override
            public void onSuccess(E r) {
                // Local persist
                repository.persist(entity, new IDbReturnHandler<E>() {
                    @Override
                    public void onSuccess(E r) {
                        handler.onSuccess();
                    }

                    @Override
                    public void onError(Throwable error) {
                        handler.onRepositoryFailure(error);
                    }
                });
            }

            @Override
            public void onError(Throwable error) {
                handler.onRestFailure(error);
            }
        });
    }

    @Override
    public void delete(final E entity, final IDaoWriteReturnHandler handler) {
        // Persist distant
        restClient.delete(entity, new IRestReturnHandler<Void>() {
            @Override
            public void onSuccess(Void r) {
                repository.delete(entity, new IDbReturnHandler<Void>() {
                    @Override
                    public void onSuccess(Void r) {
                        handler.onSuccess();
                    }

                    @Override
                    public void onError(Throwable error) {
                        handler.onRepositoryFailure(error);
                    }
                });
            }

            @Override
            public void onError(Throwable error) {
                handler.onRestFailure(error);
            }
        });
    }
}
