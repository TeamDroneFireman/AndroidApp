package edu.istic.tdf.dfclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.fragment.DronesMapFragment;

/**
 * Created by tremo on 27/04/16.
 */
public class DronesMapActivity extends BaseActivity implements DronesMapFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drones_map);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.map_container, DronesMapFragment.newInstance())
                .commitAllowingStateLoss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drones_map_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            /*case R.id.switch_to_sitac:
                intent = new Intent(this, SitacActivity.class);
                this.startActivity(intent);
                break;*/
            case R.id.logout_button:
                logout();
                break;
            default:
                break;
        }

        return true;
    }

}
