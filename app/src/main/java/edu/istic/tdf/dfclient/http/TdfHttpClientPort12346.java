package edu.istic.tdf.dfclient.http;

import javax.inject.Inject;

import okhttp3.OkHttpClient;

/**
 * Created by maxime on 25/04/2016.
 */
// TODO : Remove these ugly classes when LoadBalancer will rock
public class TdfHttpClientPort12346 extends TdfHttpClient {
    private static final int PORT = 12346;

    @Inject
    public TdfHttpClientPort12346(OkHttpClient client) {
        super(client);
    }
}
