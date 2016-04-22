package edu.istic.tdf.dfclient.auth;

import android.content.Context;
import android.content.SharedPreferences;

import edu.istic.tdf.dfclient.TdfApplication;

/**
 * Created by maxime on 22/04/2016.
 */
public class AuthHelper {

    private final static String SHARED_PREFENCES_LOGIN_NAME = "SPLOGIN";
    private final static String SHARED_PREFERENCE_USERID = "USERID";
    private final static String SHARED_PREFERENCE_TOKEN = "TOKEN";

    public static void storeCredentials(Credentials credentials) {
        Context context = TdfApplication.getAppContext();
        if(context == null) {
            return;
        }

        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFENCES_LOGIN_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(SHARED_PREFERENCE_USERID, credentials.getUserId());
        editor.putString(SHARED_PREFERENCE_TOKEN, credentials.getToken());
        editor.commit();
    }

    public static Credentials loadCredentials() {
        Context context = TdfApplication.getAppContext();
        if(context == null) {
            return null;
        }

        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFENCES_LOGIN_NAME, Context.MODE_PRIVATE);
        Credentials credentials = new Credentials();

        credentials.setUserId(sharedPref.getString(SHARED_PREFERENCE_USERID, null));
        credentials.setToken(sharedPref.getString(SHARED_PREFERENCE_TOKEN, null));

        if(!credentials.isValid()) {
            return null;
        }

        return credentials;
    }
}
