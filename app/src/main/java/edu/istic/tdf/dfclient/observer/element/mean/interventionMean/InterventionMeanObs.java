package edu.istic.tdf.dfclient.observer.element.mean.interventionMean;

import android.location.Location;

import java.util.Collection;

import edu.istic.tdf.dfclient.observer.command.ICommand;
import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.domain.element.mean.MeanState;
import edu.istic.tdf.dfclient.domain.element.mean.interventionMean.IInterventionMean;
import edu.istic.tdf.dfclient.domain.element.mean.interventionMean.InterventionMean;

/**
 * represent the observable element
 * Created by guerin on 21/04/16.
 */
public class InterventionMeanObs implements IInterventionMeanObs {

    /**
     * Element associate to the adapter
     */
    private IInterventionMean interventionMean;

    /**
     * the collection of commands
     */
    private Collection<ICommand> commands;

    public InterventionMeanObs(){
        this.interventionMean = new InterventionMean();
    }

    public InterventionMeanObs(MeanState state, Role role, String name){
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
        return this.interventionMean.getName();
    }

    @Override
    public void addCommand(ICommand command) {
        this.commands.add(command);
    }

    private void executeAllCommands(){
        for(ICommand command:this.commands)
        {
            command.execute();
        }
    }
}
