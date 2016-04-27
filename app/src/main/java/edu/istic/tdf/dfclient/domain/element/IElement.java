package edu.istic.tdf.dfclient.domain.element;

import edu.istic.tdf.dfclient.domain.IEntity;
import edu.istic.tdf.dfclient.domain.geo.Location;
import edu.istic.tdf.dfclient.drawable.PictoFactory;

/**
 *
 * IElement is graphic element of mean_other
 * Created by btessiau on 20/04/16.
 */
public interface IElement extends IEntity {

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
    public void setLocation(Location location);

    /**
     *
     * @return the location
     */
    public Location getLocation();

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

    public PictoFactory.ElementForm getForm();
    public void setForm(PictoFactory.ElementForm form);

}
