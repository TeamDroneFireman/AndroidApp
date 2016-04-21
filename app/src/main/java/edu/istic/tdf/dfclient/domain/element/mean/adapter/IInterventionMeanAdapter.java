package edu.istic.tdf.dfclient.domain.element.mean.adapter;

import edu.istic.tdf.dfclient.domain.element.ICommand;
import edu.istic.tdf.dfclient.domain.element.mean.IInterventionMean;

/**
 * Created by btessiau on 21/04/16.
 */
public interface IInterventionMeanAdapter extends IInterventionMean {

    /**
     *
     * @param command add a command to the adapter
     */
    public void addCommand(ICommand command);
}
