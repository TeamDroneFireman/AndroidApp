package edu.istic.tdf.dfclient.domain.element.mean.drone.mission;

import java.util.ArrayList;

import edu.istic.tdf.dfclient.domain.geo.GeoPoint;

/**
 * Created by btessiau on 20/04/16.
 */
public class Mission {

    private ArrayList<GeoPoint> pathPoints = new ArrayList<>();

    public ArrayList<GeoPoint> getPathPoints() {
        return pathPoints;
    }

    public void setPathPoints(ArrayList<GeoPoint> pathPoints) {
        this.pathPoints = pathPoints;
    }

    public Mission() {
    }
}
