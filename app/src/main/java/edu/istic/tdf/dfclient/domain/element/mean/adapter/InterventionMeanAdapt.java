package edu.istic.tdf.dfclient.domain.element.mean.adapter;

import android.location.Location;

import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.domain.element.mean.IInterventionMean;
import edu.istic.tdf.dfclient.domain.element.mean.InterventionMean;
import edu.istic.tdf.dfclient.domain.element.mean.MeanState;

/**
 * represent the observable element
 * Created by guerin on 21/04/16.
 */
public class InterventionMeanAdapt implements IInterventionMean{


    private IInterventionMean interventionMean;

    public InterventionMeanAdapt(){
        this.interventionMean=new InterventionMean();
    }

    public InterventionMeanAdapt(MeanState state,Role role,String name){
        this.interventionMean=new InterventionMean(state,role,name);
    }

    public void getPicto(){
        //TODO UsinePicto.getPicto(interventionMean)
    }


    @Override
    public void setState(MeanState state) {
        //TODO notify observer

        interventionMean.setState(state);
    }

    @Override
    public MeanState getState() {
        return interventionMean.getState();
    }

    @Override
    public void setAction(String action) {

        //TODO notify observer

        interventionMean.setAction(action);
    }

    @Override
    public String getAction() {
        return interventionMean.getAction();
    }

    @Override
    public void setRole(Role role) {
        //TODO notify observer

        interventionMean.setRole(role);
    }

    @Override
    public Role getRole() {
        return interventionMean.getRole();
    }

    @Override
    public void setPosition(Location location) {
        //TODO notify observer

        interventionMean.setPosition(location);
    }

    @Override
    public Location getPosition() {
        return interventionMean.getPosition();
    }

    @Override
    public void setName(String name) {
        //TODO notify observer

        interventionMean.setName(name);
    }

    @Override
    public String getName() {
        return interventionMean.getName();
    }
}
