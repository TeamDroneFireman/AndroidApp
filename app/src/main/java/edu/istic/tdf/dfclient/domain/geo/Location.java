package edu.istic.tdf.dfclient.domain.geo;

import java.util.Locale;

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
public class Location {

    @Getter @Setter
    String postalCode;

    @Getter @Setter
    String city;

    @Getter @Setter
    String street;

    @Getter @Setter
    GeoPoint geopoint;

    public android.location.Address toAndroidAddress(Locale locale){
        android.location.Address address = new android.location.Address(locale);
        address.setPostalCode(this.getPostalCode());
        address.setLocality(this.getCity());
        address.setAddressLine(0, this.getStreet());
        address.setLatitude(this.getGeopoint().getLatitude());
        address.setLongitude(this.getGeopoint().getLongitude());

        return address;
    }

}
