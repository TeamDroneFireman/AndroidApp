package edu.istic.tdf.dfclient.UI;

import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.drawable.PictoFactory;
import edu.istic.tdf.dfclient.drawable.element.DomainType;

/**
 * Created by Alexandre on 22/04/2016.
 */
public class Tool {

    private String icon = "icone";
    private DomainType domainType = DomainType.INTERVENTIONMEAN;
    private PictoFactory.ElementForm form = PictoFactory.ElementForm.MEAN;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    private Role role = Role.WHITE;
    private String title = "Nom";

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

    public Tool(){}

    public Tool(String title){
        this.title = title;
    }

    public Tool(PictoFactory.ElementForm form, Role role){
        this.form = form;
        this.role = role;
    }

    public Tool(String title, DomainType domainType){
        this.title = title;
        this.domainType = domainType;
    }

    public PictoFactory.ElementForm getForm() {
        return form;
    }

    public void setForm(PictoFactory.ElementForm form) {
        this.form = form;
    }

}
