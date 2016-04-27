package edu.istic.tdf.dfclient.domain.element.mean.interventionMean;

import java.util.Date;
import java.util.HashMap;

import edu.istic.tdf.dfclient.domain.element.Element;
import edu.istic.tdf.dfclient.domain.geo.Location;

import edu.istic.tdf.dfclient.domain.Entity;
import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.domain.element.mean.MeanState;
import edu.istic.tdf.dfclient.drawable.PictoFactory;

/**
 *
 * InterventionMean represent things like trucks... during an intervention (
 * Created by guerin on 21/04/16.
 */
public class InterventionMean extends Element implements IInterventionMean {

    /**
     * represent the state for the means table
     */
    private MeanState state;

    /**
     * represent the current action
     * But not use for the moment !
     */
    private String action;

    public InterventionMean() {
        this.state=MeanState.ASKED;
        this.role=Role.DEFAULT;
        this.name="";
    }

    public InterventionMean(MeanState s, Role r,String n){
        this.state=s;
        this.role=r;
        this.name=n;
    }

    @Override
    public void setState(MeanState state) {
        this.state=state;
    }

    @Override
    public MeanState getState() {
        return this.state;
    }

    @Override
    public void setAction(String action) {
        this.action=action;
    }

    @Override
    public String getAction() {
        return this.action;
    }


}
