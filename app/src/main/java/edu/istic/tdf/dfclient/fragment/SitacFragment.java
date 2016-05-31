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
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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
import edu.istic.tdf.dfclient.domain.element.mean.drone.mission.Mission;
import edu.istic.tdf.dfclient.domain.element.pointOfInterest.PointOfInterest;
import edu.istic.tdf.dfclient.domain.geo.GeoPoint;
import edu.istic.tdf.dfclient.domain.image.ImageDrone;
import edu.istic.tdf.dfclient.drawable.PictoFactory;
import edu.istic.tdf.dfclient.util.MapUtils;

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
    private List<ImageDrone> imageDronesToSync = new ArrayList<>();

    // Liste d'association marker <--> element
    private HashMap<Marker, Element> markersList = new HashMap<>();

    // Liste d'association marker <--> element
    private HashMap<Marker, Collection<ImageDrone>> markersListImageDrone = new HashMap<>();

    // Gestion des chemins de drone
    // List d'association id drone <--> polyline
    private HashMap<String, Polyline> dronePathsList = new HashMap<>();
    private HashMap<String, Polyline> missionPathsList = new HashMap<>();
    private HashMap<String, Polygon> missionZonesList = new HashMap<>();

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
                    rectOptions.color(Role.WHITE.getColor());

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

                if(isImageDroneMarker(marker))
                {
                    mListener.handleImageDronesSelected(markersListImageDrone.get(marker));
                }
                else if(!isDronePathMode){
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
                element.setForm(PictoFactory.getFormPlanned(element.getForm()));
                switch(element.getType()){
                    case AIRMEAN:
                    case MEAN:
                    case MEAN_OTHER:
                        MeanState meanState = ((IMean) element).getState();
                        if(meanState==MeanState.ENGAGED){
                            ((IMean)element).setState(MeanState.INTRANSIT);
                        }else if(meanState==MeanState.ARRIVED){
                            ((IMean)element).setState(MeanState.ENGAGED);
                            ((IMean)element).setState(MeanState.INTRANSIT);
                        }
                        break;

                }

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
        isDronePathMode = false;
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

    /**
     *
     * Draw a polyline on map and store it in the dronePathsList hashmap with the element id as key
     *
     * @param element
     */
    private void drawDronePath(Element element){

        // Remove paths if already present
        removePathsForDrone(element);

        if(((Drone)element).hasMission()) {

            // Mission points
            ArrayList<LatLng> missionPathPoints = MapUtils.geoPointListToLatLngList(((Drone) element).getMission().getPathPoints());

            if(((Drone)element).getMission().getPathMode() == Mission.PathMode.ZONE){

                PolygonOptions polygonOptions = new PolygonOptions().addAll(missionPathPoints).strokeColor(Role.PEOPLE.getColor()).fillColor(0x5564DD17);
                Polygon polygon = googleMap.addPolygon(polygonOptions);
                missionZonesList.put(element.getId(), googleMap.addPolygon(polygonOptions));

            } else {

                PolylineOptions missionPathsOptions = new PolylineOptions().addAll(missionPathPoints).color(0x5564DD17);
                missionPathsList.put(element.getId(), googleMap.addPolyline(missionPathsOptions));

                // Drone progress points
                ArrayList<LatLng> dronePathPoints = new ArrayList<>();

                LatLng nearestPointOnMission = MapUtils.findNearestPoint(
                        MapUtils.geoPointToLatLng(element.getLocation().getGeopoint()),
                        missionPathPoints
                );

                // Check on which mission segment is the nearest point
                LatLng lastPoint = missionPathPoints.get(0);
                int closestMissionPointIndex = 0;

                for (LatLng currentPoint : missionPathPoints) {

                    if (MapUtils.isOnSegment(nearestPointOnMission, lastPoint, currentPoint)) {
                        closestMissionPointIndex = missionPathPoints.indexOf(currentPoint);
                    }

                    lastPoint = currentPoint;
                }

                dronePathPoints.addAll(missionPathPoints.subList(0, closestMissionPointIndex));
                dronePathPoints.add(nearestPointOnMission);

                PolylineOptions dronePathsOptions = new PolylineOptions().addAll(dronePathPoints).color(Role.PEOPLE.getColor());
                dronePathsList.put(element.getId(), googleMap.addPolyline(dronePathsOptions));
            }

        }

    }

    private void removePathsForDrone(Element element) {

        if(missionZonesList.get(element.getId()) != null){
            missionZonesList.get(element.getId()).remove();
        }

        if(dronePathsList.get(element.getId()) != null){
            dronePathsList.get(element.getId()).remove();
        }

        if(missionPathsList.get(element.getId()) != null){
            missionPathsList.get(element.getId()).remove();
        }
    }

    public ArrayList<LatLng> getCurrentDronePath(){
        return this.currentPath;
    }

    public interface OnFragmentInteractionListener {

        /**
         *
         * @return true iff the current intervention is archived
         */
        boolean isInterventionArchived();

        /**
         * handle the selection of a marker of type image drone
         * @param imageDrones
         */
        void handleImageDronesSelected(Collection<ImageDrone> imageDrones);

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

    private Marker getImageDroneMarker(ImageDrone imageDrone){
        for (Map.Entry<Marker, Collection<ImageDrone>> entry : markersListImageDrone.entrySet()) {
            Marker marker = entry.getKey();
            Collection<ImageDrone> imageDroneValues = entry.getValue();

            Iterator<ImageDrone> iterator = imageDroneValues.iterator();
            ImageDrone img;
            while(iterator.hasNext())
            {
                img = iterator.next();

                if (img.getGeoPoint() != null &&
                        img.getGeoPoint().getAltitude() == imageDrone.getGeoPoint().getAltitude() &&
                        img.getGeoPoint().getLatitude() == imageDrone.getGeoPoint().getLatitude() &&
                        img.getGeoPoint().getLongitude() == imageDrone.getGeoPoint().getLongitude())
                {
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

        for (ImageDrone imageDrone : imageDronesToSync){
            updateImageDrone(imageDrone);
        }

        imageDronesToSync.clear();
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
            drawDronePath(element);
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

    /**
     * add a marker that represent a ImageDrone
     * @param imageDrone
     * @return
     */
    private Marker addMarker(ImageDrone imageDrone){
        if(googleMap != null) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(imageDrone.getGeoPoint().getLatitude(), imageDrone.getGeoPoint().getLongitude()))
                    .draggable(false));

            Collection<ImageDrone> imageDrones = new ArrayList<>();
            imageDrones.add(imageDrone);
            markersListImageDrone.put(marker, imageDrones);
            return marker;
        }

        imageDronesToSync.add(imageDrone);
        return null;
    }

    /**
     *
     * @param element
     * @return true iff the current element is not an external Point of Interest
     * AND if the current user is not the CODIS
     * AND if the current intervention is not archived
     */
    private boolean isDraggable(Element element)
    {
        if(this.isCodis || element.getId() == null || mListener.isInterventionArchived())
        {
            return false;
        }

        switch (element.getType())
        {
            case POINT_OF_INTEREST:
                //disable contextual drawer for external SIG
                if(((PointOfInterest)element).isExternal())
                {
                    return false;
                }
                break;
        }

        return true;
    }

    private boolean isImageDroneMarker(Marker marker)
    {
        return markersListImageDrone.containsKey(marker);
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

        if(element.getForm() == PictoFactory.ElementForm.AIRMEAN || element.getForm() == PictoFactory.ElementForm.AIRMEAN_PLANNED){
            drawDronePath(element);
        }

        markersList.put(marker, element);
    }

    /**
     * Update the marker if require
     * @param marker
     * @param imageDrone
     */
    private void updateImageDroneMarker(Marker marker, ImageDrone imageDrone){
        Collection<ImageDrone> imageDrones =  markersListImageDrone.get(marker);
        Collection<ImageDrone> newImageDrones = new ArrayList<>();
        for (ImageDrone img:imageDrones)
        {
            if(img.getId() != imageDrone.getId())
            {
                newImageDrones.add(img);
            }
        }

        newImageDrones.add(imageDrone);
        marker.remove();
        markersListImageDrone.remove(marker);
        marker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(imageDrone.getGeoPoint().getLatitude(), imageDrone.getGeoPoint().getLongitude()))
                .draggable(false));
        markersListImageDrone.put(marker, newImageDrones);
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

    public void updateImageDrones(Collection<ImageDrone> imageDrones)
    {
        for (ImageDrone imageDrone : imageDrones) {
            updateImageDrone(imageDrone);
        }
    }

    private void updateImageDrone(ImageDrone imageDrone)
    {
        Marker marker = getImageDroneMarker(imageDrone);
        if(marker == null){
            addMarker(imageDrone);
        } else {
            updateImageDroneMarker(marker, imageDrone);
        }
    }

    public void removeImageDrones(Collection<ImageDrone> imageDrones)
    {
        for (ImageDrone imageDrone : imageDrones) {
            removeImageDrone(imageDrone);
        }
    }

    private void removeImageDrone(ImageDrone imageDrone)
    {
        Marker marker = getImageDroneMarker(imageDrone);
        if(marker != null){
            Collection<ImageDrone> imageDrones = markersListImageDrone.get(marker);

            for (ImageDrone img:imageDrones)
            {
                if(img.getId() == imageDrone.getId())
                {
                    imageDrones.remove(img);
                }
            }

            if(imageDrones.size() == 0)
            {
                marker.remove();
                markersListImageDrone.remove(marker);
            }
        }
    }
}
