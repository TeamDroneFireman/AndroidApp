package edu.istic.tdf.dfclient.http;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by maxime on 25/04/2016.
 */
// TODO : Remove these ugly classes when LoadBalancer will rock
public class TdfHttpClientPort12345 extends TdfHttpClient {

    private final static String SCHEME = "http";
    private final static String HOST = "projetm2gla.istic.univ-rennes1.fr";
    private final static int PORT = 12345;
    private final static String PATH = "api/";

    @Inject
    public TdfHttpClientPort12345(OkHttpClient client) {
        super(client);
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
     * Gets the absolute URL of a resource, with query parameters
     *
     * @param relativeUrl     The relative endpoint
     * @param queryParameters The GET query parameters
     * @return An HTTP Url object
     */
    private HttpUrl getAbsoluteUrl(String relativeUrl, HashMap<String, String> queryParameters) {
        HttpUrl.Builder urlBuilder = new HttpUrl.Builder()
                .scheme(SCHEME)
                .host(HOST)
                .port(PORT)
                .addPathSegments(PATH)
                .addPathSegments(relativeUrl);

        for (Map.Entry<String, String> entry : queryParameters.entrySet()) {
            urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
        }

        return urlBuilder.build();
    }

    /**
     * Gets the absolute URL of a resource
     *
     * @param relativeUrl The relative endpoint
     * @return An HTTP Url object
     */
    private HttpUrl getAbsoluteUrl(String relativeUrl) {
        return this.getAbsoluteUrl(relativeUrl, new HashMap<String, String>());
    }
}
