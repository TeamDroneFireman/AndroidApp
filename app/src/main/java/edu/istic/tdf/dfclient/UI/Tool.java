package edu.istic.tdf.dfclient.UI;

/**
 * Created by Alexandre on 22/04/2016.
 */
public class Tool {

    private String icon = "icone";

    public String getTitle() {
        return title;
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
}
