package edu.istic.tdf.dfclient.domain.intervention;

import android.location.Address;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import edu.istic.tdf.dfclient.domain.Entity;
import edu.istic.tdf.dfclient.domain.sinister.ISinister;
import edu.istic.tdf.dfclient.domain.element.IElement;

/**
 * Created by btessiau on 20/04/16.
 */
public class Intervention extends Entity implements IIntervention {

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
    private Date creationDate;

    /**
     * the Date when the intervention is archived or null if the intervention isn't archived
     */
    private Date archiveDate;

    /**
     * True if the intervention is archived
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
        return creationDate;
    }

    @Override
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public Date getArchiveDate() {
        if(this.archived && this.archiveDate != null)
        {
            return this.archiveDate;
        }

        return null;
    }

    @Override
    public void setArchiveDate(Date archiveDate) {
        this.archiveDate = archiveDate;
    }

    @Override
    public boolean isArchived() {
        return this.archived;
    }

    @Override
    public void archive() {
        this.archived = true;
        this.archiveDate = new Date();
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
