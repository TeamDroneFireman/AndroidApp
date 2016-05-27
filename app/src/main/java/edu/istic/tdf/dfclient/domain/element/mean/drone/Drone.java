package edu.istic.tdf.dfclient.domain.element.mean.drone;

import com.raizlabs.android.dbflow.annotation.Table;

import java.util.Date;
import java.util.HashMap;

import edu.istic.tdf.dfclient.database.TdfDatabase;
import edu.istic.tdf.dfclient.domain.element.Element;
import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.domain.element.mean.MeanState;
import edu.istic.tdf.dfclient.domain.element.mean.drone.mission.Mission;
import edu.istic.tdf.dfclient.drawable.PictoFactory;

/**
 * A mean of type drone
 */
@Table(database = TdfDatabase.class)
public class Drone extends Element implements IDrone {

    /**
     * represent the list of states with the corresponding timestamp
     */
    private HashMap<MeanState, Date> states;

    /**
     * represent the current mission of the drone
     */
    private Mission mission;
    private MeanState currentState;
    /**
     * represent the current action
     * But not use for the moment !
     */
    private String action;

    public Drone() {
        super();
        this.states = new HashMap<>();
        this.currentState = MeanState.ASKED;
        this.states.put(MeanState.ASKED, new Date());
        this.states.put(MeanState.VALIDATED, null);
        this.states.put(MeanState.ARRIVED, null);
        this.states.put(MeanState.ENGAGED, null);
        this.states.put(MeanState.RELEASED, null);
        this.role=Role.DEFAULT;
        this.name="";
        this.form = PictoFactory.ElementForm.AIRMEAN;
    }

    @Override
    public void setState(MeanState state) {
        this.currentState = state;
        this.states.put(state, new Date());
    }

    @Override
    public MeanState getState() {
        /*MeanState currentState;
        if(this.states.get(MeanState.RELEASED) != null){
            currentState = MeanState.RELEASED;
        }else if (this.states.get(MeanState.ENGAGED) != null){
            currentState = MeanState.ENGAGED;
        }else if (this.states.get(MeanState.ARRIVED) != null){
            currentState = MeanState.ARRIVED;
        }else if (this.states.get(MeanState.VALIDATED) != null){
            currentState = MeanState.VALIDATED;
        }else{
            currentState = MeanState.ASKED;
        }*/
        if(this.states.get(MeanState.RELEASED)!=null){
            return MeanState.RELEASED;
        }

        return this.currentState;
    }

    @Override
    public Date getStateDate(MeanState state) {
        return this.states.get(state);
    }

    @Override
    public HashMap<MeanState, Date> getStates() {
        return this.states;
    }

    @Override
    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String getAction() {
        return this.action;
    }

    @Override
    public Mission getMission() {
        return this.mission;
    }

    @Override
    public void setMission(Mission mission) {
        this.mission = mission;
    }

    @Override
    public boolean hasMission() {
        return (this.mission != null);
    }
}
