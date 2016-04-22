package edu.istic.tdf.dfclient.adapter.element.pointOfInterest;

import edu.istic.tdf.dfclient.adapter.command.ICommand;
import edu.istic.tdf.dfclient.domain.element.pointOfInterest.IPointOfInterest;

/**
 * Created by btessiau on 22/04/16.
 */
public interface IPointOfInterestAdapter extends IPointOfInterest {

    /**
     *
     * @param command add a command to the adapter
     */
    public void addCommand(ICommand command);
}
