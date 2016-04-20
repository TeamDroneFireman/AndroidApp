package edu.istic.tdf.dfclient.dao;

/**
 * Created by maxime on 20/04/2016.
 */
public interface IDaoCallback<Result> {

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
}
