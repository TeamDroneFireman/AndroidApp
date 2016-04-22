package edu.istic.tdf.dfclient.domain.sinister;

import java.util.Collection;
import java.util.Iterator;

import edu.istic.tdf.dfclient.domain.element.mean.IMean;

/**
 * Created by btessiau on 20/04/16.
 */
public class Sinister implements ISinister {

    /**
     * The unique Id of this object
     */
    private String id;

    /**
     * the sinisterCode of the sinister
     */
    private SinisterCode sinisterCode;

    /**
     * the default collection of means for the defined sinisterCode
     */
    private Collection<IMean> means;

    public Sinister() {

    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public SinisterCode getSinisterCode() {
        return this.sinisterCode;
    }

    @Override
    public void setSinisterCode(SinisterCode sinisterCode) {
        this.sinisterCode = sinisterCode;
    }

    @Override
    public boolean addMean(IMean mean) {
        if (this.means != null && mean != null)
        {
            return this.means.add(mean);
        }

        return false;
    }

    @Override
    public boolean removeMean(IMean mean) {
        if (this.means != null && mean != null)
        {
            return this.means.remove(mean);
        }

        return false;
    }

    @Override
    public Iterator<IMean> getIteratorOnMeans() {
        if (means != null)
        {
            return this.means.iterator();
        }

        return null;
    }
}
