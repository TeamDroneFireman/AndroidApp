package edu.istic.tdf.dfclient.rest.service.login.request;

import com.google.gson.annotations.SerializedName;

/**
 * Request sent on login
 */
public class LoginRequest {

    /**
     * The user login
     */
    @SerializedName("email")
    private String login;

    /**
     * The user password
     */
    private String password;

    public LoginRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
