package edu.istic.tdf.dfclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import javax.inject.Inject;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;
import edu.istic.tdf.dfclient.fragment.InterventionCreateFormFragment;
import edu.istic.tdf.dfclient.fragment.InterventionDetailFragment;
import edu.istic.tdf.dfclient.fragment.InterventionListFragment;

public class MainMenuActivity extends BaseActivity implements InterventionDetailFragment.OnFragmentInteractionListener, InterventionListFragment.OnFragmentInteractionListener, InterventionCreateFormFragment.OnFragmentInteractionListener {

    @Inject
    InterventionListFragment interventionListFragment;

    @Inject
    InterventionCreateFormFragment interventionCreateFormFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Inject dagger dependencies
        getApplicationComponent().inject(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.list_container, interventionListFragment)
                .commit();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.detail_container, InterventionDetailFragment.newInstance())
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.logout_button:
                intent = new Intent(this, LoginActivity.class);
                this.startActivity(intent);
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void onInterventionSelect(Intervention intervention) {
        if(intervention != null)
        {
            Intent intent = new Intent(this, SitacActivity.class);
            intent.putExtra("interventionId",intervention.getId());
            this.startActivity(intent);
        }
    }

    @Override
    public void handleInterventionCreation() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.detail_container, interventionCreateFormFragment)
                .commit();
    }

    @Override
    public void handleInterventionSelected(Intervention intervention) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.detail_container, InterventionDetailFragment.newInstance(intervention))
                .commit();
    }

    @Override
    public void onCreateIntervention() {
        interventionListFragment.loadInterventions(null);
    }
}
