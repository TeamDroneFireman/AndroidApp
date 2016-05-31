package edu.istic.tdf.dfclient.domain.element.mean;

/**
 * Created by guerin on 20/04/16.
 */
public enum MeanState {

    ASKED,
    VALIDATED,
    ARRIVED,
    ENGAGED,
    INTRANSIT,
    RELEASED;


    public MeanState getNextMeanState(){
        switch (this){
            case ASKED : return VALIDATED;
            case VALIDATED: return ARRIVED;
            case ARRIVED:return ENGAGED;
            case ENGAGED:return RELEASED;
            case INTRANSIT:return INTRANSIT;
            default:return ASKED;
        }
    }

    public MeanState getPreviousMeanState(){
        switch (this){
            case ASKED : return ASKED;
            case VALIDATED: return ASKED;
            case ARRIVED:return VALIDATED;
            case ENGAGED:return ARRIVED;
            case RELEASED:return ENGAGED;
            case INTRANSIT:return INTRANSIT;
            default:return ASKED;
        }
    }

    public String getMeanActionAsReadableText(){
        switch (this){
            case ASKED : return "Demander le moyen";
            case VALIDATED: return "Valider le moyen";
            case ARRIVED:return "Le moyen est arrivé";
            case ENGAGED:return "Le moyen est engagé";
            case INTRANSIT:return "Le moyen est en transit";
            case RELEASED:return "Libérer le moyen";
            default:return "Etat par défaut";
        }
    }

    public String getMeanAsReadableText(){
        switch (this){
            case ASKED : return "Moyen demandé";
            case VALIDATED: return "Moyen validé";
            case ARRIVED:return "Moyen arrivé";
            case ENGAGED:return "Moyen engagé";
            case INTRANSIT:return "Moyen en transit";
            case RELEASED:return "Moyen libéré";
            default:return "Etat par défaut";
        }
    }
}
