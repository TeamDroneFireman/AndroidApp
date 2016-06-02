package edu.istic.tdf.dfclient.domain;

/**
 * A domain persistable entity
 *
 * @author maxime
 */
public interface IEntity
{
    /**
     * Returns the entity ID
     * @return The ID
     */
    String getId();

    /**
     * Sets the entity ID
     * @param id The entity ID
     */
    void setId(String id);
}
