package edu.istic.tdf.dfclient.observer.intervention;

import android.location.Address;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import edu.istic.tdf.dfclient.domain.element.IElement;
import edu.istic.tdf.dfclient.domain.geo.Location;
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
    public String getName() {
        return this.intervention.getName();
    }

    @Override
    public void setName(String name) {
        this.intervention.setName(name);
    }

    @Override
    public Date getCreationDate() {
        return this.intervention.getCreationDate();
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
    public Location getLocation() {
        return this.intervention.getLocation();
    }

    @Override
    public void setLocation(Location location) {
        this.intervention.setLocation(location);
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
    public void setCreationDate(Date dateCreation) {
        this.intervention.setCreationDate(dateCreation);
    }

    @Override
    public Date getArchiveDate() {
        return this.intervention.getArchiveDate();
    }

    @Override
    public void setArchiveDate(Date dateArchived) {
        this.intervention.setArchiveDate(dateArchived);
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

    @Override
    public String getId() {
        return this.intervention.getId();
    }

    @Override
    public void setId(String id) {
        this.intervention.setId(id);
    }
}
