package edu.istic.tdf.dfclient.domain.element.mean;

import android.location.Location;

import edu.istic.tdf.dfclient.domain.element.Role;

/**
 *
 * InterventionMean represent truck during an intervention (
 * Created by guerin on 21/04/16.
 */
public class InterventionMean implements IInterventionMean {

    /**
     * The unique Id of this object
     */
    private String id;

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

    public InterventionMean() {
        this.state=MeanState.DEMANDED;
        this.role=Role.NONE;
        this.name="";
    }

    public InterventionMean(MeanState s, Role r,String n){
        this.state=s;
        this.role=r;
        this.name=n;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
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
    public void setPosition(Location location) {
        this.location=location;
    }

    @Override
    public Location getPosition() {
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

}
