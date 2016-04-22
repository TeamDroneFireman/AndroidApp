package edu.istic.tdf.dfclient.domain.element.mean.drone;

import edu.istic.tdf.dfclient.domain.element.mean.IMean;
import edu.istic.tdf.dfclient.domain.element.mean.drone.mission.IMission;

/**
 * Created by btessiau on 20/04/16.
 */
public interface IDrone extends IMean {

    /**
     *
     * @return the current mission or null if not exist
     */
    public IMission getMission();

    /**
     *
     * @param mission the new mission, must be not null
     */
    public void setMission(IMission mission);

    /**
     *
     * @return true iff the drone has a mission
     */
    public boolean hasMission();
}
