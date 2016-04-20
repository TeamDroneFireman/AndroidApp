package edu.istic.tdf.dfclient.domain;

import java.util.Iterator;

import edu.istic.tdf.dfclient.domain.element.mean.IMean;

/**
 * Created by btessiau on 20/04/16.
 */
public interface ISinister {

    /**
     *
     * @return the sinister code, example 'SAP'
     */
    public SinisterCode getSinisterCode();

    /**
     *
     * @param sinisterCode
     */
    public void setSinisterCode(SinisterCode sinisterCode);

    /**
     *
     * @param mean must be not null
     * @return true iff the collection is modified
     */
    public boolean addMean(IMean mean);

    /**
     *
     * @param mean must be not null
     * @return true iff the collection is modified
     */
    public boolean removeMean(IMean mean);

    /**
     *
     * @return an iterator on means collection
     * or null if elements collection is null
     */
    public Iterator<IMean> getIteratorOnMeans();

}
