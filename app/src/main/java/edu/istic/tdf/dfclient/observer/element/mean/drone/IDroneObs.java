package edu.istic.tdf.dfclient.observer.element.mean.drone;

import java.util.Collection;

import edu.istic.tdf.dfclient.observer.command.ICommand;
import edu.istic.tdf.dfclient.domain.element.mean.drone.IDrone;

/**
 * Created by btessiau on 22/04/16.
 */
public interface IDroneObs extends IDrone {

    /**
     *
     * @param command add a command to the adapter
     */
    public void addCommand(ICommand command);

    /**
     *
     * @return
     */
    public IDrone getDrone();

    /**
     *
     * @param drone
     */
    public void setDrone(IDrone drone);

    /**
     *
     * @return
     */
    public Collection<ICommand> getCommands();

    /**
     *
     * @param commands
     */
    public void setCommands(Collection<ICommand> commands);

}
