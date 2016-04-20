package edu.istic.tdf.dfclient.domain.element;

import android.location.Location;

/**
 * Created by btessiau on 20/04/16.
 */
public class InterventionZone implements IInterventionZone {
    private Role role;
    private String name;

    public InterventionZone() {
        this.init();
    }

    private void init() {
        this.role = Role.NONE;
        this.name = "Default Name";
    }

    @Override
    public void printOnMap() {
        //TODO
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
        //TODO

    }

    @Override
    public void setName(String name) {
        this.name = name;

    }
}
