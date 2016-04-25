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

    /**
     * Is a CODIS user
     */
    private Boolean isCodisUser;

    public Credentials() {
    }

    public Credentials(String userId, String token, Boolean isCodisUser) {
        this.userId = userId;
        this.token = token;
        this.isCodisUser = isCodisUser;
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

    public Boolean isCodisUser() {
        return isCodisUser;
    }

    public void setIsCodisUser(Boolean isCodisUser) {
        this.isCodisUser = isCodisUser;
    }

    public boolean isValid() {
        return (this.getUserId() != null && this.getToken() != null);
    }
}
