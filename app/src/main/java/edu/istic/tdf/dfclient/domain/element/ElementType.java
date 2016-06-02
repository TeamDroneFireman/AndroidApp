package edu.istic.tdf.dfclient.domain.element;

import edu.istic.tdf.dfclient.drawable.PictoFactory;

/**
 * Created by Alexandre on 28/04/2016.
 */
public enum ElementType
{
    MEAN,
    MEAN_GROUP,
    MEAN_COLUMN,
    MEAN_OTHER,
    POINT_OF_INTEREST,
    WATERPOINT,
    AIRMEAN;

    public static ElementType getElementType(PictoFactory.ElementForm form)
    {
        switch (form)
        {
            case MEAN:
            case MEAN_PLANNED:
                return ElementType.MEAN;

            case MEAN_GROUP_PLANNED:
            case MEAN_GROUP:
                return ElementType.MEAN_GROUP;

            case MEAN_COLUMN:
            case MEAN_COLUMN_PLANNED:
                return ElementType.MEAN_COLUMN;

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
