package edu.istic.tdf.dfclient.domain.element;

import android.support.v4.app.ActivityCompat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import edu.istic.tdf.dfclient.R;
import lombok.Value;

/**
 * Created by btessiau on 20/04/16.
 */
public enum Role {

    PEOPLE("Personnes/Sanitaire", 0xFF64DD17, 0xFF64DD17, 0xFF2A5D0A),
    FIRE("Extinction/Incendie", 0xFFD50000, 0xFFD50000, 0xFF550000),
    WATER("Eau", 0xFF2962FF, 0xFF2962FF, 0xFF14317F),
    SPECIFIC("Risques particuliers", 0xFFFF6D00, 0xFFFF6D00, 0xFF7F3600),
    DEFAULT("Par défaut", 0xFF000000, 0xFF000000, 0xFF4C4C4C),
    WHITE("Par défaut (blanc)", 0xFFFFFFFF, 0xFFFFFFFF, 0xFF7F7F7F),
    COMMAND("Commandement/Sectorisation", 0xFFC51162, 0xFFC51162, 0xFF450623);

    private String title;
    private int color;
    private int lightColor;
    private int darkColor;

    private Role(String title, int color, int lightColor, int darkColor){
        this.title = title;
        this.color = color;
        this.lightColor = lightColor;
        this.darkColor = darkColor;
    }

    public String getTitle(){
        return title;
    }

    public int getColor(){
        return color;
    }
    public int getLightColor(){
        return lightColor;
    }
    public int getDarkColor(){
        return darkColor;
    }

    @Override
    public String toString() {
        return title;
    }

}
