package edu.istic.tdf.dfclient.domain.intervention;

import android.location.Address;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import edu.istic.tdf.dfclient.domain.Entity;
import edu.istic.tdf.dfclient.domain.element.IElement;

/**
 * Created by btessiau on 20/04/16.
 */
public class Intervention extends Entity implements IIntervention {

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
    public Date getCreationDate()
    {
        return dateCreation;
    }

    @Override
    public Date getArchivedDate() {
        return this.dateArchived;
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
    public boolean addElement(IElement element) {
        return this.elements.add(element);
    }

    @Override
    public boolean removeElement(IElement element) {
        return this.elements.remove(element);
    }

    @Override
    public Iterator<IElement> getIteratorOnElements() {
        return this.elements.iterator();
    }

    @Override
    public Address getAddress() {
        return this.address;
    }

    @Override
    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public Collection<IElement> getElements() {
        return elements;
    }

    @Override
    public void setElements(Collection<IElement> elements) {
        this.elements = elements;
    }

    @Override
    public Date getDateCreation() {
        return dateCreation;
    }

    @Override
    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    @Override
    public Date getDateArchived() {
        return dateArchived;
    }

    @Override
    public void setDateArchived(Date dateArchived) {
        this.dateArchived = dateArchived;
    }

    @Override
    public void setArchived(boolean archived) {
        this.archived = archived;
    }
}
