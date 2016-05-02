package edu.istic.tdf.dfclient.rest.domain;

import edu.istic.tdf.dfclient.domain.intervention.Intervention;
import edu.istic.tdf.dfclient.http.TdfHttpClient;
import edu.istic.tdf.dfclient.http.configuration.TdfHttpClientConf;
import edu.istic.tdf.dfclient.rest.IRestClient;
import edu.istic.tdf.dfclient.rest.RestClient;
import edu.istic.tdf.dfclient.rest.RestEndpoints;


public class InterventionRestClient extends RestClient<Intervention> implements IRestClient<Intervention> {

    public InterventionRestClient(TdfHttpClient httpClient) {
        super(Intervention.class, httpClient);
        httpClient.setConf(new TdfHttpClientConf(RestEndpoints.Intervention)); // Hack because no load balancer
    }
}
