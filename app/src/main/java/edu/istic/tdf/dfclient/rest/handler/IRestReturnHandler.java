package edu.istic.tdf.dfclient.rest.handler;

/**
 * Represents a callback called after a rest call (call call call... loll)
 *
 * @author maxime
 */
public interface IRestReturnHandler<Result> {

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
