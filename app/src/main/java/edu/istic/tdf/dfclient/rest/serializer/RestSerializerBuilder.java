package edu.istic.tdf.dfclient.rest.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Builds a Gson serializer with some parameters
 */
public class RestSerializerBuilder {

    /**
     * Builds a Gson serializer
     * @return A Gson serializer
     */
    public static Gson build() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .create();

        return gson;
    }
}
