package edu.istic.tdf.dfclient.domain.element;

import edu.istic.tdf.dfclient.domain.Entity;
import edu.istic.tdf.dfclient.domain.geo.Location;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;
import edu.istic.tdf.dfclient.drawable.PictoFactory;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by maxime on 27/04/2016.
 */
public abstract class Element extends Entity implements IElement {

    @Getter
    @Setter
    protected String intervention;

    /**
     *it's the current fonctionnality of the InterventionMean: water,fire, sap...
     */
    @Getter
    @Setter
    protected Role role;

    /**
     *
     * It's the name used for the GUI
     */
    @Getter
    @Setter
    protected String name;

    /**
     * InterventionMean's location
     * Maybe change int the futur (the type)
     */
    @Getter
    @Setter
    protected Location location;

    /**
     * The picto form
     */
    @Getter
    @Setter
    protected PictoFactory.ElementForm form;

    public ElementType getType(){

        switch (this.getForm()){

            case MEAN:
            case MEAN_PLANNED:
            case MEAN_GROUP:
            case MEAN_GROUP_PLANNED:
            case MEAN_COLUMN:
            case MEAN_COLUMN_PLANNED:
                return ElementType.MEAN;

            case MEAN_OTHER:
            case MEAN_OTHER_PLANNED:
                return ElementType.MEAN_OTHER;

            case WATERPOINT:
            case WATERPOINT_SUPPLY:
            case WATERPOINT_SUSTAINABLE:
            case SOURCE:
            case TARGET:
                return ElementType.POINT_OF_INTEREST;

            case AIRMEAN:
            case AIRMEAN_PLANNED:
                return ElementType.AIRMEAN;

        }

        return ElementType.MEAN;
    }

    public boolean isMeanFromMeanTable(){
        ElementType elementType = this.getType();
        boolean isMean = elementType == ElementType.MEAN;
        isMean |= elementType == ElementType.AIRMEAN;
        return isMean;
    }

    public void setFormFromMeanType(String meanType){
        switch(meanType){
            case "VSAV":
                this.setForm(PictoFactory.ElementForm.MEAN_PLANNED);
                break;
            case "CCGC":
                this.setForm(PictoFactory.ElementForm.MEAN_PLANNED);
                break;
            case "CCF":
                this.setForm(PictoFactory.ElementForm.MEAN_PLANNED);
                break;
            case "VLHR":
                this.setForm(PictoFactory.ElementForm.MEAN_PLANNED);
                break;
            case "FPT":
                this.setForm(PictoFactory.ElementForm.MEAN_PLANNED);
                break;
            case "EPA":
                this.setForm(PictoFactory.ElementForm.MEAN_PLANNED);
                break;
            case "VLCG":
                this.setForm(PictoFactory.ElementForm.MEAN_PLANNED);
                break;
            case "DRONE":
                this.setForm(PictoFactory.ElementForm.AIRMEAN_PLANNED);
                break;
            default:
                this.setForm(PictoFactory.ElementForm.MEAN);
                break;
        }
    }

    public void setRoleFromMeanType(String meanType){
        switch(meanType){
            case "VSAV":
                this.setRole(Role.PEOPLE);
                break;
            case "CCGC":
                this.setRole(Role.WATER);
                break;
            case "CCF":
                this.setRole(Role.WATER);
                break;
            case "VLHR":
                this.setRole(Role.WATER);
                break;
            case "FPT":
                this.setRole(Role.PEOPLE);
                break;
            case "EPA":
                this.setRole(Role.PEOPLE);
                break;
            case "VLCG":
                this.setRole(Role.PEOPLE);
                break;
            case "DRONE":
                this.setRole(Role.COMMAND);
                break;
            default:
                this.setRole(Role.DEFAULT);
                break;
        }
    }

    public static void setDefaultElementValues(Element element, String meanType, Intervention intervention){
        element.setFormFromMeanType(meanType);
        element.setRoleFromMeanType(meanType);
        element.setIntervention(intervention.getId());
        element.setName(meanType);
        element.setLocation(new Location());
    }
}
