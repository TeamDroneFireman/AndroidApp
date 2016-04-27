package edu.istic.tdf.dfclient.http;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import edu.istic.tdf.dfclient.auth.Credentials;
import edu.istic.tdf.dfclient.http.configuration.TdfHttpClientConf;
import edu.istic.tdf.dfclient.http.configuration.TdfHttpClientConfUser;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class TdfHttpClient {

    private final static String SCHEME = "http";
    private final static String HOST = "projetm2gla.istic.univ-rennes1.fr";

    public static final String HTTP_ACCEPT = "application/json";
    public static final String HTTP_CONTENT_TYPE = "application/json; charset=utf-8";

    OkHttpClient client;
    TdfHttpClientConf conf;


    public TdfHttpClient(OkHttpClient client) {
        this.client = client;
        this.conf = new TdfHttpClientConfUser(); // TODO : Remove this ugly default
    }

    public TdfHttpClientConf getConf() {
        return conf;
    }

    public void setConf(TdfHttpClientConf conf) {
        this.conf = conf;
    }

    /**
     * {@inheritDoc}
     */
    public void get(String url, HashMap<String, String> queryParameters, HashMap<String, String> headers, Callback handler) {
        Request.Builder requestBuilder = new Request.Builder()
                .url(getAbsoluteUrl(url, queryParameters))
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
        this.get(url, new HashMap<String, String>(), new HashMap<String, String>(), handler);
    }

    /**
     * {@inheritDoc}
     */
    public void post(String url, HashMap<String, String> queryParameters, String body, HashMap<String, String> headers, Callback handler) {
        Request.Builder requestBuilder = new Request.Builder()
                .url(getAbsoluteUrl(url, queryParameters))
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
        this.post(url, new HashMap<String, String>(), body, new HashMap<String, String>(), handler);
    }

    /**
     * {@inheritDoc}
     */
    public void patch(String url, HashMap<String, String> queryParameters, String body, HashMap<String, String> headers, Callback handler) {
        Request.Builder requestBuilder = new Request.Builder()
                .url(getAbsoluteUrl(url, queryParameters))
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
        this.patch(url, new HashMap<String, String>(), body, new HashMap<String, String>(), handler);
    }

    /**
     * {@inheritDoc}
     */
    public void delete(String url, HashMap<String, String> queryParameters, HashMap<String, String> headers, Callback handler) {
        Request.Builder requestBuilder = new Request.Builder()
                .url(getAbsoluteUrl(url, queryParameters))
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
        this.delete(url, new HashMap<String, String>(), new HashMap<String, String>(), handler);
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
                .port(this.conf.getPort())
                .addPathSegments(this.conf.getPath())
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