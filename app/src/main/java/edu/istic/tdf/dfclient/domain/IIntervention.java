package edu.istic.tdf.dfclient.domain;

import android.location.Address;

import java.util.Iterator;

import edu.istic.tdf.dfclient.domain.element.IElement;

/**
 * Created by btessiau on 20/04/16.
 */
public interface IIntervention {

    /**
     *
     * @return the sinister including the default means list
     */
    public ISinister getSinister();

    /**
     *
     * @param sinister you want to associate with your intervention
     * sinister must be not null
     */
    public void setSinister(ISinister sinister);

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

}
