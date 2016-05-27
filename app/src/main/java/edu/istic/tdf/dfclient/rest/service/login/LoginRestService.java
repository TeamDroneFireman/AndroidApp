package edu.istic.tdf.dfclient.rest.service.login;

import com.google.gson.Gson;

import edu.istic.tdf.dfclient.http.TdfHttpClient;
import edu.istic.tdf.dfclient.http.configuration.TdfHttpClientConf;
import edu.istic.tdf.dfclient.http.handler.RestHttpResponseHandler;
import edu.istic.tdf.dfclient.rest.RestEndpoints;
import edu.istic.tdf.dfclient.rest.handler.IRestReturnHandler;
import edu.istic.tdf.dfclient.rest.service.login.request.LoginRequest;
import edu.istic.tdf.dfclient.rest.service.login.response.LoginResponse;

/**
 * A REST service for user login
 */
public class LoginRestService {

    private final static String LOGIN_URI = "api/SITUsers/login";

    TdfHttpClient httpClient;
    Gson serializer;

    public LoginRestService(TdfHttpClient httpClient, Gson serializer) {
        this.httpClient = httpClient;
        this.serializer = serializer;
        this.httpClient.setConf(new TdfHttpClientConf(RestEndpoints.User));
    }

    /**
     * {@inheritDoc}
     */
    public void login(String login, String password, IRestReturnHandler<LoginResponse> callback) {
        RestHttpResponseHandler<LoginResponse> handler =
                new RestHttpResponseHandler<>(callback, LoginResponse.class);

        LoginRequest request = new LoginRequest(login, password);
        String body = serializer.toJson(request);

        httpClient.post(LOGIN_URI, body, handler);
    }
}
