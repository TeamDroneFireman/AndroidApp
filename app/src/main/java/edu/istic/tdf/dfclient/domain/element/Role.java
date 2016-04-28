package edu.istic.tdf.dfclient.domain.element;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by btessiau on 20/04/16.
 */
public enum Role {

    PEOPLE("people", 0xFF64DD17),
    FIRE("fire", 0xFFD50000),
    WATER("water", 0xFF2962FF),
    SPECIFIC("specific", 0xFFFF6D00),
    DEFAULT("default", 0xFF000000),
    COMMAND("command", 0xFFC51162);

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
