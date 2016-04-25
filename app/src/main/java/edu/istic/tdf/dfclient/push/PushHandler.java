package edu.istic.tdf.dfclient.push;

import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import eu.inloop.easygcm.GcmListener;

/**
 *  Custom GCM listener
 */
public class PushHandler {
    public static final String CHANNEL_KEY = "channel";

    public static final String CHANNEL_TEST = "test";

    public void handlePush(String sender, Bundle data) {

        if(data.containsKey(CHANNEL_KEY) && data.getString(CHANNEL_KEY) != null) {
            switch(data.getString(CHANNEL_KEY)) {
                case CHANNEL_TEST:
                    handlePushTest(sender, data);
                    break;
            }
        }
    }

    protected void handlePushTest(String sender, Bundle data){

    }
}
