package edu.istic.tdf.dfclient.observer.element.mean.interventionMean;

import android.location.Location;

import java.util.Collection;

import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.domain.element.mean.MeanState;
import edu.istic.tdf.dfclient.domain.element.mean.interventionMean.IInterventionMean;
import edu.istic.tdf.dfclient.drawable.PictoFactory;
import edu.istic.tdf.dfclient.observer.command.ICommand;

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
    public void setLocation(Location location) {
        interventionMean.setLocation(location);

        //notify observer
        this.executeAllCommands();
    }

    @Override
    public Location getLocation() {
        return interventionMean.getLocation();
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
    public PictoFactory.ElementForm getForm() {
        return null;
    }

    @Override
    public void setForm(PictoFactory.ElementForm form) {

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

    @Override
    public IInterventionMean getInterventionMean() {
        return interventionMean;
    }

    @Override
    public void setInterventionMean(IInterventionMean interventionMean) {
        this.interventionMean = interventionMean;
    }

    @Override
    public Collection<ICommand> getCommands() {
        return commands;
    }

    @Override
    public void setCommands(Collection<ICommand> commands) {
        this.commands = commands;
    }

    @Override
    public String getId() {
        return this.interventionMean.getId();
    }

    @Override
    public void setId(String id) {
        this.interventionMean.setId(id);
        this.executeAllCommands();
    }
}
