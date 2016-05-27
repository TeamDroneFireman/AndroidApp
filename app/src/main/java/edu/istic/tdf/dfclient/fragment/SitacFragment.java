package edu.istic.tdf.dfclient.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.TdfApplication;
import edu.istic.tdf.dfclient.UI.Tool;
import edu.istic.tdf.dfclient.domain.element.Element;
import edu.istic.tdf.dfclient.domain.element.ElementType;
import edu.istic.tdf.dfclient.domain.element.IElement;
import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.domain.element.mean.IMean;
import edu.istic.tdf.dfclient.domain.element.mean.MeanState;
import edu.istic.tdf.dfclient.domain.element.mean.drone.Drone;
import edu.istic.tdf.dfclient.domain.element.mean.drone.IDrone;
import edu.istic.tdf.dfclient.domain.element.pointOfInterest.PointOfInterest;
import edu.istic.tdf.dfclient.domain.geo.GeoPoint;
import edu.istic.tdf.dfclient.drawable.PictoFactory;

public class SitacFragment extends SupportMapFragment implements OnMapReadyCallback, Observer {

    //minimal distance between 2 marker
    private Double minimalDistance = 0.00003;
    //Last valid lat
    private Double lastValidLat;
    //Last valid lng
    private Double lastValidLng;
    //Last valid lat
    private Marker currentDraggedMarker;

    private OnFragmentInteractionListener mListener;
    private GoogleMap googleMap;

    private Double latitude = 0.0;
    private Double longitude = 0.0;

    private List<Element> elementsToSync = new ArrayList<>();

    // Liste d'association marker <--> element
    private HashMap<Marker, Element> markersList = new HashMap<>();

    // Gestion des chemins de drone
    private boolean isDronePathMode;
    private ArrayList<LatLng> currentPath;
    private Polyline currentPolyline;

    private boolean isCodis;

    public SitacFragment() {
    }

    public static SitacFragment newInstance() { return new SitacFragment(); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.isCodis = ((TdfApplication)this.getActivity().getApplication()).loadCredentials().isCodisUser();

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
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(isDronePathMode){

                    currentPath.add(latLng);

                    PolylineOptions rectOptions = new PolylineOptions().addAll(currentPath);
                    if(currentPolyline != null){
                        currentPolyline.remove();
                    }

                    currentPolyline = googleMap.addPolyline(rectOptions);

                }else if (hasElementSelected()) {
                    Element element = mListener.tryGetSelectedElement();
                    if (element != null)
                    {
                        GeoPoint geoPoint = new GeoPoint();
                        geoPoint.setLatitude(latLng.latitude);
                        geoPoint.setLongitude(latLng.longitude);
                        element.getLocation().setGeopoint(geoPoint);
                        Tool tool = mListener.getSelectedTool();
                        mListener.handleElementAdded(tool, latLng.latitude, latLng.longitude);
                    }
                    else
                    {
                        element = createElementFromLatLng(latLng);

                        Marker marker = addMarker(element);
                        if (marker != null) {
                            addMarker(element).showInfoWindow();
                        }
                    }
                } else {
                    cancelSelection();
                }
            }
        });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                if(!isDronePathMode){
                    mListener.setSelectedElement(markersList.get(marker));
                }
                return false;
            }

        });

        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                //initialize the current position to the initial marker position
                lastValidLat = marker.getPosition().latitude;
                lastValidLng = marker.getPosition().longitude;
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                currentDraggedMarker = marker;
                //compute in a thread the last valid position
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        computeLastValidPosition(currentDraggedMarker);
                    }
                });

                Element element = markersList.get(marker);

                element.getLocation().getGeopoint().setLatitude(marker.getPosition().latitude);
                element.getLocation().getGeopoint().setLongitude(marker.getPosition().longitude);
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                LatLng latLng = new LatLng(lastValidLat, lastValidLng);
                marker.setPosition(latLng);

                Element element = markersList.get(marker);

                element.getLocation().getGeopoint().setLatitude(marker.getPosition().latitude);
                element.getLocation().getGeopoint().setLongitude(marker.getPosition().longitude);

                mListener.handleUpdatedElement(element);
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

    public boolean isLocationEmpty(){
        return this.longitude == 0.0 || this.latitude == 0.0;
    }

    private Element createElementFromLatLng(LatLng latLng){
        Tool tool = mListener.getSelectedTool();
        return mListener.handleElementAdded(tool, latLng.latitude, latLng.longitude);
    }

    /**
     *
     * @param currentMarker
     * @return an array that contain a valid lat and a valid lng
     */
    private void computeLastValidPosition(Marker currentMarker)
    {
        Double newLat = currentMarker.getPosition().latitude;
        Double newLng = currentMarker.getPosition().longitude;

        Double currentLat;
        Double currentLng;
        Double diffLat;
        Double diffLng;

        Double distanceToTheCurrentMarker;
        boolean isPositionValid = true;

        for (Marker marker : markersList.keySet()) {
            if (!marker.getId().equals(currentMarker.getId()))
            {
                currentLat = marker.getPosition().latitude;
                currentLng = marker.getPosition().longitude;
                diffLat = newLat - currentLat;
                diffLng = newLng - currentLng;
                distanceToTheCurrentMarker = Math.sqrt(Math.pow(diffLat, 2) + Math.pow(diffLng, 2));
                isPositionValid = isPositionValid && (distanceToTheCurrentMarker > this.minimalDistance);
            }
        }

        if(isPositionValid)
        {
            this.lastValidLat = newLat;
            this.lastValidLng = newLng;
        }
    }

    public void cancelSelection(){
        if(currentPolyline != null){
            currentPolyline.remove();
        }
        for (Map.Entry<Marker, Element> entry : markersList.entrySet()) {
            Marker marker = entry.getKey();
            IElement elementValue = entry.getValue();
            if (elementValue.getId() == null){
                marker.remove();
            }
        }
        if (mListener != null)
            mListener.handleCancelSelection();
    }

    public void cancelSelectionAfterPushIfRequire(Element element, Element currentElement){
        for (Map.Entry<Marker, Element> entry : markersList.entrySet()) {
            Marker marker = entry.getKey();
            IElement elementValue = entry.getValue();
            if (elementValue.getId() == null){
                marker.remove();
            }
        }

        if(currentElement != null && currentElement.getId() != null && currentElement.getId().equals(element.getId()))
        {
            mListener.handleCancelSelection();
        }
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

    public void setDronePathMode(boolean isDronePathMode) {
        this.currentPath = new ArrayList<LatLng>();
        this.isDronePathMode = isDronePathMode;
    }

    public ArrayList<LatLng> getCurrentDronePath(){
        return this.currentPath;
    }

    public interface OnFragmentInteractionListener {

        Tool getSelectedTool();
        Element tryGetSelectedElement();

        void setSelectedElement(Element element);
        Element handleElementAdded(Tool tool, Double latitude, Double longitude);
        void handleUpdatedElement(Element element);
        void handleCancelSelection();

    }

    private Marker getMarker(IElement element){
        if(markersList.containsValue(element)){
            for (Map.Entry<Marker, Element> entry : markersList.entrySet()) {
                Marker marker = entry.getKey();
                IElement elementValue = entry.getValue();
                if(elementValue.equals(element)){
                    return marker;
                } else if (elementValue.getId() != null && elementValue.getId() == element.getId()){
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

        if(element.getForm() == PictoFactory.ElementForm.AIRMEAN || element.getForm() == PictoFactory.ElementForm.AIRMEAN_PLANNED){

            ArrayList<LatLng> pathPoints = new ArrayList<>();
            if(((Drone)element).getMission() != null){

                for(GeoPoint geoPoint : ((Drone)element).getMission().getPathPoints()){
                    pathPoints.add(new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude()));
                }

                PolylineOptions rectOptions = new PolylineOptions().addAll(pathPoints);

                Polyline dronePolyline = googleMap.addPolyline(rectOptions);
            }

        }

        if(googleMap != null) {

            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(element.getLocation().getGeopoint().getLatitude(), element.getLocation().getGeopoint().getLongitude()))
                    .title(element.getName())
                    .draggable(isDraggable(element))
                    .icon(BitmapDescriptorFactory.fromBitmap(
                            PictoFactory.createPicto(getContext()).setElement(element).toBitmap()
                    )));

            markersList.put(marker, element);
            return marker;
        }
        elementsToSync.add(element);
        return null;
    }

    private boolean isDraggable(Element element)
    {
        boolean result = true;

        switch (element.getType())
        {
            case POINT_OF_INTEREST:
                //disable contextual drawer for external SIG
                if(((PointOfInterest)element).isExternal())
                {
                    result = false;
                }
                break;
        }

        result = result && (element.getId() != null) && !this.isCodis;

        return result;
    }

    private void updateMarker(Marker marker, Element element){
        markersList.remove(marker);

        marker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(element.getLocation().getGeopoint().getLatitude(), element.getLocation().getGeopoint().getLongitude()))
                .title(element.getName())
                .draggable(isDraggable(element))
                .icon(BitmapDescriptorFactory.fromBitmap(
                        PictoFactory.createPicto(getContext()).setElement(element).toBitmap()
                )));

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
            switch (ElementType.getElementType(element.getForm())){
                case MEAN:
                    if (((IMean) element).getStates().get(MeanState.RELEASED) != null){
                        continue;
                    }
                    break;
                case AIRMEAN:
                    if (((IDrone) element).getStates().get(MeanState.RELEASED) != null) {
                        continue;
                    }
                    break;
                default:
                    break;
            }
            if(element.getLocation().getGeopoint() != null) {
                updateElement(element);
            }
        }
    }

    public void removeElement(Element element){
        Marker marker = getMarker(element);
        if(marker != null){
            marker.remove();
            markersList.remove(marker);
        }
    }

    public void removeElements(Collection<Element> elements){
        for(Element element : elements){
            removeElement(element);
        }
    }

}
