package edu.istic.tdf.dfclient.domain.element;

import android.location.Location;

/**
 * Created by btessiau on 20/04/16.
 */
public class InterventionZone implements IInterventionZone {
    private Role role;


    @Override
    public void printOnMap() {

    }

    @Override
    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public Role getRole() {
        return this.role;
    }

    @Override
    public void setPosition(Location location) {

    }

    @Override
    public void setName(String name) {

    }
}
