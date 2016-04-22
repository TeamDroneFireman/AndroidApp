package edu.istic.tdf.dfclient.drawable.element.mean.drone;

import android.graphics.drawable.Drawable;

import edu.istic.tdf.dfclient.drawable.element.IElementDrawable;
import edu.istic.tdf.dfclient.observer.element.mean.drone.DroneObs;
import edu.istic.tdf.dfclient.drawable.element.DomaineType;
import edu.istic.tdf.dfclient.drawable.PictoFactory;

/**
 * Created by btessiau on 22/04/16.
 */
public class DroneDrawable extends DroneObs implements IElementDrawable {
    private PictoFactory pictoFactory;

    public DroneDrawable(PictoFactory pictoFactory){
        this.pictoFactory=pictoFactory;
    }

    @Override
    public Drawable getPitcto() {
        // TODO: 22/04/16
        return pictoFactory.getPicto(this, DomaineType.DRONE);
    }
}