package edu.istic.tdf.dfclient.domain.sinister;

import java.util.Iterator;
import java.util.List;

import edu.istic.tdf.dfclient.domain.IEntity;

/**
 * Created by tremo on 27/04/16.
 */
public interface ISinister extends IEntity {

    boolean addElement(String mean);

    boolean removeElement(String mean);

    Iterator<String> getIteratorOnElements();

    void setMeans(List<String> means);
}
