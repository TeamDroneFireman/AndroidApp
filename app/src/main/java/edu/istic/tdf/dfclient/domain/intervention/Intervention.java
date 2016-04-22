package edu.istic.tdf.dfclient.domain.intervention;

import android.location.Address;

import com.raizlabs.android.dbflow.annotation.Table;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import edu.istic.tdf.dfclient.database.TdfDatabase;
import edu.istic.tdf.dfclient.domain.Entity;
import edu.istic.tdf.dfclient.domain.element.IElement;

/**
 * Created by btessiau on 20/04/16.
 */
@Table(database = TdfDatabase.class)
public class Intervention extends Entity implements IIntervention {

    /**
     * The intervention name
     */
    private String name;

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
     * True iff the intervention is archived
     */
    private boolean archived;

    public Intervention() {
        this.setCreationDate(new Date());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
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
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public Date getArchiveDate() {
        return archiveDate;
    }

    @Override
    public void setArchiveDate(Date archiveDate) {
        this.archiveDate = archiveDate;
    }

    @Override
    public void setArchived(boolean archived) {
        this.archived = archived;
    }
}
