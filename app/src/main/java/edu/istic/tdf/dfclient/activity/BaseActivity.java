package edu.istic.tdf.dfclient.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import edu.istic.tdf.dfclient.TdfApplication;
import edu.istic.tdf.dfclient.dagger.component.ApplicationComponent;
import eu.inloop.easygcm.EasyGcm;

/**
 * Created by maxime on 22/04/2016.
 *
 * Base activity that manages injection
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Push
        EasyGcm.setLoggingLevel(EasyGcm.Logger.LEVEL_WARNING);
        EasyGcm.init(this);

        // UI Binding
        ButterKnife.bind(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return ((TdfApplication) getApplication()).getApplicationComponent();
    }
}
