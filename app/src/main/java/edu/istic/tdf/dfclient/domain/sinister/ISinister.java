package edu.istic.tdf.dfclient.domain.sinister;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import edu.istic.tdf.dfclient.domain.IEntity;

/**
 * Created by tremo on 27/04/16.
 */
public interface ISinister extends IEntity {

    boolean addElement(MeanCount mean);

    boolean removeElement(MeanCount mean);

    Iterator<MeanCount> getIteratorOnElements();

    void setMeans(ArrayList<MeanCount> means);

    ArrayList<MeanCount> getMeans();
}
