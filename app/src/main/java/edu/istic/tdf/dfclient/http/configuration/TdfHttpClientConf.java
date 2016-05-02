package edu.istic.tdf.dfclient.http.configuration;

import edu.istic.tdf.dfclient.rest.RestEndpoints;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Created by maxime on 27/04/2016.
 */
@RequiredArgsConstructor
public class TdfHttpClientConf {

    @Getter
    int port = 80;

    @Getter
    @NonNull
    RestEndpoints restEndpoints;

}
