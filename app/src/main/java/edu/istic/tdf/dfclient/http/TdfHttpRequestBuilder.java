package edu.istic.tdf.dfclient.http;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Request;

/**
 * Decorates a OKHttp3 request to append TDF requests parameters
 */
public class TdfHttpRequestBuilder {

    /**
     * The OKHTTP3 source builder
     */
    Request.Builder builder;

    /**
     * Constructs a TdfHttpRequestBuilder using OKHTTP3 Builder
     * @param requestBuilder The source OKHTTP3 Builder
     */
    public TdfHttpRequestBuilder(Request.Builder requestBuilder) {
        this.builder = requestBuilder;
    }

    /**
     * Appends some headers to request
     * @param headers
     */
    public void appendHeaders(HashMap<String, String> headers) {
        Iterator it = headers.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            builder.addHeader((String) pair.getKey(), (String) pair.getValue());
            it.remove();
        }
    }

    /**
     * Sets authentication headers
     */
    public void setAuth() {
        HashMap<String, String> authHeaders = new HashMap<>();
        authHeaders.put("HTTP-AUTH-LOGIN", "THESUPERLOGIN");
        authHeaders.put("HTTP-AUTH-TOKEN", "THESUPERTOKEN");
        this.appendHeaders(authHeaders);
    }

    /**
     * Sets the Accept header
     * @param accept The accept content type
     */
    public void setAcceptHeaders(String accept) {
        HashMap<String, String> acceptHeaders = new HashMap<>();
        acceptHeaders.put("Accept", accept);
        this.appendHeaders(acceptHeaders);
    }

    /**
     * Gets the OKHTTP3 Builder after updates
     * @return The okhttp3 builder
     */
    public Request.Builder getBuilder() {
        return this.builder;
    }
}
