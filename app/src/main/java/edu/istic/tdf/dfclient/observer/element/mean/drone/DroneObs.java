package edu.istic.tdf.dfclient.observer.element.mean.drone;


import java.util.Collection;

import edu.istic.tdf.dfclient.domain.geo.Location;
import edu.istic.tdf.dfclient.drawable.PictoFactory;
import edu.istic.tdf.dfclient.observer.command.ICommand;
import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.domain.element.mean.MeanState;
import edu.istic.tdf.dfclient.domain.element.mean.drone.IDrone;
import edu.istic.tdf.dfclient.domain.element.mean.drone.mission.IMission;

/**
 * Created by btessiau on 22/04/16.
 */
public class DroneObs implements IDroneObs {

    /**
     * Element associate to the adapter
     */
    private IDrone drone;

    /**
     * the collection of commands
     */
    private Collection<ICommand> commands;

    public DroneObs(IDrone drone) {
        this.drone = drone;
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
    public void setState(MeanState state) {
        drone.setState(state);

        //notify observer
        this.executeAllCommands();
    }

    @Override
    public MeanState getState() {
        return drone.getState();
    }

    @Override
    public void setAction(String action) {
        drone.setAction(action);

        //notify observer
        this.executeAllCommands();
    }

    @Override
    public String getAction() {
        return drone.getAction();
    }

    @Override
    public void setRole(Role role) {
        drone.setRole(role);

        //notify observer
        this.executeAllCommands();
    }

    @Override
    public Role getRole() {
        return drone.getRole();
    }


    @Override
    public void setLocation(Location location) {
        drone.setLocation(location);

        //notify observer
        this.executeAllCommands();
    }

    @Override
    public Location getLocation() {
        return drone.getLocation();
    }

    @Override
    public void setName(String name) {
        drone.setName(name);
        //notify observer
        this.executeAllCommands();
    }

    @Override
    public String getName() {
        return this.drone.getName();
    }

    @Override
    public PictoFactory.ElementForm getForm() {
        return null;
    }

    @Override
    public void setForm(PictoFactory.ElementForm form) {

    }

    @Override
    public IMission getMission() {
        return this.drone.getMission();
    }

    @Override
    public void setMission(IMission mission) {
        this.drone.setMission(mission);
    }

    @Override
    public boolean hasMission() {
        return this.drone.hasMission();
    }

    @Override
    public IDrone getDrone() {
        return drone;
    }

    @Override
    public void setDrone(IDrone drone) {
        this.drone = drone;
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
        return this.getId();
    }

    @Override
    public void setId(String id) {
        this.setId(id);
        this.executeAllCommands();
    }
}
