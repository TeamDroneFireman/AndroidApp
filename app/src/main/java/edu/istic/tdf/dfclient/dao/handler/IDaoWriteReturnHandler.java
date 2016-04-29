package edu.istic.tdf.dfclient.dao.handler;

/**
 * Handler for DAO Results
 * @param <Result> The result type
 */
public interface IDaoWriteReturnHandler<E> {

    /**
     * Callback called after REST request result
     */
    void onSuccess(E r);

    /**
     * Callback called if something goes wrong with repository
     * @param e
     */
    void onRepositoryFailure(Throwable e);

    /**
     * Callback called if something goes wrong with Rest
     * @param e
     */
    void onRestFailure(Throwable e);
}
