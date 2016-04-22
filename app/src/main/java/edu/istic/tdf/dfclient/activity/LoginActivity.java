package edu.istic.tdf.dfclient.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.dao.handler.IDaoSelectReturnHandler;
import edu.istic.tdf.dfclient.dao.domain.InterventionDao;
import edu.istic.tdf.dfclient.dao.handler.IDaoWriteReturnHandler;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;
import edu.istic.tdf.dfclient.fragment.LoginFragment;

public class LoginActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener {

    InterventionDao interventionDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.login_container, LoginFragment.newInstance())
                .commit();

        interventionDao = new InterventionDao();

        testMaxime();
    }

    @Override
    public void onLoginSuccess() {
        Intent intent = new Intent(this, MainMenuActivity.class);
        this.startActivity(intent);
    }

    @Override
    public void onLoginError(String message) {

    }

    public void testMaxime() {
        Intervention inter = new Intervention();
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
