package edu.istic.tdf.dfclient.push;

import android.content.Context;
import android.content.SharedPreferences;

import edu.istic.tdf.dfclient.TdfApplication;
import edu.istic.tdf.dfclient.auth.Credentials;

/**
 * Created by maxime on 24/05/2016.
 */
public class PushHelper {

    private final static String SHARED_PREFENCES_PUSH_NAME = "SPPUSH";
    private final static String SHARED_PREFERENCES_REG_ID = "REG_ID";

    public static void storeRegistrationId(String registrationId) {
        Context context = TdfApplication.getAppContext();
        if(context == null) {
            return;
        }

        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFENCES_PUSH_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(SHARED_PREFERENCES_REG_ID, registrationId);
        editor.apply();
    }

    public static String getRegistrationId() {
        Context context = TdfApplication.getAppContext();
        if(context == null) {
            return null;
        }

        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFENCES_PUSH_NAME, Context.MODE_PRIVATE);

        return sharedPref.getString(SHARED_PREFERENCES_REG_ID, null);
    }
}
