package edu.istic.tdf.dfclient.observer.intervention;

import android.location.Address;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import edu.istic.tdf.dfclient.domain.element.IElement;
import edu.istic.tdf.dfclient.domain.intervention.IIntervention;

/**
 * Created by btessiau on 22/04/16.
 */
public class InterventionObs implements IInterventionObs {

    private IIntervention intervention;

    public InterventionObs(IIntervention intervention) {
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

    @Override
    public Collection<IElement> getElements() {
        return this.intervention.getElements();
    }

    @Override
    public void setElements(Collection<IElement> elements) {
        this.intervention.setElements(elements);
    }

    @Override
    public Date getDateCreation() {
        return this.intervention.getDateCreation();
    }

    @Override
    public void setDateCreation(Date dateCreation) {
        this.intervention.setDateCreation(dateCreation);
    }

    @Override
    public Date getDateArchived() {
        return this.intervention.getDateArchived();
    }

    @Override
    public void setDateArchived(Date dateArchived) {
        this.intervention.setDateArchived(dateArchived);
    }

    @Override
    public void setArchived(boolean archived) {
        this.intervention.setArchived(archived);
    }

    @Override
    public IIntervention getIntervention() {
        return intervention;
    }

    @Override
    public void setIntervention(IIntervention intervention) {
        this.intervention = intervention;
    }
}
