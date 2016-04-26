package edu.istic.tdf.dfclient.observer.element.pointOfInterest;

import android.location.Location;

import java.util.Collection;

import edu.istic.tdf.dfclient.drawable.PictoFactory;
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
    public void setLocation(Location location) {
        pointOfInterest.setLocation(location);

        //notify observer
        this.executeAllCommands();
    }

    @Override
    public Location getLocation() {
        return pointOfInterest.getLocation();
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
    public IPointOfInterest getPointOfInterest() {
        return pointOfInterest;
    }

    @Override
    public void setPointOfInterest(IPointOfInterest pointOfInterest) {
        this.pointOfInterest = pointOfInterest;
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
        return this.pointOfInterest.getId();
    }

    @Override
    public void setId(String id) {
        this.pointOfInterest.setId(id);
        this.executeAllCommands();
    }
}
