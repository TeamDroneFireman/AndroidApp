package edu.istic.tdf.dfclient.fragment;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.UI.Tool;

public class SitacFragment extends SupportMapFragment implements OnMapReadyCallback {

    private OnFragmentInteractionListener mListener;

    public SitacFragment() {
    }

    public static SitacFragment newInstance() {
        return new SitacFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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

        final GoogleMap gMap = map;

        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){

            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }

        });
        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_tab_unselected_24dp1);
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
                Tool selectedTool = mListener.getSelectedTool();
                if (selectedTool != null) {
                    gMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(selectedTool.getTitle())
                            .draggable(true)
                            .icon(icon));
                }
            }
        });

        gMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                Toast.makeText(getContext(), marker.getTitle() + "(" + marker.getId()+ ")", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                Toast.makeText(getContext(), marker.getTitle() + "(" + marker.getId()+ ") OK ! ", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

            }
        });
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            if (location != null)
            {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(location.getLatitude(), location.getLongitude()), 13));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))
                        .zoom(17)
                        .build();
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }

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

    public interface OnFragmentInteractionListener {
       public Tool getSelectedTool();
    }

    }