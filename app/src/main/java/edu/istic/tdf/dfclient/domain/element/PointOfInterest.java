package edu.istic.tdf.dfclient.domain.element;

import android.location.Location;

/**
 * represent water point, two triangles
 * Created by guerin on 21/04/16.
 */
public class PointOfInterest implements IElement {

    private Role role;

    private Location location;

    private String name;






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
