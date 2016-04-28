package edu.istic.tdf.dfclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;
import edu.istic.tdf.dfclient.fragment.InterventionCreateFormFragment;
import edu.istic.tdf.dfclient.fragment.InterventionDetailFragment;
import edu.istic.tdf.dfclient.fragment.InterventionListFragment;

public class MainMenuActivity extends BaseActivity implements InterventionDetailFragment.OnFragmentInteractionListener,
        InterventionListFragment.OnFragmentInteractionListener,
        InterventionCreateFormFragment.OnFragmentInteractionListener,
        OnMapReadyCallback {

    // UI
    SupportMapFragment mapFragment; // Map
    Marker mapMarker; // Map marker

    /**
     * The right bottom fragment (intervention details)
     */
    @Inject
    InterventionDetailFragment interventionDetailFragment;

    /**
     * The left fragment (list)
     */
    @Inject
    InterventionListFragment interventionListFragment;

    /**
     * Intervention creation fragment, displayed instead of detail fragment
     */
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
                .replace(R.id.detail_container, interventionDetailFragment)
                .commit();

        // Map
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.intervention_detail_map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Called when select button is touched. Goes to the next activity
     * @param intervention The selected intervention
     */
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
    public void onInterventionSelection(Intervention intervention) {
        if(intervention != null)
        {
            // Detail fragment
            Intent intent = new Intent(this, SitacActivity.class);
            intent.putExtra("interventionId",intervention.getId());
            this.startActivity(intent);
        }
    }

    /**
     * Called when archive button is touched.
     */
    @Override
    public void onInterventionArchived() {
        interventionListFragment.loadInterventions(null);
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
                .replace(R.id.detail_container, interventionDetailFragment)
                .commit();
        interventionDetailFragment.setCurrentIntervention(intervention);

        // Map
        if(intervention.getLocation() != null && intervention.getLocation().getGeopoint() != null) {

            final LatLng location = new LatLng(intervention.getLocation().getGeopoint().getLatitude(),
                    intervention.getLocation().getGeopoint().getLongitude());
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    if(mapMarker != null) {
                        mapMarker.remove();
                    }
                    mapMarker = googleMap.addMarker(new MarkerOptions().position(location));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                    googleMap.moveCamera(CameraUpdateFactory.zoomTo(16));
                }
            });

        }
    }

    @Override
    public void onCreateIntervention() {
        interventionListFragment.loadInterventions(null);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
