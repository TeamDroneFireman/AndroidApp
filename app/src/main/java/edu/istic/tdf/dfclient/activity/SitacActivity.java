package edu.istic.tdf.dfclient.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.fragment.LoginFragment;
import edu.istic.tdf.dfclient.fragment.SitacFragment;

public class SitacActivity extends AppCompatActivity implements SitacFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitac);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.sitac_container, SitacFragment.newInstance())
                .commit();

    }
}
