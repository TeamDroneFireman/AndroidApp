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
    private Call call = null;

    /**
     * The response if given
     */
    private Response response = null;

    public HttpException(Throwable throwable, Call call, Response response) {
        super(throwable);
        this.call = call;
        this.response = response;
    }

    public Call getCall() {
        return call;
    }

    public void setCall(Call call) {
        this.call = call;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
