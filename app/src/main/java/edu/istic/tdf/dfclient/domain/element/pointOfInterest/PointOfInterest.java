package edu.istic.tdf.dfclient.domain.element.pointOfInterest;

import edu.istic.tdf.dfclient.domain.element.ElementType;
import edu.istic.tdf.dfclient.domain.geo.Location;

import edu.istic.tdf.dfclient.domain.Entity;
import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.drawable.PictoFactory;

/**
 * represent water point, two triangles
 * Created by guerin on 21/04/16.
 */
public class PointOfInterest extends Entity implements IPointOfInterest {

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
        this.setForm(PictoFactory.ElementForm.SOURCE);
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
    public void setLocation(Location location) {
        this.location=location;
    }

    @Override
    public Location getLocation() {
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

    @Override
    public PictoFactory.ElementForm getForm() {
        return null;
    }

    @Override
    public void setForm(PictoFactory.ElementForm form) {

    }

    @Override
    public ElementType getType() {
        return ElementType.POINT_OF_INTEREST;
    }
}
