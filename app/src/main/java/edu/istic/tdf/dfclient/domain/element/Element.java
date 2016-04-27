package edu.istic.tdf.dfclient.domain.element;

import edu.istic.tdf.dfclient.domain.Entity;

/**
 * Created by maxime on 27/04/2016.
 */
public abstract class Element extends Entity implements IElement {
    public void merge(Element distant) throws Exception {
        if(!this.getClass().equals(distant.getClass())) {
            throw new Exception("Cannot merge two different classes");
        }
        if(!this.getForm().equals(distant.getForm()))
            this.setForm(distant.getForm());
        if(!this.getLocation().equals(distant.getLocation()))
            this.setLocation(distant.getLocation());
        if(!this.getName().equals(distant.getName()))
            this.setName(distant.getName());
        if(!this.getRole().equals(distant.getRole()))
            this.setRole(distant.getRole());
    }
}
