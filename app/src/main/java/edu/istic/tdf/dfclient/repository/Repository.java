package edu.istic.tdf.dfclient.repository;

import com.raizlabs.android.dbflow.config.TdfDatabaseTDF_Database;
import com.raizlabs.android.dbflow.runtime.transaction.BaseTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.TransactionListener;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.language.SQLCondition;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

import edu.istic.tdf.dfclient.database.IDbReturnHandler;
import edu.istic.tdf.dfclient.database.TdfDatabase;
import edu.istic.tdf.dfclient.domain.Entity;
import edu.istic.tdf.dfclient.domain.sinister.Sinister;
import edu.istic.tdf.dfclient.rest.IRestReturnHandler;

/**
 * {@inheritDoc}
 */
public abstract class Repository<E extends Entity> implements IRepository<E> {

    /**
     * The class of entity
     */
    final Class<E> entityClass;

    public Repository(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public void findAll(final IDbReturnHandler<ArrayList<E>> handler) {

        SQLite.select()
                .from(entityClass)
                .async().queryList(new TransactionListener<List<E>>() {

            @Override
            public void onResultReceived(List<E> result) {
                handler.onSuccess((ArrayList<E>) result);
            }

            @Override
            public boolean onReady(BaseTransaction<List<E>> transaction) {
                return false;
            }

            @Override
            public boolean hasResult(BaseTransaction<List<E>> transaction, List<E> result) {
                return false;
            }
        });
    }

    @Override
    public void find(String id, final IDbReturnHandler<E> handler) {
        // Async Transaction Queue Retrieval (Recommended for large queries)
        SQLite.select()
                .from(entityClass)
                .async().querySingle(new TransactionListener<E>() {
            @Override
            public void onResultReceived(E result) {
                handler.onSuccess(result);
            }

            @Override
            public boolean onReady(BaseTransaction<E> transaction) {
                return false;
            }

            @Override
            public boolean hasResult(BaseTransaction<E> transaction, E result) {
                return false;
            }
        });
    }

    @Override
    public void persist(E entity, final IDbReturnHandler<E> handler) {
        entity.save();
        if(handler != null) {
            handler.onSuccess(entity);
        }
    }

    @Override
    public void persist(E entity) {
        persist(entity, null);
    }

    @Override
    public void delete(E entity) {
        entity.delete();
    }
}
