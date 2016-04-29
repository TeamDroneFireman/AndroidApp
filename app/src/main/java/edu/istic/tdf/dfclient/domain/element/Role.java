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
    PEOPLE("Personnes/Sanitaire", 0xFF64DD17),
    FIRE("Extinction/Incendie", 0xFFD50000),
    WATER("Eau", 0xFF2962FF),
    SPECIFIC("Risques particuliers", 0xFFFF6D00),
    DEFAULT("Par défaut", 0xFF000000),
    COMMAND("Commandement/Sectorisation", 0xFFC51162);

    private String title;
    private int color;

    private Role(String title, int color){
        this.title = title;
        this.color = color;
    }

    public String getTitle(){
        return title;
    }

    public int getColor(){
        return color;
    }

    @Override
    public String toString() {
        return title;
    }

}
