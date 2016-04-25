package edu.istic.tdf.dfclient.push;

import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import eu.inloop.easygcm.GcmListener;

/**
 *  Custom GCM listener
 */
public class PushListener implements GcmListener {

    /**
     * {@inheritDoc}
     */
    @Override
    public void onMessage(String s, Bundle bundle) {
        Log.i("Maxime", "Push received on chanel "+s+" with data" + bundle.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendRegistrationIdToBackend(String s) {
        Log.i("Maxime", "GCM Registration ID"+s);
    }
}
