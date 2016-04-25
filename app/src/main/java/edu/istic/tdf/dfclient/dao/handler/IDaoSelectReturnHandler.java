package edu.istic.tdf.dfclient.dao.handler;

/**
 * Handler for DAO Results
 * @param <Result> The result type
 */
public interface IDaoSelectReturnHandler<Result> {

    /**
     * Callback called after repository request result
     * @param r
     */
    void onRepositoryResult(Result r);

    /**
     * Callback called after REST request result
     * @param r
     */
    void onRestResult(Result r);

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
