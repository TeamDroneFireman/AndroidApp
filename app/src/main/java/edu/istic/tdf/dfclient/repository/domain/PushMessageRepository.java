package edu.istic.tdf.dfclient.repository.domain;

import java.util.List;

import edu.istic.tdf.dfclient.database.IDbReturnHandler;
import edu.istic.tdf.dfclient.domain.PushMessage;
import edu.istic.tdf.dfclient.repository.IRepository;
import edu.istic.tdf.dfclient.repository.Repository;

/**
 * Created by tremo on 01/06/16.
 */
public class PushMessageRepository extends Repository<PushMessage> implements IRepository<PushMessage> {
    public PushMessageRepository(){
        super(PushMessage.class);
    }
}
