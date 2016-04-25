package edu.istic.tdf.dfclient.drawable.element;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by guerin on 22/04/16.
 */
public interface IElementDrawable {

    /**
     *
     * @return
     */
    public Bitmap getPitcto();

    /**
     *
     * @return
     */
    public DomainType getDomainType();

    /**
     *
     * @param domainType
     */
    public void setDomainType(DomainType domainType);

}
