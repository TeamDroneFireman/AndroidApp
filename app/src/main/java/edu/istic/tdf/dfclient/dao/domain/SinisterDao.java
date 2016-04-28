package edu.istic.tdf.dfclient.dao.domain;

import java.util.List;

import edu.istic.tdf.dfclient.dao.Dao;
import edu.istic.tdf.dfclient.dao.DaoSelectionParameters;
import edu.istic.tdf.dfclient.dao.IDao;
import edu.istic.tdf.dfclient.dao.handler.IDaoSelectReturnHandler;
import edu.istic.tdf.dfclient.dao.handler.IDaoWriteReturnHandler;
import edu.istic.tdf.dfclient.domain.sinister.Sinister;
import edu.istic.tdf.dfclient.http.TdfHttpClient;
import edu.istic.tdf.dfclient.repository.domain.SinisterRepository;
import edu.istic.tdf.dfclient.rest.domain.SinisterRestClient;

/**
 * Created by tremo on 27/04/16.
 */
public class SinisterDao extends Dao<Sinister, SinisterRepository, SinisterRestClient>
        implements IDao<Sinister, SinisterRepository, SinisterRestClient> {

    public SinisterDao(TdfHttpClient httpClient) {
        super(new SinisterRepository(), new SinisterRestClient(httpClient));
    }

    @Override
    public void findAll(DaoSelectionParameters selectionParameters, IDaoSelectReturnHandler<List<Sinister>> result) {

    }

    @Override
    public void find(String id, IDaoSelectReturnHandler<Sinister> result) {

    }

    @Override
    public void persist(Sinister entity, IDaoWriteReturnHandler result) {

    }

    @Override
    public void delete(Sinister entity, IDaoWriteReturnHandler result) {

    }
}

