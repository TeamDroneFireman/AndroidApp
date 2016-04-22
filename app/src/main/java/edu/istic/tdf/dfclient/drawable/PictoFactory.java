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
 * Created by guerin on 22/04/16.
 */
public class PictoFactory {

    private int defaultColor=Color.WHITE;

    private Context context;

    public PictoFactory(Context context){
        this.context=context;
    }


    public Drawable getPicto(IElement element, DomainType domainType) {

        Object defaultForm=getDefaultPicto(domainType);


        return null;
    }


    public Object getDefaultPicto(DomainType domainType) {
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
    private  Object getDefaultSensibleForm() {
        return null;
    }

    /**
     * Trieangle /\
     * @return
     */
    private Object getDefaultRiskForm() {

        return null;
    }

    /**
     * Circle
     * @return
     */
    private Object getDefaulWaterPointForm() {

        Drawable drawable=ContextCompat.getDrawable(context, R.drawable.water_point_drawable);
       // drawable.setF(defaultColor);
        drawable.mutate().setColorFilter(defaultColor, PorterDuff.Mode.MULTIPLY);

        return null;
    }

    /**
     * Square
     * @return
     */
    private Object getDefaultInterventionMeanForm() {
        return null;
    }

    /**
     * AirMean = square with two triangles
     * @return
     */
    private Object getDefaultDroneForm() {
        return null;
    }
}