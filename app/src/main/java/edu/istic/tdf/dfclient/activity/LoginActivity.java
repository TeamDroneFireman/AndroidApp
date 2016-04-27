package edu.istic.tdf.dfclient.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.auth.AuthHelper;
import edu.istic.tdf.dfclient.dao.DaoSelectionParameters;
import edu.istic.tdf.dfclient.dao.domain.InterventionMeanDao;
import edu.istic.tdf.dfclient.dao.handler.IDaoSelectReturnHandler;
import edu.istic.tdf.dfclient.dao.domain.InterventionDao;
import edu.istic.tdf.dfclient.dao.handler.IDaoWriteReturnHandler;
import edu.istic.tdf.dfclient.domain.element.mean.interventionMean.InterventionMean;
import edu.istic.tdf.dfclient.domain.geo.GeoPoint;
import edu.istic.tdf.dfclient.domain.geo.Location;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;
import edu.istic.tdf.dfclient.domain.intervention.SinisterCode;
import edu.istic.tdf.dfclient.fragment.LoginFragment;
import edu.istic.tdf.dfclient.observer.intervention.InterventionObs;
import edu.istic.tdf.dfclient.rest.handler.IRestReturnHandler;
import edu.istic.tdf.dfclient.rest.service.login.LoginRestService;
import edu.istic.tdf.dfclient.rest.service.login.response.LoginResponse;

public class LoginActivity extends BaseActivity implements LoginFragment.OnFragmentInteractionListener {

    @Inject
    LoginFragment loginFragment;

    @Inject
    InterventionDao interventionDao;

    @Inject
    InterventionMeanDao interventionMeanDao;

    private InterventionObs intervention;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inject dagger dependencies
        getApplicationComponent().inject(this);

        setContentView(R.layout.activity_login);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.login_container, loginFragment)
                .commit();

        testMaxime();
    }

    protected void testMaxime() {
        Intervention inter = Intervention.builder()
                .location(Location.builder()
                                .address("Rue de l'étang du diable, 35760 SAINT GREGOIRE")
                                .geopoint(new GeoPoint(3.1234, 2.543, 3.1))
                                .build()
                )
                .sinisterCode(SinisterCode.FDF)
                .creationDate(new Date())
                .name("Toto")
                .build();
        interventionDao.persist(inter, new IDaoWriteReturnHandler() {
            @Override
            public void onSuccess() {
                Log.i("MAXIME", "IT WORKED");
            }

            @Override
            public void onRepositoryFailure(Throwable e) {
                Log.i("MAXIME", "IT DID NOT WORKED");

            }

            @Override
            public void onRestFailure(Throwable e) {
                Log.i("MAXIME", "IT DID NOT WORKED");
            }
        });

        interventionDao.find("57207136b87e690100d7718f", new IDaoSelectReturnHandler<Intervention>() {
            @Override
            public void onRepositoryResult(Intervention r) {
            }

            @Override
            public void onRestResult(Intervention r) {
                intervention = new InterventionObs(r);
            }

            @Override
            public void onRepositoryFailure(Throwable e) {
                Log.e("MAXIME", "REPO FAILURE");
            }

            @Override
            public void onRestFailure(Throwable e) {
                Log.e("MAXIME", "REST FAILURE");
            }
        });

        interventionDao.findAll(new DaoSelectionParameters(), new IDaoSelectReturnHandler<List<Intervention>>() {
            @Override
            public void onRepositoryResult(List<Intervention> r) {
                Log.e("MAXIME", "REPO WORKED");

            }

            @Override
            public void onRestResult(List<Intervention> r) {
                Log.e("MAXIME", "REST WORKED");

            }

            @Override
            public void onRepositoryFailure(Throwable e) {
                Log.e("MAXIME", "REPO FAILURE");

            }

            @Override
            public void onRestFailure(Throwable e) {
                Log.e("MAXIME", "REST FAILURE");

            }
        });

        interventionMeanDao.findByIntervention("57207136b87e690100d7718f", new DaoSelectionParameters(), new IDaoSelectReturnHandler<List<InterventionMean>>() {
            @Override
            public void onRepositoryResult(List<InterventionMean> r) {
                Log.e("MAXIME", "REPO WORKED");

            }

            @Override
            public void onRestResult(List<InterventionMean> r) {
                Log.e("MAXIME", "REST WORKED");

            }

            @Override
            public void onRepositoryFailure(Throwable e) {
                Log.e("MAXIME", "REPO FAILURE");

            }

            @Override
            public void onRestFailure(Throwable e) {
                Log.e("MAXIME", "REST FAILURE");

            }
        });
    }
}
