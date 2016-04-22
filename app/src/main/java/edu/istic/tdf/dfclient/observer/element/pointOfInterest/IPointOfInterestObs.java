package edu.istic.tdf.dfclient.observer.element.pointOfInterest;

import edu.istic.tdf.dfclient.observer.command.ICommand;
import edu.istic.tdf.dfclient.domain.element.pointOfInterest.IPointOfInterest;

/**
 * Created by btessiau on 22/04/16.
 */
public interface IPointOfInterestObs extends IPointOfInterest {

    /**
     *
     * @param command add a command to the adapter
     */
    public void addCommand(ICommand command);
}
