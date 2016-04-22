package edu.istic.tdf.dfclient.adapter.element.mean.interventionMean;

import android.location.Location;

import java.util.Collection;

import edu.istic.tdf.dfclient.adapter.command.ICommand;
import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.domain.element.mean.MeanState;
import edu.istic.tdf.dfclient.domain.element.mean.interventionMean.IInterventionMean;
import edu.istic.tdf.dfclient.domain.element.mean.interventionMean.InterventionMean;

/**
 * represent the observable element
 * Created by guerin on 21/04/16.
 */
public class InterventionMeanAdapter implements IInterventionMeanAdapter{

    /**
     * Element associate to the adapter
     */
    private IInterventionMean interventionMean;

    /**
     * the collection of commands
     */
    private Collection<ICommand> commands;

    public InterventionMeanAdapter(){
        this.interventionMean = new InterventionMean();
    }

    public InterventionMeanAdapter(MeanState state, Role role, String name){
        this.interventionMean=new InterventionMean(state,role,name);
    }

    public void getPicto(){
        //TODO UsinePicto.getPicto(interventionMean)
    }


    @Override
    public void setState(MeanState state) {
        if (this.interventionMean != null) {
            interventionMean.setState(state);

            //notify observer
            this.executeAllCommands();
        }
    }

    @Override
    public MeanState getState() {
        if (this.interventionMean != null) {
            return interventionMean.getState();
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setAction(String action) {
        if (this.interventionMean != null) {
            interventionMean.setAction(action);

            //notify observer
            this.executeAllCommands();
        }
    }

    @Override
    public String getAction() {
        if (this.interventionMean != null) {
            return interventionMean.getAction();
        }
        else
        {
            return null;
        }
    }

    @Override
    public String getId() {
        if (this.interventionMean != null) {
            return this.interventionMean.getId();
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setId(String id) {
        if (this.interventionMean != null) {
            this.interventionMean.setId(id);

            //notify observer
            this.executeAllCommands();
        }
    }

    @Override
    public void setRole(Role role) {
        if (this.interventionMean != null) {
            interventionMean.setRole(role);

            //notify observer
            this.executeAllCommands();
        }
    }

    @Override
    public Role getRole() {
        if (this.interventionMean != null) {
            return interventionMean.getRole();
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setPosition(Location location) {
        if (this.interventionMean != null) {
            interventionMean.setPosition(location);

            //notify observer
            this.executeAllCommands();
        }
    }

    @Override
    public Location getPosition() {
        if (this.interventionMean != null) {
            return interventionMean.getPosition();
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setName(String name) {
        if (this.interventionMean != null)
        {
            interventionMean.setName(name);
            //notify observer
            this.executeAllCommands();
        }
    }

    @Override
    public String getName() {
        if (this.interventionMean != null)
        {
            return this.interventionMean.getName();
        }
        else
        {
            return null;
        }
    }

    @Override
    public void addCommand(ICommand command) {
        if (this.commands != null && command != null)
        {
            this.commands.add(command);
        }
    }

    private void executeAllCommands(){
        if (this.commands != null)
        {
            for(ICommand command:this.commands)
            {
                command.execute();
            }
        }
    }
}
