package edu.istic.tdf.dfclient.dagger.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Scope for entire app
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface AppScope {
}
