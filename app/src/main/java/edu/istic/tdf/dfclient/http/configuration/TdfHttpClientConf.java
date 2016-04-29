package edu.istic.tdf.dfclient.http.configuration;

/**
 * Created by maxime on 27/04/2016.
 */
public abstract class TdfHttpClientConf {
    abstract public int getPort();
    public String getPath(){
        return "api/";
    }
}
