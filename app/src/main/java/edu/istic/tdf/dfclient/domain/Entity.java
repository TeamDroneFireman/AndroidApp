package edu.istic.tdf.dfclient.domain;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import edu.istic.tdf.dfclient.database.TdfDatabase;

/**
 * {@inheritDoc}
 */
@Table(database = TdfDatabase.class)
public class Entity extends BaseModel implements IEntity
{
    @Column
    @PrimaryKey
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
