package edu.istic.tdf.dfclient.rest.domain;

import android.util.Log;

import java.io.IOException;

import edu.istic.tdf.dfclient.domain.intervention.Intervention;
import edu.istic.tdf.dfclient.http.TdfHttpClient;
import edu.istic.tdf.dfclient.http.configuration.TdfHttpClientConf;
import edu.istic.tdf.dfclient.rest.IRestClient;
import edu.istic.tdf.dfclient.rest.RestClient;
import edu.istic.tdf.dfclient.rest.RestEndpoints;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class InterventionRestClient extends RestClient<Intervention> implements IRestClient<Intervention> {

    protected static final String TAG = "InterventionRestClient";

    public InterventionRestClient(TdfHttpClient httpClient) {
        super(Intervention.class, httpClient);
        httpClient.setConf(new TdfHttpClientConf(RestEndpoints.Intervention)); // Hack because no load balancer
    }

    public void subscribe(final String registrationId, final Intervention intervention) {
        // TODO : Register here
        this.httpClient.post("URL_DE_SOUSCRIPTION", "LE BODY", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Error when subscribing to intervention [" + intervention.getId() + "] with registrationId [" + registrationId + "]");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "Subscribed to intervention [" + intervention.getId() + "] with registrationId [" + registrationId + "]");
            }
        });
    }
}
