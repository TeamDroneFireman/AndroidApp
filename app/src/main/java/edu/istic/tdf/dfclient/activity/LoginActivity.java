package edu.istic.tdf.dfclient.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.dao.DaoSelectionParameters;
import edu.istic.tdf.dfclient.dao.IDaoReturnHandler;
import edu.istic.tdf.dfclient.dao.domain.InterventionDao;
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
        interventionDao.findAll(new DaoSelectionParameters(20,0), new IDaoReturnHandler<List<Intervention>>() {
            @Override
            public void onRepositoryResult(List<Intervention> r) {
                Log.d("MAXIME", "Hourra Repo!!!");
            }

            @Override
            public void onRestResult(List<Intervention> r) {
                Log.d("MAXIME", "Hourra REST!!!");
            }

            @Override
            public void onRepositoryFailure(Throwable e) {
                Log.d("MAXIME", "Oooooohhhh....");
            }

            @Override
            public void onRestFailure(Throwable e) {
                Log.d("MAXIME", "Oooooohhhh....");
            }
        });

        interventionDao.find("1", new IDaoReturnHandler<Intervention>() {
            @Override
            public void onRepositoryResult(Intervention r) {
                Log.d("MAXIME", "Hourra Repo!!!");

            }

            @Override
            public void onRestResult(Intervention r) {
                Log.d("MAXIME", "Hourra REST!!!");

            }

            @Override
            public void onRepositoryFailure(Throwable e) {
                Log.d("MAXIME", "Oooooohhhh....");
            }

            @Override
            public void onRestFailure(Throwable e) {
                Log.d("MAXIME", "Oooooohhhh....");
            }
        });
    }
}
