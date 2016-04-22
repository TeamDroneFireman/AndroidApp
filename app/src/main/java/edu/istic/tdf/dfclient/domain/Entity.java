package edu.istic.tdf.dfclient.domain;

import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * {@inheritDoc}
 */
public abstract class Entity extends BaseModel implements IEntity {

    @PrimaryKey
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
