package edu.istic.tdf.dfclient.domain.element;

/**
 * Created by btessiau on 20/04/16.
 */
public interface IAction extends IElement {

    /**
     * Change the action'state
     * @param state
     */
    public void setState(ActionState state);

    /**
     *
     * @return
     */
    public ActionState getState();
}
