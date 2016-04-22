package edu.istic.tdf.dfclient.adapter.element.pointOfInterest;

import android.location.Location;

import java.util.Collection;

import edu.istic.tdf.dfclient.adapter.command.ICommand;
import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.domain.element.pointOfInterest.IPointOfInterest;

/**
 * Created by btessiau on 22/04/16.
 */
public class PointOfInterestAdapter implements IPointOfInterestAdapter {

    /**
     * Element associate to the adapter
     */
    private IPointOfInterest pointOfInterest;

    /**
     * the collection of commands
     */
    private Collection<ICommand> commands;

    @Override
    public String getId() {
        if (this.pointOfInterest != null) {
            return this.pointOfInterest.getId();
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setId(String id) {
        if (this.pointOfInterest != null) {
            this.pointOfInterest.setId(id);

            //notify observer
            this.executeAllCommands();
        }
    }

    @Override
    public void setRole(Role role) {
        if (this.pointOfInterest != null) {
            pointOfInterest.setRole(role);

            //notify observer
            this.executeAllCommands();
        }
    }

    @Override
    public Role getRole() {
        if (this.pointOfInterest != null) {
            return pointOfInterest.getRole();
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setPosition(Location location) {
        if (this.pointOfInterest != null) {
            pointOfInterest.setPosition(location);

            //notify observer
            this.executeAllCommands();
        }
    }

    @Override
    public Location getPosition() {
        if (this.pointOfInterest != null) {
            return pointOfInterest.getPosition();
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setName(String name) {
        if (this.pointOfInterest != null)
        {
            pointOfInterest.setName(name);
            //notify observer
            this.executeAllCommands();
        }
    }

    @Override
    public String getName() {
        if (this.pointOfInterest != null)
        {
            return this.pointOfInterest.getName();
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
