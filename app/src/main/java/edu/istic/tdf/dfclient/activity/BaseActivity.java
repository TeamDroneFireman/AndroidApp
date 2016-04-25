package edu.istic.tdf.dfclient.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import edu.istic.tdf.dfclient.TdfApplication;
import edu.istic.tdf.dfclient.dagger.component.ApplicationComponent;

/**
 * Created by maxime on 22/04/2016.
 *
 * Base activity that manages injection
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO : Use butterknife
        //ButterKnife.inject(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return ((TdfApplication) getApplication()).getApplicationComponent();
    }
}
