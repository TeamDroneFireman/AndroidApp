package edu.istic.tdf.dfclient.drawable.element.pointOfInterest;

import android.graphics.drawable.Drawable;

import edu.istic.tdf.dfclient.drawable.element.IElementDrawable;
import edu.istic.tdf.dfclient.observer.element.pointOfInterest.PointOfInterestObs;
import edu.istic.tdf.dfclient.drawable.element.DomaineType;
import edu.istic.tdf.dfclient.drawable.PictoFactory;

/**
 * Created by btessiau on 22/04/16.
 */
public class PointOfInterestDrawable extends PointOfInterestObs implements IElementDrawable {
    private PictoFactory pictoFactory;

    private DomaineType domaineType;

    public PointOfInterestDrawable(PictoFactory pictoFactory){
        this.pictoFactory=pictoFactory;
    }

    @Override
    public Drawable getPitcto() {
        return pictoFactory.getPicto(this, domaineType);
    }

    public DomaineType getDomaineType() {
        return domaineType;
    }

    public void setDomaineType(DomaineType domaineType) {
        this.domaineType = domaineType;
    }
}