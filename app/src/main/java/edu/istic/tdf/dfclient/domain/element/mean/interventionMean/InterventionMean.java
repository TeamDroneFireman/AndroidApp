package edu.istic.tdf.dfclient.domain.element.mean.interventionMean;

import com.raizlabs.android.dbflow.annotation.Table;

import java.util.Date;
import java.util.HashMap;

import edu.istic.tdf.dfclient.database.TdfDatabase;
import edu.istic.tdf.dfclient.domain.element.Element;

import edu.istic.tdf.dfclient.domain.geo.Location;
import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.domain.element.mean.MeanState;
import edu.istic.tdf.dfclient.drawable.PictoFactory;

/**
 * InterventionMean represent things like trucks... during an intervention (
 * Created by guerin on 21/04/16.
 */
@Table(database = TdfDatabase.class)
public class InterventionMean extends Element implements IInterventionMean
{
    /**
     * represent the list of states with the corresponding timestamp
     */
    private HashMap<MeanState, Date> states;

    private MeanState currentState;
    /**
     * represent the current action
     * But not use for the moment !
     */
    private String action;

    public InterventionMean()
    {
        super();
        states = new HashMap<MeanState, Date>();
        this.currentState=MeanState.ASKED;
        this.states.put(MeanState.ASKED, new Date());
        this.states.put(MeanState.VALIDATED, null);
        this.states.put(MeanState.ARRIVED, null);
        this.states.put(MeanState.ENGAGED, null);
        this.states.put(MeanState.RELEASED, null);
        this.role=Role.DEFAULT;
        this.form= PictoFactory.ElementForm.MEAN;
        this.name="";
    }

    @Override
    public void setState(MeanState state)
    {
        this.currentState = state;
        if(this.states.get(state)==null){
            this.states.put(state, new Date());
        }
    }

    @Override
    public MeanState getState()
    {
        if(this.states.get(MeanState.RELEASED)!=null){
            return MeanState.RELEASED;
        }
        return this.currentState;
    }

    @Override
    public Date getStateDate(MeanState state)
    {
        return this.states.get(state);
    }

    @Override
    public HashMap<MeanState, Date> getStates()
    {
        return this.states;
    }

    @Override
    public void setAction(String action)
    {
        this.action=action;
    }

    @Override
    public String getAction()
    {
        return this.action;
    }

    @Override
    public void setRole(Role role)
    {
        this.role=role;
    }

    @Override
    public Role getRole()
    {
        return this.role;
    }

    @Override
    public void setLocation(Location location)
    {
        this.location=location;
    }

    @Override
    public Location getLocation()
    {
        return this.location;
    }

    @Override
    public void setName(String name)
    {
        this.name=name;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public PictoFactory.ElementForm getForm()
    {
        return form;
    }

    @Override
    public void setForm(PictoFactory.ElementForm form)
    {
        this.form = form;
    }

}
