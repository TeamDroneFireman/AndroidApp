package edu.istic.tdf.dfclient.observer.element.pointOfInterest;

import android.location.Location;

import java.util.Collection;

import edu.istic.tdf.dfclient.observer.command.ICommand;
import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.domain.element.pointOfInterest.IPointOfInterest;

/**
 * Created by btessiau on 22/04/16.
 */
public class PointOfInterestObs implements IPointOfInterestObs {

    /**
     * Element associate to the adapter
     */
    private IPointOfInterest pointOfInterest;

    /**
     * the collection of commands
     */
    private Collection<ICommand> commands;

    @Override
    public void setRole(Role role) {
        pointOfInterest.setRole(role);

        //notify observer
        this.executeAllCommands();
    }

    @Override
    public Role getRole() {
        return pointOfInterest.getRole();
    }

    @Override
    public void setPosition(Location location) {
        pointOfInterest.setPosition(location);

        //notify observer
        this.executeAllCommands();
    }

    @Override
    public Location getPosition() {
        return pointOfInterest.getPosition();
    }

    @Override
    public void setName(String name) {
        pointOfInterest.setName(name);
        //notify observer
        this.executeAllCommands();
    }

    @Override
    public String getName() {
        return this.pointOfInterest.getName();
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
