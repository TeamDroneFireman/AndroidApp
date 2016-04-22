package edu.istic.tdf.dfclient.http;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Request;

/**
 * Builds the request
 */
public class TdfHttpRequestBuilder {

    Request.Builder builder;

    public TdfHttpRequestBuilder(Request.Builder requestBuilder) {
        this.builder = requestBuilder;
    }

    public void appendHeaders(HashMap<String, String> headers) {
        Iterator it = headers.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            builder.addHeader((String) pair.getKey(), (String) pair.getValue());
            it.remove();
        }
    }

    public void setAuth() {
        HashMap<String, String> authHeaders = new HashMap<>();
        authHeaders.put("HTTP-AUTH-LOGIN", "THESUPERLOGIN");
        authHeaders.put("HTTP-AUTH-TOKEN", "THESUPERTOKEN");
        this.appendHeaders(authHeaders);
    }

    public void setAcceptHeaders(String accept) {
        HashMap<String, String> acceptHeaders = new HashMap<>();
        acceptHeaders.put("Accept", accept);
        this.appendHeaders(acceptHeaders);
    }

    public Request.Builder getBuilder() {
        return this.builder;
    }
}
