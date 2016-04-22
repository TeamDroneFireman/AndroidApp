package edu.istic.tdf.dfclient.drawable.element;

import android.graphics.drawable.Drawable;

/**
 * Created by guerin on 22/04/16.
 */
public interface IElementDrawable {

    /**
     *
     * @return
     */
    public Drawable getPitcto();

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
