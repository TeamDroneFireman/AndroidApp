package edu.istic.tdf.dfclient.domain.element;

import android.location.Location;

/**
 * Created by btessiau on 20/04/16.
 */
public class OperationalOrganisation implements IOperationalOrganisation {
    private Role role;
    private String name;
    private Location location;

    public OperationalOrganisation() {
        this.init();
    }

    private void init() {
        this.role = Role.NONE;
        this.name = "Default Name";
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
        this.location = location;
    }

    @Override
    public Location getPosition() {
        return this.location;
    }

    @Override
    public void setName(String name) {
        this.name = name;

    }

    @Override
    public String getName() {
        return this.name;
    }
}
