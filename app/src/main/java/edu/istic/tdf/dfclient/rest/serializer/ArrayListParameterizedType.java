package edu.istic.tdf.dfclient.rest.serializer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Represents an ArrayList parameterized type
 */
public class ArrayListParameterizedType implements ParameterizedType {

    private Type type;

    public ArrayListParameterizedType(Type type) {
        this.type = type;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return new Type[] {type};
    }

    @Override
    public Type getRawType() {
        return ArrayList.class;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}