package edu.istic.tdf.dfclient.domain;

import io.realm.RealmObject;

/**
 * {@inheritDoc}
 */
public abstract class Entity implements IEntity {
    abstract public String getRestEndpoint();
}
