package edu.istic.tdf.dfclient.domain.element.mean;

import edu.istic.tdf.dfclient.domain.element.IElement;

/**
 * Created by btessiau on 20/04/16.
 */
public interface IMean extends IElement {

    /***
     * Change element's state
     * @param state
     */
    public void setState(MeanState state);

    /**
     *
     * @return state
     */
    public MeanState getState();

    /**
     * change the current action (defence, move...)
     * @param action
     */
    public void setAction(String action);

    /**
     *
     * @return action
     */
    public String getAction();
}
