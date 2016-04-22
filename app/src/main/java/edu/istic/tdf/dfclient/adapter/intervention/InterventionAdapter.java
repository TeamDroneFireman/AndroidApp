package edu.istic.tdf.dfclient.adapter.intervention;

import android.location.Address;

import java.util.Date;
import java.util.Iterator;

import edu.istic.tdf.dfclient.domain.element.IElement;
import edu.istic.tdf.dfclient.domain.intervention.IIntervention;

/**
 * Created by btessiau on 22/04/16.
 */
public class InterventionAdapter implements IInterventionAdapter {

    private IIntervention intervention;

    public InterventionAdapter(IIntervention intervention) {
        this.intervention = intervention;
    }

    @Override
    public Date getCreationDate() {
        return this.intervention.getCreationDate();
    }

    @Override
    public Date getArchivedDate() {
        return this.intervention.getArchivedDate();
    }

    @Override
    public boolean isArchived() {
        return this.intervention.isArchived();
    }

    @Override
    public void archive() {
        this.intervention.archive();
    }

    @Override
    public boolean addElement(IElement element) {
        return this.intervention.addElement(element);
    }

    @Override
    public boolean removeElement(IElement element) {
        return this.intervention.removeElement(element);
    }

    @Override
    public Iterator<IElement> getIteratorOnElements() {
        return this.intervention.getIteratorOnElements();
    }

    @Override
    public Address getAddress() {
        return this.intervention.getAddress();
    }

    @Override
    public void setAddress(Address address) {
        this.intervention.setAddress(address);
    }
}
