package edu.istic.tdf.dfclient.domain.element.mean.drone;

import com.google.gson.annotations.Expose;

import edu.istic.tdf.dfclient.domain.geo.Location;

import edu.istic.tdf.dfclient.domain.Entity;
import edu.istic.tdf.dfclient.domain.element.Element;
import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.domain.element.mean.MeanState;
import edu.istic.tdf.dfclient.domain.element.mean.drone.mission.IMission;
import edu.istic.tdf.dfclient.drawable.PictoFactory;

/**
 * A mean of type drone
 */
public class Drone extends Element implements IDrone {

    /**
     * represent the current mission of the drone
     */
    private IMission mission;

    /**
     * represent the state for the means table
     */
    private MeanState state;

    /**
     * represent the current action
     * But not use for the moment !
     */
    private String action;

    public Drone() {
        this.state=MeanState.ASKED;
        this.role=Role.DEFAULT;
        this.name="";
    }

    public Drone(MeanState s, Role r,String n){
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

    @Override
    public IMission getMission() {
        return this.mission;
    }

    @Override
    public void setMission(IMission mission) {
        this.mission = mission;
    }

    @Override
    public boolean hasMission() {
        return (this.mission != null);
    }
}
