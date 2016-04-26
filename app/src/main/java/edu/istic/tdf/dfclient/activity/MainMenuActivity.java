package edu.istic.tdf.dfclient.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.domain.intervention.IIntervention;
import edu.istic.tdf.dfclient.fragment.InterventionCreateFormFragment;
import edu.istic.tdf.dfclient.fragment.InterventionDetailFragment;
import edu.istic.tdf.dfclient.fragment.InterventionListFragment;

public class MainMenuActivity extends BaseActivity implements InterventionDetailFragment.OnFragmentInteractionListener, InterventionListFragment.OnFragmentInteractionListener, InterventionCreateFormFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.list_container, InterventionListFragment.newInstance())
                .commit();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.detail_container, InterventionDetailFragment.newInstance())
                .commit();

    }

    @Override
    public void onInterventionSelect() {
        Intent intent = new Intent(this, SitacActivity.class);
        this.startActivity(intent);
    }

    @Override
    public void handleInterventionCreation() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.detail_container, InterventionCreateFormFragment.newInstance())
                .commit();
    }

    @Override
    public void createIntervention(IIntervention intervention) {
        // TODO: 26/04/16
        Toast t = Toast.makeText(this, intervention.getName(), Toast.LENGTH_LONG);
        t.getView().setBackgroundColor(Color.RED);
        t.show();

        //push intervention
    }
}
