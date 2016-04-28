package edu.istic.tdf.dfclient.domain.sinister;

import java.util.ArrayList;
import java.util.List;

import edu.istic.tdf.dfclient.domain.Entity;

/**
 * Created by tremo on 27/04/16.
 */
public class Sinister extends Entity implements ISinister{
    private int code;
    private List<String> means = new ArrayList<String>();

    private List<String> findMeansByCode(int code){
        return means;
    }

}
