package edu.istic.tdf.dfclient.rest.domain;

import edu.istic.tdf.dfclient.domain.sinister.Sinister;
import edu.istic.tdf.dfclient.http.TdfHttpClient;
import edu.istic.tdf.dfclient.http.configuration.TdfHttpClientConf;
import edu.istic.tdf.dfclient.rest.IRestClient;
import edu.istic.tdf.dfclient.rest.RestClient;
import edu.istic.tdf.dfclient.rest.RestEndpoints;

/**
 * The REST client for Sinister
 */
public class SinisterRestClient extends RestClient<Sinister> implements IRestClient<Sinister> {

public SinisterRestClient(TdfHttpClient httpClient) {
        super(Sinister.class, httpClient);
                httpClient.setConf(new TdfHttpClientConf(RestEndpoints.Sinister)); // Hack because no load balancer
        }

}
