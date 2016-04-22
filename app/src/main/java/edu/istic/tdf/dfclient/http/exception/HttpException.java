package edu.istic.tdf.dfclient.http.exception;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Throwed when an exception occured when HTTP requesting
 */
public class HttpException extends Exception {
    /**
     * The call
     */
    Call call = null;

    /**
     * The response if given
     */
    Response response = null;

    public HttpException(Throwable throwable, Call call, Response response) {
        super(throwable);
        this.call = call;
        this.response = response;
    }
}
