package edu.istic.tdf.dfclient.domain.element.mean.adapter;

import android.location.Location;

import java.util.Collection;

import edu.istic.tdf.dfclient.domain.element.ICommand;
import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.domain.element.mean.IInterventionMean;
import edu.istic.tdf.dfclient.domain.element.mean.InterventionMean;
import edu.istic.tdf.dfclient.domain.element.mean.MeanState;

/**
 * represent the observable element
 * Created by guerin on 21/04/16.
 */
public class InterventionMeanAdapt implements IInterventionMeanAdapter{

    /**
     * Element associate to the adapter
     */
    private IInterventionMean interventionMean;

    private Collection<ICommand> commands;

    public InterventionMeanAdapt(){
        this.interventionMean = new InterventionMean();
    }

    public InterventionMeanAdapt(MeanState state,Role role,String name){
        this.interventionMean=new InterventionMean(state,role,name);
    }

    public void getPicto(){
        //TODO UsinePicto.getPicto(interventionMean)
    }


    @Override
    public void setState(MeanState state) {
        interventionMean.setState(state);

        //notify observer
        this.executeAllCommands();
    }

    @Override
    public MeanState getState() {
        return interventionMean.getState();
    }

    @Override
    public void setAction(String action) {
        interventionMean.setAction(action);

        //notify observer
        this.executeAllCommands();
    }

    @Override
    public String getAction() {
        return interventionMean.getAction();
    }

    @Override
    public String getId() {
        return this.interventionMean.getId();
    }

    @Override
    public void setId(String id) {
        this.interventionMean.setId(id);

        //notify observer
        this.executeAllCommands();
    }

    @Override
    public void setRole(Role role) {
        interventionMean.setRole(role);

        //notify observer
        this.executeAllCommands();
    }

    @Override
    public Role getRole() {
        return interventionMean.getRole();
    }

    @Override
    public void setPosition(Location location) {
        interventionMean.setPosition(location);

        //notify observer
        this.executeAllCommands();
    }

    @Override
    public Location getPosition() {
        return interventionMean.getPosition();
    }

    @Override
    public void setName(String name) {
        interventionMean.setName(name);
        //notify observer
        this.executeAllCommands();
    }

    @Override
    public String getName() {
        return interventionMean.getName();
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
