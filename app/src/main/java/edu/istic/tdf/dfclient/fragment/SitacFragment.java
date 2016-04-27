package edu.istic.tdf.dfclient.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.UI.Tool;
import edu.istic.tdf.dfclient.domain.element.IElement;
import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;
import edu.istic.tdf.dfclient.drawable.PictoFactory;

public class SitacFragment extends SupportMapFragment implements OnMapReadyCallback, Observer {

    private OnFragmentInteractionListener mListener;
    private GoogleMap googleMap;

    // Liste d'association marker <--> element
    private HashMap<Marker, IElement> markersList = new HashMap<>();

    public SitacFragment() {
    }

    public static SitacFragment newInstance() { return new SitacFragment(); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_sitac, container, false);

        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap map) {

        this.googleMap = map;
        initMap();

    }

    private void initMap(){

        for(IElement element : mListener.getInterventionElements()){
            markersList.put(createMarker(element), element);
        }

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                IElement element = markersList.get(marker);
                Toast.makeText(getContext(), element.getName(), Toast.LENGTH_SHORT).show();
                mListener.handleElementAdded();
                return false;
            }

        });

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                Tool selectedTool = mListener.getSelectedTool();
                if (selectedTool != null) {

                    //View marker = getIconView();

                    Marker marker = googleMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(selectedTool.getTitle())
                            .draggable(true)
                            .snippet("Description")
                            .icon(BitmapDescriptorFactory.fromBitmap(
                                    PictoFactory.createPicto(getContext())
                                            .setDrawable(selectedTool.getForm().getDrawable())
                                            .setColor(Role.getRandomColor())
                                            .toBitmap()
                            )));
                    mListener.handleElementAdded();

                } else {
                    mListener.handleCancelSelection();
                }
            }
        });

        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                Toast.makeText(getContext(), marker.getTitle() + "(" + marker.getId() + ")", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                Toast.makeText(getContext(), marker.getTitle() + "(" + marker.getId() + ") OK ! ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

            }
        });

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mListener.getInterventionLatLng(), 10));

    }


    private MarkerOptions createMarkerOptions(IElement element){

        LatLng latLng = new LatLng(element.getLocation().getLatitude(), element.getLocation().getLongitude());
        Bitmap bitmap =  PictoFactory.createPicto(getContext())
                .setDrawable(element.getForm().getDrawable())
                .setColor(element.getRole().getColor())
                .toBitmap();

        return new MarkerOptions()
                .position(latLng)
                .title(element.getName())
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap));

    }

    private Marker createMarker(IElement element){
        return googleMap.addMarker(createMarkerOptions(element));
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void update(Observable observable, Object data) {}

    public interface OnFragmentInteractionListener {

        public LatLng getInterventionLatLng();
        public Collection<IElement> getInterventionElements();

        public Tool getSelectedTool();
        public void handleElementAdded();
        public void handleCancelSelection();

    }

}