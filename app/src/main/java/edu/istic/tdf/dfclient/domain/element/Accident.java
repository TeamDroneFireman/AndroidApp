package edu.istic.tdf.dfclient.domain.element;

import android.location.Location;

/**
 * Created by btessiau on 20/04/16.
 */
public class Accident implements IAccident {

    /**
     * The unique Id of this object
     */
    private String id;

    /**
     * The role of this element
     */
    private Role role;

    /**
     * The name of this element
     */
    private String name;

    /**
     * The location of this element
     */
    private Location location;

    public Accident() {

    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
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
