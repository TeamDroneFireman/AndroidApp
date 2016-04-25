package edu.istic.tdf.dfclient.rest.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;

/**
 * Created by maxime on 22/04/2016.
 */
public class RestSerializerBuilder {
    public static Gson build() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();

        return gson;
    }
}
