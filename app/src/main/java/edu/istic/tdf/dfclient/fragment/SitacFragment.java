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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.UI.Tool;
import edu.istic.tdf.dfclient.domain.element.Element;
import edu.istic.tdf.dfclient.domain.element.IElement;
import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.domain.geo.GeoPoint;
import edu.istic.tdf.dfclient.drawable.PictoFactory;

public class SitacFragment extends SupportMapFragment implements OnMapReadyCallback, Observer {

    private OnFragmentInteractionListener mListener;
    private GoogleMap googleMap;

    private Double latitude = 0.0;
    private Double longitude = 0.0;

    private List<Element> elementsToSync = new ArrayList<>();

    // Liste d'association marker <--> element
    private HashMap<Marker, Element> markersList = new HashMap<>();

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
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;
        initMap();
        syncMarker();

    }

    private void initMap(){
        googleMap.getUiSettings().setTiltGesturesEnabled(false);
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (hasElementSelected()) {
                    Element element = createElementFromLatLng(latLng);
                    Marker marker = addMarker(element);
                    if(marker != null){
                        addMarker(element).showInfoWindow();
                    }
                } else {

                    cancelSelection();
                }
            }
        });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                mListener.setSelectedElement(markersList.get(marker));
                return false;
            }

        });

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(this.latitude, this.longitude), 18));

    }

    public void setLocation(GeoPoint geoPoint){

        this.latitude = geoPoint.getLatitude();
        this.longitude = geoPoint.getLongitude();

        if(googleMap != null ){
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude()), 18));
        }
    }

    private Element createElementFromLatLng(LatLng latLng){
        Tool tool = mListener.getSelectedTool();
        return mListener.handleElementAdded(tool.getForm(), latLng.latitude, latLng.longitude);
    }

    private void cancelSelection(){

        mListener.handleCancelSelection();
    }

    private boolean hasElementSelected(){
        return mListener.getSelectedTool() != null;
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

        public Tool getSelectedTool();

        public void setSelectedElement(Element element);
        public Element handleElementAdded(PictoFactory.ElementForm form, Double latitude, Double longitude);
        public void handleCancelSelection();

    }

    private Marker getMarker(IElement element){
        if(markersList.containsValue(element)){
            for (Map.Entry<Marker, Element> entry : markersList.entrySet()) {
                Marker marker = entry.getKey();
                IElement elementValue = entry.getValue();
                if(elementValue.equals(element)){
                    return marker;
                } else if (element.getId() != null && elementValue.getId() == element.getId()){
                    return marker;
                }
            }
        }
        return null;
    }

    private void syncMarker(){
        for (Element element : elementsToSync){
            updateElement(element);
        }
        elementsToSync.clear();
    }

    private Marker addMarker(Element element){

        if(element.getRole() == null ){
            element.setRole(Role.DEFAULT);
        }
        if(element.getForm() == null){
            element.setForm(PictoFactory.ElementForm.MEAN);
        }

        if(googleMap != null) {

            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(element.getLocation().getGeopoint().getLatitude(), element.getLocation().getGeopoint().getLongitude()))
                    .title(element.getName())
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.fromBitmap(
                            PictoFactory.createPicto(getContext())
                                    .setLabel(element.getName())
                                    .setDrawable(element.getForm().getDrawable())
                                    .setColor(element.getRole().getColor())
                                    .toBitmap()
                    )));

            markersList.put(marker, element);
            return marker;
        }
        elementsToSync.add(element);
        return null;
    }

    private void updateMarker(Marker marker, Element element){

        marker.setIcon(BitmapDescriptorFactory.fromBitmap(PictoFactory.createPicto(getContext()).setElement(element).toBitmap()));
        marker.setPosition(new LatLng(element.getLocation().getGeopoint().getLatitude(), element.getLocation().getGeopoint().getLongitude()));
        marker.setTitle(element.getName());

        markersList.remove(marker);
        markersList.put(marker, element);

    }

    public void updateElement(Element element){
        Marker marker = getMarker(element);
        if(marker == null){
            addMarker(element);
        } else {
            updateMarker(marker, element);
        }
    }

    public void updateElements(Collection<Element> elements){
        for(Element element : elements){
            updateElement(element);
        }
    }

    public void removeElement(IElement element){
        Marker marker = getMarker(element);
        if(marker != null){
            marker.remove();
            markersList.remove(marker);
        }
    }

    public void removeElements(Collection<IElement> elements){
        for(IElement element : elements){
            removeElement(element);
        }
    }

}