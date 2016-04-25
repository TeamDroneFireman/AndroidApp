package edu.istic.tdf.dfclient.drawable.element.pointOfInterest;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import edu.istic.tdf.dfclient.drawable.element.IElementDrawable;
import edu.istic.tdf.dfclient.observer.element.pointOfInterest.PointOfInterestObs;
import edu.istic.tdf.dfclient.drawable.element.DomainType;
import edu.istic.tdf.dfclient.drawable.PictoFactory;

/**
 * Created by btessiau on 22/04/16.
 */
public class PointOfInterestDrawable extends PointOfInterestObs implements IElementDrawable {
    private PictoFactory pictoFactory;

    private DomainType domainType;

    public PointOfInterestDrawable(PictoFactory pictoFactory){
        this.pictoFactory=pictoFactory;
    }

    @Override
    public Bitmap getPitcto() {
        return pictoFactory.getBitMap(this, domainType);
    }

    public DomainType getDomainType() {
        return domainType;
    }

    public void setDomainType(DomainType domainType) {
        this.domainType = domainType;
    }
}