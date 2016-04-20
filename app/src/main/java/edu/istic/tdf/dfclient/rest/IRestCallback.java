package edu.istic.tdf.dfclient.rest;

/**
 * Represents a callback called after a rest call (call call call... loll)
 *
 * @author maxime
 */
public interface IRestCallback<Result> {

    /**
     * Callback called in case of successful request
     * @param r Result of the request
     */
    void onSuccess(Result r);
}
