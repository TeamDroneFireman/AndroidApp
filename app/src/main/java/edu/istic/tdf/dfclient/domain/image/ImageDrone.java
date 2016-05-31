package edu.istic.tdf.dfclient.domain.image;

import com.raizlabs.android.dbflow.annotation.Table;

import java.util.Date;

import edu.istic.tdf.dfclient.database.TdfDatabase;
import edu.istic.tdf.dfclient.domain.Entity;
import edu.istic.tdf.dfclient.domain.geo.GeoPoint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by btessiau on 30/05/16.
 */
@Table(database = TdfDatabase.class)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageDrone extends Entity {

    @Getter
    @Setter
    public GeoPoint geoPoint;

    @Getter
    @Setter
    public String drone;

    @Getter
    @Setter
    public String intervention;

    @Getter
    @Setter
    public Date takenAt;

    @Getter
    @Setter
    public String link;
}
