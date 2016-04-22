package edu.istic.tdf.dfclient.repository;

import android.util.Log;

import com.raizlabs.android.dbflow.runtime.transaction.BaseTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.TransactionListener;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

import edu.istic.tdf.dfclient.database.IDbReturnHandler;
import edu.istic.tdf.dfclient.domain.Entity;
import edu.istic.tdf.dfclient.domain.Entity_Table;

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
    public void findAll(int limit, int offset, final IDbReturnHandler<List<E>> handler) {
        SQLite.select()
                .from(entityClass)
                .limit(limit)
                .offset(offset)
                .async().queryList(new SelectTransactionListener<>(handler));
    }

    @Override
    public void find(String id, final IDbReturnHandler<E> handler) {
        SQLite.select()
                .from(entityClass)
                .where(Entity_Table.id.eq(id))
                .async().querySingle(new SelectTransactionListener<>(handler));
    }

    @Override
    public void persist(E entity, final IDbReturnHandler<E> handler) {
        entity.save();
        handler.onSuccess(entity);
    }

    @Override
    public void persist(E entity) {
        persist(entity, new IDbReturnHandler<E>() {
            @Override
            public void onSuccess(E r) {
                // Nothing
            }

            @Override
            public void onError(Throwable error) {
                // Nothing
            }
        });
    }

    @Override
    public void delete(E entity) {
        entity.delete();
    }
}
