package edu.istic.tdf.dfclient.dao;

import com.google.common.base.Optional;

/**
 * Represents selections parameters for a DAO layer
 */
public class DaoSelectionParameters {

    /**
     * Selection limit
     */
    private int limit = 10;

    /**
     * Selection offset
     */
    private int offset = 0;

    /**
     * Constructs a selection parameters with defaults parameters
     */
    public DaoSelectionParameters(){}

    /**
     * Constructs a selection parameters
     * @param limit The selection limit
     * @param offset The selection offset
     */
    public DaoSelectionParameters(int limit, int offset) {
        this.setLimit(limit);
        this.setOffset(offset);
    }


    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
