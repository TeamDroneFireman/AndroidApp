package edu.istic.tdf.dfclient.repository;

import com.raizlabs.android.dbflow.runtime.transaction.BaseTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.TransactionListener;

import edu.istic.tdf.dfclient.database.IDbReturnHandler;

class SelectTransactionListener<ResultClass> implements TransactionListener<ResultClass> {

    IDbReturnHandler<ResultClass> handler;

    public SelectTransactionListener(final IDbReturnHandler<ResultClass> handler) {
        this.handler = handler;
    }

    @Override
    public void onResultReceived(ResultClass result) {
        handler.onSuccess(result);
    }

    @Override
    public boolean onReady(BaseTransaction<ResultClass> transaction) {
        return true;
    }

    @Override
    public boolean hasResult(BaseTransaction<ResultClass> transaction, ResultClass result) {
        return true;
    }
}