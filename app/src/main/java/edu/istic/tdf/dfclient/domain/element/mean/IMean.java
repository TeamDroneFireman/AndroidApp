package edu.istic.tdf.dfclient.domain.element.mean;

import java.util.Date;
import java.util.HashMap;

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
     * Get current state
     * @return state
     */
    public MeanState getState();

    /**
     * Get timestamp for the state passed as parameter
     * @param state
     * @return
     */
    public Date getStateDate(MeanState state);

    /**
     * Get the map of all states and their timestamp
     * @return
     */
    public HashMap<MeanState, Date> getStates();

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
