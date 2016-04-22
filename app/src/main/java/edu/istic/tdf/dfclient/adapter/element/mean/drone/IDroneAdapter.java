package edu.istic.tdf.dfclient.adapter.element.mean.drone;

import edu.istic.tdf.dfclient.domain.element.toRemove.ICommand;
import edu.istic.tdf.dfclient.domain.element.mean.drone.IDrone;

/**
 * Created by btessiau on 22/04/16.
 */
public interface IDroneAdapter extends IDrone {

    /**
     *
     * @param command add a command to the adapter
     */
    public void addCommand(ICommand command);
}
