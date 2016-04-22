package edu.istic.tdf.dfclient.adapter.intervention;

import android.location.Address;

import java.util.Date;
import java.util.Iterator;

import edu.istic.tdf.dfclient.domain.element.IElement;
import edu.istic.tdf.dfclient.domain.sinister.ISinister;

/**
 * Created by btessiau on 22/04/16.
 */
public class InterventionAdapter implements IInterventionAdapter {

    private IInterventionAdapter interventionAdapter;

    @Override
    public String getId() {
        if (this.interventionAdapter != null)
        {
            return this.interventionAdapter.getId();
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setId(String id) {
        if (this.interventionAdapter != null)
        {
            this.interventionAdapter.setId(id);
        }
    }

    @Override
    public Date getCreationDate() {
        if (this.interventionAdapter != null)
        {
            return this.interventionAdapter.getCreationDate();
        }
        else
        {
            return null;
        }
    }

    @Override
    public Date getArchiveDate() {
        if (this.interventionAdapter != null)
        {
            return this.interventionAdapter.getArchiveDate();
        }
        else
        {
            return null;
        }
    }

    @Override
    public boolean isArchived() {
        if (this.interventionAdapter != null)
        {
            return this.interventionAdapter.isArchived();
        }
        else
        {
            return false;
        }
    }

    @Override
    public void archive() {
        if (this.interventionAdapter != null)
        {
            this.interventionAdapter.archive();
        }
    }

    @Override
    public ISinister getSinister() {
        if (this.interventionAdapter != null)
        {
            return this.interventionAdapter.getSinister();
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setSinister(ISinister sinister) {
        if (this.interventionAdapter != null)
        {
            this.interventionAdapter.setSinister(sinister);
        }
    }

    @Override
    public boolean addElement(IElement element) {
        if (this.interventionAdapter != null && element != null)
        {
            return this.interventionAdapter.addElement(element);
        }
        else
        {
            return false;
        }
    }

    @Override
    public boolean removeElement(IElement element) {
        if (this.interventionAdapter != null && element != null)
        {
            return this.interventionAdapter.removeElement(element);
        }
        else
        {
            return false;
        }
    }

    @Override
    public Iterator<IElement> getIteratorOnElements() {
        if (this.interventionAdapter != null)
        {
            return this.interventionAdapter.getIteratorOnElements();
        }
        else
        {
            return null;
        }
    }

    @Override
    public Address getAddress() {
        if (this.interventionAdapter != null)
        {
            return this.interventionAdapter.getAddress();
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setAddress(Address address) {
        if (this.interventionAdapter != null)
        {
            this.interventionAdapter.setAddress(address);
        }
    }

    @Override
    public void setCreationDate(Date creationDate) {
        if (this.interventionAdapter != null)
        {
            this.interventionAdapter.setCreationDate(creationDate);
        }
    }

    @Override
    public void setArchiveDate(Date archiveDate) {
        if (this.interventionAdapter != null)
        {
            this.interventionAdapter.setArchiveDate(archiveDate);
        }
    }
}
