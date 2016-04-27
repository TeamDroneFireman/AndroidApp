package edu.istic.tdf.dfclient.fragment;

/**
 * Created by tremo on 27/04/16.
 */


import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.domain.element.mean.drone.Drone;

public class DronesMapFragment extends SupportMapFragment implements OnMapReadyCallback, Observer {
    Drone drone;

    private OnFragmentInteractionListener mListener;

    public DronesMapFragment() {
        // Required empty public constructor
    }

    public static DronesMapFragment newInstance() {
        DronesMapFragment fragment = new DronesMapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drone = new Drone();/*
        Location tempLoc = new Location("tempLoc");
        tempLoc.setLatitude(219);
        tempLoc.setLongitude(302);
        drone.setLocation(tempLoc);
        String latitude = "Latitude : " + String.valueOf(drone.getLocation().getGeopoint().getLatitude());
        String longitude = "Longitude : " + String.valueOf(drone.getLocation().getGeopoint().getLongitude());
        Toast toastLatitude = Toast.makeText(this.getContext(), latitude, Toast.LENGTH_LONG);
        Toast toastLongitude = Toast.makeText(this.getContext(), longitude, Toast.LENGTH_LONG);
        toastLatitude.show();
        toastLongitude.show();

*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_drones_map, container, false);

        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void update(Observable observable, Object data) {

    }

    @Override
    public void onMapReady(GoogleMap map) {

    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
    }
}
