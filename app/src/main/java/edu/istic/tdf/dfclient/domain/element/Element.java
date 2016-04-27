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
}
