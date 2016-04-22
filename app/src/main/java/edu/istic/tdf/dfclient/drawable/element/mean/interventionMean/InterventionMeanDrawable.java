package edu.istic.tdf.dfclient.drawable.element.mean.interventionMean;

import android.graphics.drawable.Drawable;

import edu.istic.tdf.dfclient.observer.element.mean.interventionMean.InterventionMeanObs;
import edu.istic.tdf.dfclient.drawable.element.DomaineType;
import edu.istic.tdf.dfclient.drawable.element.IElementDrawable;
import edu.istic.tdf.dfclient.drawable.PictoFactory;

/**
 * Created by guerin on 22/04/16.
 */
public class InterventionMeanDrawable extends InterventionMeanObs implements IElementDrawable {
    private PictoFactory pictoFactory;

    public InterventionMeanDrawable(PictoFactory pictoFactory){
        this.pictoFactory=pictoFactory;
    }

    @Override
    public Drawable getPitcto() {
        return pictoFactory.getPicto(this, DomaineType.INTERVENTIONMEAN);
    }
}
