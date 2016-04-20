package edu.istic.tdf.dfclient.domain.element.mean;

/**
 *
 * IInterventMean represent mobile mean
 * Created by btessiau on 20/04/16.
 */
public interface IInterventionMean extends IMean {


    /***
     * Change element's state
     * @param state
     */
    public void setState(MeanState state);

    /**
     * change the current action (defence, move...)
     * @param action
     */
    public void setAction(String action);
}
