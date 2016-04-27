package edu.istic.tdf.dfclient.dao;

import java.util.HashMap;
import java.util.Map;

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
     * Filters
     */
    private HashMap<String, String> filters = new HashMap<>();

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

    /**
     * Constructs a selection parameters with filters
     * @param limit The selection limit
     * @param offset The selection offset
     * @param filters selection filters
     */
    public DaoSelectionParameters(int limit, int offset, HashMap<String, String> filters) {
        this.setLimit(limit);
        this.setOffset(offset);
        this.setFilters(filters);
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

    public HashMap<String, String> getFilters() {
        return filters;
    }

    public void setFilters(HashMap<String, String> filters) {
        this.filters = filters;
    }

    public void addFilter(String key, String value) {
        this.filters.put(key, value);
    }

    public void removeFilter(String key) {
        this.filters.remove(key);
    }
}
