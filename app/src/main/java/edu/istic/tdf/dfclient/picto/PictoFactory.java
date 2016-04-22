package edu.istic.tdf.dfclient.picto;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.domain.element.IElement;

/**
 * Created by guerin on 22/04/16.
 */
public class PictoFactory {

    private static int defaultColor=Color.WHITE;


    public static Object getPicto(IElement element, DomaineType domaineType) {

        Object defaultForm=getDefaultPicto(domaineType);


        return null;
    }


    public static Object getDefaultPicto(DomaineType domaineType) {
        switch (domaineType){
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
    private static Object getDefaultSensibleForm() {
        return null;
    }

    /**
     * Trieangle /\
     * @return
     */
    private static Object getDefaultRiskForm() {

        return null;
    }

    /**
     * Circle
     * @return
     */
    private static Object getDefaulWaterPointForm() {
       /* Resources res = Resources.getSystem();
        //TODO récuperer le context pour le param null
        Drawable drawable;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            drawable = res.getDrawable(R.drawable.waterPointDrawable,null);
        }else{
           drawable= res.getDrawable(R.drawable.waterPointDrawable);
        }*/


        return null;
    }

    /**
     * Square
     * @return
     */
    private static Object getDefaultInterventionMeanForm() {
        return null;
    }

    /**
     * AirMean = square with two triangles
     * @return
     */
    private static Object getDefaultDroneForm() {
        return null;
    }
}
