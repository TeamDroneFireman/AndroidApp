package edu.istic.tdf.dfclient.http.configuration;

/**
 * Created by maxime on 27/04/2016.
 */
public class TdfHttpClientConfSinister extends TdfHttpClientConf {
    @Override
    public int getPort() {
        return 12348;
    }

    @Override
    public String getPath() {
        return "";
    }
}
