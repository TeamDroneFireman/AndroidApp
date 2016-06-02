package edu.istic.tdf.dfclient.database;

/**
 * Handles Database returns
 *
 * @author maxime
 */
public interface IDbReturnHandler<Result>
{
    /**
     * Callback called in case of successful request
     * @param r Result of the request
     */
    void onSuccess(Result r);

    /**
     * Callback called in case of error when requesting data
     * @param error The throwable returned from HTTP Client or from Deserializer
     */
    void onError(Throwable error);
}
