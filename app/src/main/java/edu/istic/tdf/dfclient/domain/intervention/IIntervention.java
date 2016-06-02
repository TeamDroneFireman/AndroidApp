package edu.istic.tdf.dfclient.domain.intervention;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import edu.istic.tdf.dfclient.domain.IEntity;
import edu.istic.tdf.dfclient.domain.element.IElement;
import edu.istic.tdf.dfclient.domain.geo.Location;

/**
 * Created by btessiau on 20/04/16.
 */
public interface IIntervention extends IEntity {
    /**
     * get the name of the intervention
     * @return
     */
    String getName();

    /**
     * set the name of the intervention
     * @param name
     */
    void setName(String name);

    /**
     *
     * @return true iff the intervention is archived
     */
    boolean isArchived();

    /**
     * archive the intervention
     */
    void archive();

    /**
     *
     * @param element must be not null
     * @return true iff the collection is modified
     */
    boolean addElement(IElement element);

    /**
     *
     * @param element must be not null
     * @return true iff the collection is modified
     */
    boolean removeElement(IElement element);

    /**
     *
     * @return an iterator on elements collection
     * or null if elements collection is null
     */
    Iterator<IElement> getIteratorOnElements();

    /**
     *
     * @return the address of the intervention
     */
    Location getLocation();

    /**
     *
     * @param location of the intervention
     */
    void setLocation(Location location);

    /**
     *
     * @return
     */
    Collection<IElement> getElements();

    /**
     *
     * @param elements
     */
    void setElements(Collection<IElement> elements);

    /**
     *
     * @return
     */
    Date getCreationDate();

    /**
     *
     * @param dateCreation
     */
    void setCreationDate(Date dateCreation);

    /**
     *
     * @return
     */
    Date getArchiveDate();

    /**
     *
     * @param dateArchived
     */
    void setArchiveDate(Date dateArchived);

    /**
     *
     * @param archived
     */
    void setArchived(boolean archived);

    /**
     *
     * @return
     */
    String getSinisterCode();

    /**
     *
     * @param sinisterCode
     */
    void setSinisterCode(String sinisterCode);
}
