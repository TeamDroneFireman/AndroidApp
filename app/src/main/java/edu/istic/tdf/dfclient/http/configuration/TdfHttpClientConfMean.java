package edu.istic.tdf.dfclient.http.configuration;

/**
 * Created by maxime on 27/04/2016.
 */
public class TdfHttpClientConfMean extends TdfHttpClientConf {
    @Override
    public int getPort() {
        return 12347;
    }

    @Override
    public String getPath() {
        return "";
    }
}
