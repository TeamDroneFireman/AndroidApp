package edu.istic.tdf.dfclient.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.auth.AuthHelper;
import edu.istic.tdf.dfclient.dao.handler.IDaoSelectReturnHandler;
import edu.istic.tdf.dfclient.dao.domain.InterventionDao;
import edu.istic.tdf.dfclient.dao.handler.IDaoWriteReturnHandler;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;
import edu.istic.tdf.dfclient.fragment.LoginFragment;
import edu.istic.tdf.dfclient.rest.handler.IRestReturnHandler;
import edu.istic.tdf.dfclient.rest.service.login.LoginRestService;
import edu.istic.tdf.dfclient.rest.service.login.response.LoginResponse;

public class LoginActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener {

    LoginFragment loginFragment;

    InterventionDao interventionDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        interventionDao = new InterventionDao();

        loginFragment = LoginFragment.newInstance();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.login_container, loginFragment)
                .commit();

        testMaxime();
    }



    public void testMaxime() {
        Intervention inter = new Intervention();
        inter.setName("Test de Michel");

        interventionDao.persist(inter, new IDaoWriteReturnHandler() {
            @Override
            public void onSuccess() {
                Log.e("MAXIME", "IT WOOOORKED !!!");
            }

            @Override
            public void onRepositoryFailure(Throwable e) {
                Log.e("MAXIME", "2 VIRGULE 21 GIGOWATTS ???");

            }

            @Override
            public void onRestFailure(Throwable e) {
                Log.e("MAXIME", "IL VA FAIRE TOUT NOIR");
            }
        });
    }
}
