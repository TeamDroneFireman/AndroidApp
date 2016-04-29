package edu.istic.tdf.dfclient.domain.sinister;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.istic.tdf.dfclient.domain.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by tremo on 27/04/16.
 */
public class Sinister extends Entity implements ISinister{
    @Getter
    @Setter
    private int code;

    @Getter
    @Setter
    private List<String> means = new ArrayList<String>();


    @Override
    public boolean addElement(String mean) {
        return this.means.add(mean);
    }

    @Override
    public boolean removeElement(String mean) {
        return this.means.remove(mean);
    }

    @Override
    public Iterator<String> getIteratorOnElements() {
        return this.means.iterator();
    }

    @Override
    public void setMeans(List<String> means){
        this.means = means;
    }

}
