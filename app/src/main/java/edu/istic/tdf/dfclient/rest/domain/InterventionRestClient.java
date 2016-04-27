package edu.istic.tdf.dfclient.rest.domain;

import javax.inject.Inject;

import edu.istic.tdf.dfclient.auth.Credentials;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;
import edu.istic.tdf.dfclient.http.TdfHttpClient;
import edu.istic.tdf.dfclient.http.configuration.TdfHttpClientConfIntervention;
import edu.istic.tdf.dfclient.rest.IRestClient;
import edu.istic.tdf.dfclient.rest.RestClient;


public class InterventionRestClient extends RestClient<Intervention> implements IRestClient<Intervention> {

    public InterventionRestClient(TdfHttpClient httpClient) {
        super(Intervention.class, httpClient);
        httpClient.setConf(new TdfHttpClientConfIntervention()); // Hack because no load balancer
    }

    @Override
    public String getRestEndpoint() {
        return "interventions/";
    }
}
