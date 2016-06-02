package edu.istic.tdf.dfclient.domain;

import com.raizlabs.android.dbflow.annotation.Table;

import java.util.Date;

import edu.istic.tdf.dfclient.database.TdfDatabase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Alex Tremoceiro on 01/06/16.
 *
 * This class allow us to retrieve new push from other tablets
 *
 * idElement is the id of the element to pull
 * topic is the rest to pull
 * idIntervention is the intervention where the push has been made
 * timestamp is the date of the push
 */
@Table(database = TdfDatabase.class)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PushMessage extends Entity {
    @Getter
    @Setter
    String idElement;

    @Getter
    @Setter
    String idIntervention;

    @Getter
    @Setter
    Date timestamp;

    @Getter
    @Setter
    String topic;
}
