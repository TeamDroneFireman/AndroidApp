package edu.istic.tdf.dfclient.domain.element.pointOfInterest;

import edu.istic.tdf.dfclient.domain.element.Element;
import edu.istic.tdf.dfclient.domain.element.ElementType;
import edu.istic.tdf.dfclient.domain.geo.Location;

import edu.istic.tdf.dfclient.domain.Entity;
import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.drawable.PictoFactory;

/**
 * represent water point, two triangles
 * Created by guerin on 21/04/16.
 */
public class PointOfInterest extends Element implements IPointOfInterest {

    @Override
    public ElementType getType() {
        return ElementType.POINT_OF_INTEREST;
    }
}
