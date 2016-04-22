package edu.istic.tdf.dfclient.http;

import java.util.HashMap;

import okhttp3.Callback;

/**
 * Created by maxime on 22/04/2016.
 */
public interface IHttpClient {
    /**
     * Makes a GET request and sends an handler
     * @param url Relative URL to request
     * @param headers Http headers map to include in request
     * @param handler Handler to apply after request succeed
     */
    void get(String url, HashMap<String, String> headers, Callback handler);

    /**
     * Makes a GET request and sends an handler
     * @param url Relative URL to request
     * @param handler Handler to apply after request succeed
     */
    void get(String url, Callback handler);

    /**
     * Makes a POST request and sends an handler
     * @param url Relative URL to request
     * @param headers Http headers map to include in request
     * @param body Body of request
     * @param handler Handler to apply after request succeed
     */
    void post(String url, String body, HashMap<String, String> headers, Callback handler);

    /**
     * Makes a POST request and sends an handler
     * @param url Relative URL to request
     * @param body Body of request
     * @param handler Handler to apply after request succeed
     */
    void post(String url, String body, Callback handler);

    /**
     * Makes a PATCH request and sends an handler
     * @param url Relative URL to request
     * @param headers Http headers map to include in request
     * @param body Body of request
     * @param handler Handler to apply after request succeed
     */
    void patch(String url, String body, HashMap<String, String> headers, Callback handler);

    /**
     * Makes a PATCH request and sends an handler
     * @param url Relative URL to request
     * @param body Body of request
     * @param handler Handler to apply after request succeed
     */
    void patch(String url, String body, Callback handler);

    /**
     * Makes a DELETE request and sends an handler
     * @param url Relative URL to request
     * @param headers Http headers map to include in request
     * @param handler Handler to apply after request succeed
     */
    void delete(String url, HashMap<String, String> headers, Callback handler);

    /**
     * Makes a DELETE request and sends an handler
     * @param url Relative URL to request
     * @param handler Handler to apply after request succeed
     */
    void delete(String url, Callback handler);
}
