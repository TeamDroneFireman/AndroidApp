package edu.istic.tdf.dfclient.UI;

import edu.istic.tdf.dfclient.drawable.element.DomainType;

/**
 * Created by Alexandre on 22/04/2016.
 */
public class Tool {

    private String icon = "icone";
    private DomainType domainType = DomainType.INTERVENTIONMEAN;

    public String getTitle() {
        return title;
    }
    public DomainType getDomainType(){
        return domainType;
    }

    public void setDomainType(DomainType domainType){
        this.domainType = domainType;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    private String title = "Nom";

    public Tool(){}

    public Tool(String title){
        this.title = title;
    }
    public Tool(String title, DomainType domainType){
        this.title = title;
        this.domainType = domainType;
    }


}
