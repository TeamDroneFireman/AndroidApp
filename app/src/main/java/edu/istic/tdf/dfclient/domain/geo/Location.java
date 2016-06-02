package edu.istic.tdf.dfclient.domain.geo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a location
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Location
{

    @Getter @Setter
    String address;

    @Getter @Setter
    GeoPoint geopoint;
}
