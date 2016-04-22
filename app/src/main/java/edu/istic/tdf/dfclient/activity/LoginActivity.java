package edu.istic.tdf.dfclient.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.dao.IDaoCallback;
import edu.istic.tdf.dfclient.dao.SinisterDao;
import edu.istic.tdf.dfclient.domain.Sinister;
import edu.istic.tdf.dfclient.fragment.LoginFragment;
import edu.istic.tdf.dfclient.rest.IRestReturnHandler;
import edu.istic.tdf.dfclient.rest.SinisterRestClient;

public class LoginActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener {

    SinisterRestClient sinisterClient;

    SinisterDao sinisterDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.login_container, LoginFragment.newInstance())
                .commit();

        sinisterClient = new SinisterRestClient();
        sinisterDao = new SinisterDao();

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
        sinisterDao.findAll(new IDaoCallback<List<Sinister>>() {
            @Override
            public void onRepositoryResult(List<Sinister> r) {
                Log.d("MAXIME", "Hourra Repo!!!");
            }

            @Override
            public void onRestResult(List<Sinister> r) {
                Log.d("MAXIME", "Hourra REST!!!");
            }

            @Override
            public void onFailure(Throwable e) {
                Log.d("MAXIME", "Oooooohhhh....");
            }
        });
    }
}
