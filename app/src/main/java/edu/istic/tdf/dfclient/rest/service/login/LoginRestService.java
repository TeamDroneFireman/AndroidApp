package edu.istic.tdf.dfclient.rest.service.login;

import android.util.Log;

import com.google.gson.Gson;

import edu.istic.tdf.dfclient.auth.Credentials;
import edu.istic.tdf.dfclient.http.TdfHttpClient;
import edu.istic.tdf.dfclient.http.handler.RestHttpResponseHandler;
import edu.istic.tdf.dfclient.rest.handler.IRestReturnHandler;
import edu.istic.tdf.dfclient.rest.serializer.RestSerializerBuilder;
import edu.istic.tdf.dfclient.rest.service.login.request.LoginRequest;
import edu.istic.tdf.dfclient.rest.service.login.response.LoginResponse;

/**
 * Created by maxime on 22/04/2016.
 */
public class LoginRestService {

    private final static String LOGIN_URI = "/SITUsers/login";

    TdfHttpClient httpClient;
    Gson serializer;

    public LoginRestService(TdfHttpClient httpClient, Gson serializer) {
        this.httpClient = httpClient;
        this.serializer = serializer;
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
