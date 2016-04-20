package edu.istic.tdf.dfclient.domain;

import android.location.Address;

import java.util.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import edu.istic.tdf.dfclient.domain.element.IElement;

/**
 * Created by btessiau on 20/04/16.
 */
public class Intervention implements IIntervention {

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

    private Date dateCreation;

    public Intervention() {

        this.init();
    }

    // Initialize attributs
    private void init(){

        this.sinister = new Sinister();
        elements = new ArrayList<IElement>();
        this.dateCreation = new Date();
    }

    @Override
    public Date getCreationDate()
    {
        return dateCreation;
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

    @Override
    public void printOnMapAllElement() {
        if(this.elements != null)
        {
            Iterator<IElement> it = this.elements.iterator();
            IElement element;
            while(it.hasNext())
            {
                element = it.next();
                //render this element on map
                element.printOnMap();
            }
        }
    }

    @Override
    public void printOnMapElement(IElement element) {
        if(element != null && this.elements != null && this.elements.contains(element))
        {
            //render this element on map
            element.printOnMap();
        }
    }

}
