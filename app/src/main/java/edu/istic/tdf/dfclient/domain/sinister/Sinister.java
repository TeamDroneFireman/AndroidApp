package edu.istic.tdf.dfclient.domain.sinister;

import com.raizlabs.android.dbflow.annotation.Table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import edu.istic.tdf.dfclient.UI.Mean;
import edu.istic.tdf.dfclient.database.TdfDatabase;
import edu.istic.tdf.dfclient.domain.Entity;
import edu.istic.tdf.dfclient.domain.element.IElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by tremo on 27/04/16.
 */
@Table(database = TdfDatabase.class)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sinister extends Entity implements ISinister{
    @Getter
    @Setter
    private String code;

    @Getter
    @Setter
    private ArrayList<MeanCount> means = new ArrayList<>();


    @Override
    public boolean addElement(MeanCount mean) {
        return this.means.add(mean);
    }

    @Override
    public boolean removeElement(MeanCount mean) {
        return this.means.remove(mean);
    }

    @Override
    public Iterator<MeanCount> getIteratorOnElements() {
        return this.means.iterator();
    }

    @Override
    public void setMeans(ArrayList<MeanCount> means){
        this.means = means;
    }

    @Override
    public ArrayList<MeanCount> getMeans(){
        return this.means;
    }

}
