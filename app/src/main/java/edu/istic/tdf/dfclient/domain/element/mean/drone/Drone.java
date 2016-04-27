package edu.istic.tdf.dfclient.domain.element.mean.drone;

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

    /**
     *it's the current fonctionnality of the InterventionMean: water,fire, sap...
     */
    private Role role;

    /**
     *
     * It's the name used for the GUI
     */
    private String name;

    /**
     * InterventionMean's location
     * Maybe change int the futur (the type)
     */
    private Location location;

    private PictoFactory.ElementForm form = PictoFactory.ElementForm.AIRMEAN;

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
    public void setRole(Role role) {
        this.role=role;
    }

    @Override
    public Role getRole() {
        return this.role;
    }

    @Override
    public void setLocation(Location location) {
        this.location=location;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public void setName(String name) {
        this.name=name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public PictoFactory.ElementForm getForm() {
        return form;
    }

    @Override
    public void setForm(PictoFactory.ElementForm form) {
        this.form = form;
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
