package edu.istic.tdf.dfclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.domain.element.Element;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;
import edu.istic.tdf.dfclient.fragment.MeansTableFragment;

public class MeansTableActivity extends BaseActivity implements MeansTableFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_means_table);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.means_table_container, MeansTableFragment.newInstance())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.means_table_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.switch_to_sitac:
                Intent intent = new Intent(this, SitacActivity.class);
                this.startActivity(intent);
                break;
            case R.id.logout_button:
                logout();
                break;
            default:
                break;
        }

        return true;
    }


    @Override
    public void handleValidation(Element element) {

    }
}
