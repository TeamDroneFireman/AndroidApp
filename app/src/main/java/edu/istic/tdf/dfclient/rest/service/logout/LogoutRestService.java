package edu.istic.tdf.dfclient.rest.service.logout;

import java.util.HashMap;

import edu.istic.tdf.dfclient.auth.AuthHelper;
import edu.istic.tdf.dfclient.auth.Credentials;
import edu.istic.tdf.dfclient.http.TdfHttpClient;
import edu.istic.tdf.dfclient.http.configuration.TdfHttpClientConfUser;
import edu.istic.tdf.dfclient.http.handler.RestHttpResponseHandler;
import edu.istic.tdf.dfclient.rest.handler.IRestReturnHandler;
import edu.istic.tdf.dfclient.rest.service.logout.response.LogoutResponse;

/**
 * Created by maxime on 22/04/2016.
 */
public class LogoutRestService {

    private final static String LOGOUT_URI = "SITUsers/logout";

    TdfHttpClient httpClient;

    public LogoutRestService(TdfHttpClient httpClient) {
        this.httpClient = httpClient;
        this.httpClient.setConf(new TdfHttpClientConfUser());
    }

    /**
     * {@inheritDoc}
     */
    public void logout(IRestReturnHandler<LogoutResponse> callback) {
        RestHttpResponseHandler<LogoutResponse> handler =
                new RestHttpResponseHandler<>(callback, LogoutResponse.class);
        Credentials credentials = AuthHelper.loadCredentials();
        HashMap<String, String> accessToken = new HashMap<>();
        accessToken.put("access_token", credentials.getToken());
        httpClient.post(LOGOUT_URI, accessToken, "", new HashMap<String, String>(), handler);
    }
}
