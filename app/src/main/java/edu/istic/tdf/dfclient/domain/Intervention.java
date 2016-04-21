package edu.istic.tdf.dfclient.domain;

import android.location.Address;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import edu.istic.tdf.dfclient.domain.element.IElement;

/**
 * Created by btessiau on 20/04/16.
 */
public class Intervention implements IIntervention {

    /**
     * The unique Id of this object
     */
    private String id;

    /**
     * The sinister which define the default means collection
     */
    private ISinister sinister;

    /**
     * The collection of elements on the intervention
     */
    private Collection<IElement> elements;

    /**
     * The address of the intervention
     */
    private Address address;

    /**
     * The creation date of the intervention
     */
    private Date dateCreation;

    /**
     * the Date when the intervention is archived or null if the intervention isn't archived
     */
    private Date dateArchived;

    /**
     * True iff the intervention is archived
     */
    private boolean archived;

    public Intervention() {

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
    public Date getCreationDate()
    {
        return dateCreation;
    }

    @Override
    public Date getArchivedDate() {
        if(this.archived && this.dateArchived != null)
        {
            return this.dateArchived;
        }

        return null;
    }

    @Override
    public boolean isArchived() {
        return this.archived;
    }

    @Override
    public void archive() {
        this.archived = true;
        this.dateArchived = new Date();
    }

    @Override
    public ISinister getSinister() {
        return this.sinister;
    }

    @Override
    public void setSinister(ISinister sinister) {
        this.sinister = sinister;
    }

    @Override
    public boolean addElement(IElement element) {

        if(element != null && elements != null)
        {
            return this.elements.add(element);
        }

        return false;
    }

    @Override
    public boolean removeElement(IElement element) {

        if(element != null && elements != null)
        {
            return this.elements.remove(element);
        }

        return false;
    }

    @Override
    public Iterator<IElement> getIteratorOnElements() {
        if (elements != null)
        {
            return this.elements.iterator();
        }

        return null;
    }

    @Override
    public Address getAddress() {
        return this.address;
    }

    @Override
    public void setAddress(Address address) {
        this.address = address;
    }

}
