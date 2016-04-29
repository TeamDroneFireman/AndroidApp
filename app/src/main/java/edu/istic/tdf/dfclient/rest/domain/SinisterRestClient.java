package edu.istic.tdf.dfclient.rest.domain;

import edu.istic.tdf.dfclient.domain.sinister.Sinister;
import edu.istic.tdf.dfclient.http.TdfHttpClient;
import edu.istic.tdf.dfclient.http.configuration.TdfHttpClientConfSinister;
import edu.istic.tdf.dfclient.rest.IRestClient;
import edu.istic.tdf.dfclient.rest.RestClient;

/**
 * Created by tremo on 27/04/16.
 */
public class SinisterRestClient extends RestClient<Sinister> implements IRestClient<Sinister> {

public SinisterRestClient(TdfHttpClient httpClient) {
        super(Sinister.class, httpClient);
                httpClient.setConf(new TdfHttpClientConfSinister()); // Hack because no load balancer
        }

@Override
public String getRestEndpoint() {
        return "api/sinisters/";
        }
}
