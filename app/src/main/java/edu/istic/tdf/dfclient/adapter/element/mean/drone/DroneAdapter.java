package edu.istic.tdf.dfclient.adapter.element.mean.drone;

import android.location.Location;

import java.util.Collection;

import edu.istic.tdf.dfclient.domain.element.toRemove.ICommand;
import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.domain.element.mean.MeanState;
import edu.istic.tdf.dfclient.domain.element.mean.drone.IDrone;
import edu.istic.tdf.dfclient.domain.element.mean.drone.mission.IMission;

/**
 * Created by btessiau on 22/04/16.
 */
public class DroneAdapter implements IDroneAdapter {

    /**
     * Element associate to the adapter
     */
    private IDrone drone;

    /**
     * the collection of commands
     */
    private Collection<ICommand> commands;

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


    @Override
    public void setState(MeanState state) {
        if (this.drone != null) {
            drone.setState(state);

            //notify observer
            this.executeAllCommands();
        }
    }

    @Override
    public MeanState getState() {
        if (this.drone != null) {
            return drone.getState();
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setAction(String action) {
        if (this.drone != null) {
            drone.setAction(action);

            //notify observer
            this.executeAllCommands();
        }
    }

    @Override
    public String getAction() {
        if (this.drone != null) {
            return drone.getAction();
        }
        else
        {
            return null;
        }
    }

    @Override
    public String getId() {
        if (this.drone != null) {
            return this.drone.getId();
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setId(String id) {
        if (this.drone != null) {
            this.drone.setId(id);

            //notify observer
            this.executeAllCommands();
        }
    }

    @Override
    public void setRole(Role role) {
        if (this.drone != null) {
            drone.setRole(role);

            //notify observer
            this.executeAllCommands();
        }
    }

    @Override
    public Role getRole() {
        if (this.drone != null) {
            return drone.getRole();
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setPosition(Location location) {
        if (this.drone != null) {
            drone.setPosition(location);

            //notify observer
            this.executeAllCommands();
        }
    }

    @Override
    public Location getPosition() {
        if (this.drone != null) {
            return drone.getPosition();
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setName(String name) {
        if (this.drone != null)
        {
            drone.setName(name);
            //notify observer
            this.executeAllCommands();
        }
    }

    @Override
    public String getName() {
        if (this.drone != null)
        {
            return this.drone.getName();
        }
        else
        {
            return null;
        }
    }

    @Override
    public IMission getMission() {
        if (this.drone != null)
        {
            return this.drone.getMission();
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setMission(IMission mission) {
        if (this.drone != null)
        {
            this.drone.setMission(mission);
        }
    }

    @Override
    public boolean hasMission() {
        if (this.drone != null)
        {
            return this.drone.hasMission();
        }
        else
        {
            return false;
        }
    }
}
