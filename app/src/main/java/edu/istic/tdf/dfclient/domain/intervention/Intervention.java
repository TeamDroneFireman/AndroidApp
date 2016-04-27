package edu.istic.tdf.dfclient.domain.intervention;

import com.raizlabs.android.dbflow.annotation.Table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import edu.istic.tdf.dfclient.database.TdfDatabase;
import edu.istic.tdf.dfclient.domain.Entity;
import edu.istic.tdf.dfclient.domain.element.IElement;
import edu.istic.tdf.dfclient.domain.geo.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * Created by btessiau on 20/04/16.
 */
@Table(database = TdfDatabase.class)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Intervention extends Entity implements IIntervention {

    /**
     * The intervention name
     */
    private String name;

    /**
     * The corresponding sinister code
     */
    private SinisterCode sinisterCode;

    /**
     * The collection of elements on the intervention
     */
    private Collection<IElement> elements = new ArrayList<>();

    /**
     * The address of the intervention
     */
    private Location location;

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
    public Location getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
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

    @Override
    public SinisterCode getSinisterCode() {
        return sinisterCode;
    }

    @Override
    public void setSinisterCode(SinisterCode sinisterCode) {
        this.sinisterCode = sinisterCode;
    }
}
