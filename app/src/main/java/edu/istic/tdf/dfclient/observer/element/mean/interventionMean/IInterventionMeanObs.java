package edu.istic.tdf.dfclient.observer.element.mean.interventionMean;

import edu.istic.tdf.dfclient.observer.command.ICommand;
import edu.istic.tdf.dfclient.domain.element.mean.interventionMean.IInterventionMean;

/**
 * Created by btessiau on 21/04/16.
 */
public interface IInterventionMeanObs extends IInterventionMean {

    /**
     *
     * @param command add a command to the adapter
     */
    public void addCommand(ICommand command);
}
