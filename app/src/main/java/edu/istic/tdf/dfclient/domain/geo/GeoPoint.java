package edu.istic.tdf.dfclient.domain.geo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a GeoPoint
 */
@AllArgsConstructor
@NoArgsConstructor
public class GeoPoint
{
    @Getter @Setter
    double latitude;

    @Getter @Setter
    double longitude;

    @Getter @Setter
    double altitude;
}
