package edu.istic.tdf.dfclient.observer.element.mean.interventionMean;

import java.util.Collection;

import edu.istic.tdf.dfclient.domain.element.mean.interventionMean.IInterventionMean;
import edu.istic.tdf.dfclient.observer.command.ICommand;

/**
 * Created by btessiau on 21/04/16.
 */
public interface IInterventionMeanObs extends IInterventionMean {

    /**
     *
     * @param command add a command to the adapter
     */
    public void addCommand(ICommand command);

    /**
     *
     * @return
     */
    public IInterventionMean getInterventionMean();

    /**
     *
     * @param interventionMean
     */
    public void setInterventionMean(IInterventionMean interventionMean);

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
