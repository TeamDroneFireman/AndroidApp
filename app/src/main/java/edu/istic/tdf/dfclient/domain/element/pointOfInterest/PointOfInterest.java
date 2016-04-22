package edu.istic.tdf.dfclient.domain.element.pointOfInterest;

import android.location.Location;

import edu.istic.tdf.dfclient.domain.element.Role;

/**
 * represent water point, two triangles
 * Created by guerin on 21/04/16.
 */
public class PointOfInterest implements IPointOfInterest {

    /**
     * The role of this element
     */
    private Role role;

    /**
     * The name of this element
     * //TODO check if we used the name for identify water point or other
     */
    private String name;

    /**
     * The location of this element
     */
    private Location location;

    public PointOfInterest() {

    }

    @Override
    public void setRole(Role role) {
        this.role=role;
    }

    @Override
    public Role getRole() {
        return this.role;
    }

    @Override
    public void setPosition(Location location) {
        this.location=location;
    }

    @Override
    public Location getPosition() {
        return this.location;
    }

    @Override
    public void setName(String name) {
        this.name=name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
