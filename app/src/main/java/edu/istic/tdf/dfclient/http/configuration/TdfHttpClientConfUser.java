package edu.istic.tdf.dfclient.http.configuration;

/**
 * Created by maxime on 27/04/2016.
 */
public class TdfHttpClientConfUser extends TdfHttpClientConf {
    @Override
    public int getPort() {
        return 12346;
    }

    @Override
    public String getPath() {
        return "api/";
    }
}
