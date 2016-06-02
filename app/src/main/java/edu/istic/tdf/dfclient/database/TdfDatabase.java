package edu.istic.tdf.dfclient.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by maxime on 21/04/2016.
 */
@Database(name = TdfDatabase.NAME, version = TdfDatabase.VERSION)
public class TdfDatabase
{
    public static final String NAME = "TDF";

    //increment when modify table
    public static final int VERSION = 8;
}