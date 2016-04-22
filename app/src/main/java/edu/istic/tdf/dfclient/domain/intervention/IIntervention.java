package edu.istic.tdf.dfclient.domain.intervention;

import android.location.Address;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import edu.istic.tdf.dfclient.domain.IEntity;
import edu.istic.tdf.dfclient.domain.element.IElement;

/**
 * Created by btessiau on 20/04/16.
 */
public interface IIntervention extends IEntity {
    /**
     * @return the Date of the intervention creation
     */
    public Date getCreationDate();

    /**
     * Sets the creation date
     * @param creationDate The creation date
     */
    public void setCreationDate(Date creationDate);

    /**
     *
     * @return the Date when the intervention is archived or null if the intervention isn't archived
     */
    public Date getArchiveDate();

    /**
     * Sets the archive date
     * @param archiveDate The archive date
     */
    public void setArchiveDate(Date archiveDate);

    /**
     *
     * @return true iff the intervention is archived
     */
    public boolean isArchived();

    /**
     * archive the intervention
     */
    public void archive();

    /**
     *
     * @param element must be not null
     * @return true iff the collection is modified
     */
    public boolean addElement(IElement element);

    /**
     *
     * @param element must be not null
     * @return true iff the collection is modified
     */
    public boolean removeElement(IElement element);

    /**
     *
     * @return an iterator on elements collection
     * or null if elements collection is null
     */
    public Iterator<IElement> getIteratorOnElements();

    /**
     *
     * @return the address of the intervention
     */
    public Address getAddress();

    /**
     *
     * @param address of the intervention
     */
    public void setAddress(Address address);

    /**
     *
     * @return
     */
    public Collection<IElement> getElements();

    /**
     *
     * @param elements
     */
    public void setElements(Collection<IElement> elements);

    /**
     *
     * @return
     */
    public Date getDateCreation();

    /**
     *
     * @param dateCreation
     */
    public void setDateCreation(Date dateCreation);

    /**
     *
     * @return
     */
    public Date getDateArchived();

    /**
     *
     * @param dateArchived
     */
    public void setDateArchived(Date dateArchived);

    /**
     *
     * @param archived
     */
    public void setArchived(boolean archived);
}
