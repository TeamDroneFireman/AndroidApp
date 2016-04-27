package edu.istic.tdf.dfclient.activity;

import android.os.Bundle;

import javax.inject.Inject;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.fragment.LoginFragment;

public class LoginActivity extends BaseActivity implements LoginFragment.OnFragmentInteractionListener {

    @Inject
    LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inject dagger dependencies
        getApplicationComponent().inject(this);

        setContentView(R.layout.activity_login);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.login_container, loginFragment)
                .commit();
    }
}
