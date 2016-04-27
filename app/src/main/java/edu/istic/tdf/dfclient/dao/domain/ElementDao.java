package edu.istic.tdf.dfclient.dao.domain;

import java.util.List;

import edu.istic.tdf.dfclient.dao.Dao;
import edu.istic.tdf.dfclient.dao.DaoSelectionParameters;
import edu.istic.tdf.dfclient.dao.handler.IDaoSelectReturnHandler;
import edu.istic.tdf.dfclient.domain.Entity;
import edu.istic.tdf.dfclient.repository.Repository;
import edu.istic.tdf.dfclient.rest.RestClient;

/**
 * Created by maxime on 27/04/2016.
 */
public abstract class ElementDao<E extends Entity, R extends Repository<E>, C extends RestClient<E>> extends Dao<E,R,C> {
    public ElementDao(R repository, C restClient) {
        super(repository, restClient);
    }

    public void findByIntervention(String interventionId, DaoSelectionParameters selectionParameters, final IDaoSelectReturnHandler<List<E>> handler) {
        selectionParameters.addFilter("interventionId", interventionId);
        this.findAll(selectionParameters, handler);
    }
}
