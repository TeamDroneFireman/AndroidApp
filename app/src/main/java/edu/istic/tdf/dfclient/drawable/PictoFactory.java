package edu.istic.tdf.dfclient.drawable;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.domain.element.IElement;
import edu.istic.tdf.dfclient.drawable.element.DomainType;

/**
 * //TODO check the color modification, check the text
 *
 *
 * Created by guerin on 22/04/16.
 */
public class PictoFactory {

    private int defaultColor=Color.WHITE;

    private Context context;

    public PictoFactory(Context context){
        this.context=context;
    }


    public Drawable getPicto(IElement element, DomainType domainType) {

        Drawable defaultForm=getDefaultPicto(domainType);


        return null;
    }


    public Drawable getDefaultPicto(DomainType domainType) {
        switch (domainType){
            case DRONE: return getDefaultDroneForm();
            case INTERVENTIONMEAN:return getDefaultInterventionMeanForm();
            case WATERPOINT:return getDefaulWaterPointForm();
            case RISK:return getDefaultRiskForm();
            case SENSIBLEPOINT:return getDefaultSensibleForm();
        }

        return null;
    }

    /***
     * Triangle \/
     * @return
     */
    private  Drawable getDefaultSensibleForm() {
        Drawable drawable=ContextCompat.getDrawable(context, R.drawable.sensible);
        // drawable.setF(defaultColor);
        drawable.mutate().setColorFilter(defaultColor, PorterDuff.Mode.MULTIPLY);

        return drawable;
    }

    /**
     * Trieangle /\
     * @return
     */
    private Drawable getDefaultRiskForm() {
        Drawable drawable=ContextCompat.getDrawable(context, R.drawable.risk);
        // drawable.setF(defaultColor);
        drawable.mutate().setColorFilter(defaultColor, PorterDuff.Mode.MULTIPLY);

        return drawable;
    }

    /**
     * Circle
     * @return
     */
    private Drawable getDefaulWaterPointForm() {

        Drawable drawable=ContextCompat.getDrawable(context, R.drawable.water_point_drawable);
       // drawable.setF(defaultColor);
        drawable.mutate().setColorFilter(defaultColor, PorterDuff.Mode.MULTIPLY);

        return drawable;
    }

    /**
     * Square
     * @return
     */
    private Drawable getDefaultInterventionMeanForm() {

        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.mean);
        // drawable.setF(defaultColor);
        drawable.mutate().setColorFilter(defaultColor, PorterDuff.Mode.MULTIPLY);

        return drawable;
    }

    /**
     * AirMean = square with two triangles
     * @return
     */
    private Drawable getDefaultDroneForm() {
        Drawable drawable=ContextCompat.getDrawable(context, R.drawable.airmean);
        // drawable.setF(defaultColor);
        drawable.mutate().setColorFilter(defaultColor, PorterDuff.Mode.MULTIPLY);

        return drawable;

    }
}
