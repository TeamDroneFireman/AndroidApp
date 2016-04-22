package edu.istic.tdf.dfclient.drawable.element.mean.drone;

import android.graphics.drawable.Drawable;

import edu.istic.tdf.dfclient.drawable.element.IElementDrawable;
import edu.istic.tdf.dfclient.observer.element.mean.drone.DroneObs;
import edu.istic.tdf.dfclient.drawable.element.DomainType;
import edu.istic.tdf.dfclient.drawable.PictoFactory;

/**
 * Created by btessiau on 22/04/16.
 */
public class DroneDrawable extends DroneObs implements IElementDrawable {
    private PictoFactory pictoFactory;

    private final DomainType domainType = DomainType.DRONE;

    public DroneDrawable(PictoFactory pictoFactory){
        this.pictoFactory=pictoFactory;
    }

    @Override
    public Drawable getPitcto() {
        return pictoFactory.getPicto(this, domainType);
    }

    public DomainType getDomainType() {
        return domainType;
    }

    public void setDomainType(DomainType domainType) {
        // TODO: 22/04/16 think if we throw an exception or let it empty
    }
}