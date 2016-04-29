package edu.istic.tdf.dfclient.domain.element;

import edu.istic.tdf.dfclient.drawable.PictoFactory;

/**
 * Created by Alexandre on 28/04/2016.
 */
public enum ElementType {

    MEAN,
    MEAN_OTHER,
    POINT_OF_INTEREST,
    WATERPOINT,
    AIRMEAN;

    public static ElementType getElementType(PictoFactory.ElementForm form){

        switch (form){

            case MEAN:
            case MEAN_PLANNED:
            case MEAN_GROUP:
            case MEAN_COLUMN:
                return ElementType.MEAN;

            case MEAN_OTHER:
            case MEAN_OTHER_PLANNED:
                return ElementType.MEAN_OTHER;

            case SOURCE:
            case TARGET:
                return ElementType.POINT_OF_INTEREST;

            case WATERPOINT:
            case WATERPOINT_SUPPLY:
            case WATERPOINT_SUSTAINABLE:
                return ElementType.WATERPOINT;

            case AIRMEAN:
            case AIRMEAN_PLANNED:
                return ElementType.AIRMEAN;
        }
        return ElementType.MEAN;
    }

}
