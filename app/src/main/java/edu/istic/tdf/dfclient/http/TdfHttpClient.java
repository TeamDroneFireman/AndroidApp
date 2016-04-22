package edu.istic.tdf.dfclient.http;

import java.util.HashMap;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class TdfHttpClient implements IHttpClient {
    //private static final String BASE_URL = "http://projetm2gla.istic.univ-rennes1.fr/";
    private static final String BASE_URL = "http://pastebin.com/raw/";

    public static final String HTTP_ACCEPT = "application/json";
    public static final String HTTP_CONTENT_TYPE = "application/json; charset=utf-8";

    // TODO : Dependency injection
    public OkHttpClient client;

    public TdfHttpClient() {
        this.client = new OkHttpClient.Builder().build();
    }

    /**
     * {@inheritDoc}
     */
    public void get(String url, HashMap<String, String> headers, Callback handler) {
        Request.Builder requestBuilder = new Request.Builder()
                .url(getAbsoluteUrl(url))
                .get();

        TdfHttpRequestBuilder tdfRequestBuilder = new TdfHttpRequestBuilder(requestBuilder);
        tdfRequestBuilder.appendHeaders(headers);
        tdfRequestBuilder.setAcceptHeaders(HTTP_ACCEPT);
        tdfRequestBuilder.setAuth();

        Request request = tdfRequestBuilder.getBuilder().build();

        client.newCall(request).enqueue(handler);
    }

    /**
     * {@inheritDoc}
     */
    public void get(String url, Callback handler) {
        this.get(url, new HashMap<String, String>(), handler);
    }

    /**
     * {@inheritDoc}
     */
    public void post(String url, String body, HashMap<String, String> headers, Callback handler) {
        Request.Builder requestBuilder = new Request.Builder()
                .url(getAbsoluteUrl(url))
                .post(RequestBody.create(MediaType.parse(HTTP_CONTENT_TYPE), body));

        TdfHttpRequestBuilder tdfRequestBuilder = new TdfHttpRequestBuilder(requestBuilder);
        tdfRequestBuilder.appendHeaders(headers);
        tdfRequestBuilder.setAcceptHeaders(HTTP_ACCEPT);
        tdfRequestBuilder.setAuth();

        Request request = tdfRequestBuilder.getBuilder().build();

        client.newCall(request).enqueue(handler);
    }

    /**
     * {@inheritDoc}
     */
    public void post(String url, String body, Callback handler) {
        this.post(url, body, new HashMap<String, String>(), handler);
    }

    /**
     * {@inheritDoc}
     */
    public void patch(String url, String body, HashMap<String, String> headers, Callback handler) {
        Request.Builder requestBuilder = new Request.Builder()
                .url(getAbsoluteUrl(url))
                .patch(RequestBody.create(MediaType.parse(HTTP_CONTENT_TYPE), body));

        TdfHttpRequestBuilder tdfRequestBuilder = new TdfHttpRequestBuilder(requestBuilder);
        tdfRequestBuilder.appendHeaders(headers);
        tdfRequestBuilder.setAcceptHeaders(HTTP_ACCEPT);
        tdfRequestBuilder.setAuth();

        Request request = tdfRequestBuilder.getBuilder().build();

        client.newCall(request).enqueue(handler);
    }

    /**
     * {@inheritDoc}
     */
    public void patch(String url, String body, Callback handler) {
        this.patch(url, body, new HashMap<String, String>(), handler);
    }

    /**
     * {@inheritDoc}
     */
    public void delete(String url, HashMap<String, String> headers, Callback handler) {
        Request.Builder requestBuilder = new Request.Builder()
                .url(getAbsoluteUrl(url))
                .get();

        TdfHttpRequestBuilder tdfRequestBuilder = new TdfHttpRequestBuilder(requestBuilder);
        tdfRequestBuilder.appendHeaders(headers);
        tdfRequestBuilder.setAcceptHeaders(HTTP_ACCEPT);
        tdfRequestBuilder.setAuth();

        Request request = tdfRequestBuilder.getBuilder().build();

        client.newCall(request).enqueue(handler);
    }

    /**
     * {@inheritDoc}
     */
    public void delete(String url, Callback handler) {
        this.delete(url, new HashMap<String, String>(), handler);
    }

    /**
     * {@inheritDoc}
     */
    private String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}