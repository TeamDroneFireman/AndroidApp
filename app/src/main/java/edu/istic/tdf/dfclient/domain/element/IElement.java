package edu.istic.tdf.dfclient.domain.element;

import android.graphics.Color;
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
    public void print();

    /**
     * set the element's color
     * @param color
     */
    public void setColor(Color color);

    /**
     * set the element's location
     * @param location
     */
    public void setPosition(Location location);

    /**
     * set the element's name
     * @param name
     */
    public void setName(String name);


}
