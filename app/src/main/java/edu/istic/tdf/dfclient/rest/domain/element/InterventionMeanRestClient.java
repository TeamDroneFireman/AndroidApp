package edu.istic.tdf.dfclient.rest.domain.element;

import edu.istic.tdf.dfclient.domain.element.mean.interventionMean.InterventionMean;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;
import edu.istic.tdf.dfclient.http.TdfHttpClient;
import edu.istic.tdf.dfclient.http.configuration.TdfHttpClientConfIntervention;
import edu.istic.tdf.dfclient.http.configuration.TdfHttpClientConfMean;
import edu.istic.tdf.dfclient.rest.IRestClient;
import edu.istic.tdf.dfclient.rest.RestClient;


public class InterventionMeanRestClient extends ElementRestClient<InterventionMean> implements IRestClient<InterventionMean> {

    public InterventionMeanRestClient(TdfHttpClient httpClient) {
        super(InterventionMean.class, httpClient);
        httpClient.setConf(new TdfHttpClientConfMean()); // Hack because no load balancer

    }

    @Override
    public String getRestEndpoint() {
        return "interventionmeans/";
    }
}
