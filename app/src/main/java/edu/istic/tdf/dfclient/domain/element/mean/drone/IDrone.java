package edu.istic.tdf.dfclient.domain.element.mean.drone;

import edu.istic.tdf.dfclient.domain.element.mean.IMean;
import edu.istic.tdf.dfclient.domain.element.mean.drone.mission.Mission;

/**
 * A mean of type drone
 */
public interface IDrone extends IMean
{
    /**
     *
     * @return the current mission or null if not exist
     */
    Mission getMission();

    /**
     *
     * @param mission the new mission, must be not null
     */
    void setMission(Mission mission);

    /**
     *
     * @return true iff the drone has a mission
     */
    boolean hasMission();
}
