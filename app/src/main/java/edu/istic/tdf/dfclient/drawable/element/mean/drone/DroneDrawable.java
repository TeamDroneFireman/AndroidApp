package edu.istic.tdf.dfclient.drawable.element.mean.drone;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import edu.istic.tdf.dfclient.domain.element.mean.drone.Drone;
import edu.istic.tdf.dfclient.drawable.element.IElementDrawable;
import edu.istic.tdf.dfclient.drawable.element.DomainType;
import edu.istic.tdf.dfclient.drawable.PictoFactory;

/**
 * Created by btessiau on 22/04/16.
 */
public class DroneDrawable extends Drone implements IElementDrawable {
    private PictoFactory pictoFactory;

    private final DomainType domainType = DomainType.DRONE;

    public DroneDrawable(Drone drone, PictoFactory pictoFactory){
        super();
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
        // TODO: 22/04/16 think if we throw an exception or let it empty
    }
}