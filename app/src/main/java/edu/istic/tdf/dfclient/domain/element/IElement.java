package edu.istic.tdf.dfclient.domain.element;

import android.location.Location;

/**
 *
 * IElement is graphic element of mean
 * Created by btessiau on 20/04/16.
 */
public interface IElement {

    /**
     * Print itself on the map
     * TODO choose the map object (parameter)
     */
    public void printOnMap();

    /**
     * set the element's role
     * @param role
     */
    public void setRole(Role role);

    /**
     *
     * @return the Role of the element
     */
    public Role getRole();


    /**
     * set the element's location
     * @param location
     */
    public void setPosition(Location location);

    /**
     *
     * @return the location
     */
    public Location getPosition();

    /**
     * set the element's name
     * @param name
     */
    public void setName(String name);

    /**
     *
     * @return the name
     */
    public String getName();

}
