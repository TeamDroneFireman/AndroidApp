package edu.istic.tdf.dfclient.push;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by maxime on 25/05/2016.
 */
public class PushSubscriptionData {
    @Getter
    @Setter
    String registrationId;

    @Getter
    @Setter
    String interventionId;
}
