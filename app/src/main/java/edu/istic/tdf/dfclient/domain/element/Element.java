package edu.istic.tdf.dfclient.domain.element;

import edu.istic.tdf.dfclient.domain.Entity;
import edu.istic.tdf.dfclient.domain.geo.Location;
import edu.istic.tdf.dfclient.drawable.PictoFactory;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by maxime on 27/04/2016.
 */
public abstract class Element extends Entity implements IElement {

    @Getter
    @Setter
    protected String intervention;

    /**
     *it's the current fonctionnality of the InterventionMean: water,fire, sap...
     */
    @Getter
    @Setter
    protected Role role;

    /**
     *
     * It's the name used for the GUI
     */
    @Getter
    @Setter
    protected String name;

    /**
     * InterventionMean's location
     * Maybe change int the futur (the type)
     */
    @Getter
    @Setter
    protected Location location;

    /**
     * The picto form
     */
    @Getter
    @Setter
    protected PictoFactory.ElementForm form;

    public ElementType getType(){

        switch (this.getForm()){

            case MEAN:
            case MEAN_PLANNED:
            case MEAN_GROUP:
            case MEAN_COLUMN:
                return ElementType.MEAN;

            case MEAN_OTHER:
            case MEAN_OTHER_PLANNED:
                return ElementType.MEAN_OTHER;

            case WATERPOINT:
            case WATERPOINT_SUPPLY:
            case WATERPOINT_SUSTAINABLE:
            case SOURCE:
            case TARGET:
                return ElementType.POINT_OF_INTEREST;

            case AIRMEAN:
            case AIRMEAN_PLANNED:
                return ElementType.AIRMEAN;
        }

        return ElementType.MEAN;
    }

    public boolean isMeanFromMeanTable(){
        ElementType elementType = this.getType();
        boolean isMean = elementType == ElementType.MEAN;
        isMean |= elementType == ElementType.AIRMEAN;
        return isMean;
    }
}
