package edu.istic.tdf.dfclient.repository.domain;

import edu.istic.tdf.dfclient.domain.image.ImageDrone;
import edu.istic.tdf.dfclient.repository.IRepository;
import edu.istic.tdf.dfclient.repository.Repository;

/**
 * Created by btessiau on 30/05/16.
 */
public class ImageDroneRepository extends Repository<ImageDrone> implements IRepository<ImageDrone> {
    public ImageDroneRepository() {
        super(ImageDrone.class);
    }
}
