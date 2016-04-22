package edu.istic.tdf.dfclient.auth;

/**
 * Created by maxime on 22/04/2016.
 */
public class Credentials {
    /**
     * Id of user
     */
    private String userId;

    /**
     * Session token
     */
    private String token;

    public Credentials() {
    }

    public Credentials(String userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isValid() {
        return (this.getUserId() != null && this.getToken() != null);
    }
}
