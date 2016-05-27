package edu.istic.tdf.dfclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import butterknife.Bind;
import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;
import edu.istic.tdf.dfclient.fragment.InterventionCreateFormFragment;
import edu.istic.tdf.dfclient.fragment.InterventionDetailFragment;
import edu.istic.tdf.dfclient.fragment.InterventionListFragment;
import edu.istic.tdf.dfclient.fragment.InterventionWelcomeFragment;

public class MainMenuActivity extends BaseActivity implements InterventionDetailFragment.OnFragmentInteractionListener,
        InterventionListFragment.OnFragmentInteractionListener,
        InterventionCreateFormFragment.OnFragmentInteractionListener,
        InterventionWelcomeFragment.OnFragmentInteractionListener,
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

    /**
     * Intervention welcome fragment, welcomes the user
     */
    @Inject
    InterventionWelcomeFragment interventionWelcomeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        setTitle(getString(R.string.activity_main_menu_title));

        progressOverlay = findViewById(R.id.progress_overlay);


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

        displayWelcome();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_button:
                logout();
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * Called when select button is touched. Goes to the next activity
     * @param intervention The selected intervention
     */
    @Override
    public void onInterventionSelected(Intervention intervention) {
        if(intervention != null)
        {
            // Detail fragment
            Intent intent = new Intent(this, SitacActivity.class);
            intent.putExtra("interventionId",intervention.getId());
            //this.startActivity(intent);
            this.startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO
    }

    /**
     * Called when archive button is touched.
     */
    @Override
    public void onInterventionArchived() {
        interventionListFragment.loadAndDisplayInterventions(null);
    }

    @Override
    public void handleInterventionCreation() {
        hideMap();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.detail_container, interventionCreateFormFragment)
                .commit();
    }

    @Override
    public void handleInterventionSelected(Intervention intervention) {
        // Display detail in fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.detail_container, interventionDetailFragment)
                .commit();

        // Load intervention in fragment and display it
        interventionDetailFragment.setIntervention(intervention);
        interventionDetailFragment.displayIntervention();

        // Map
        if(intervention.getLocation() != null && intervention.getLocation().getGeopoint() != null) {
            showMap();
            final LatLng location = new LatLng(intervention.getLocation().getGeopoint().getLatitude(),
                    intervention.getLocation().getGeopoint().getLongitude());
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    if (mapMarker != null) {
                        mapMarker.remove();
                    }
                    mapMarker = googleMap.addMarker(new MarkerOptions().position(location));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                    googleMap.moveCamera(CameraUpdateFactory.zoomTo(16));
                }
            });

        } else {
            hideMap();
        }
    }

    @Override
    public void onCreateIntervention() {
        interventionListFragment.loadAndDisplayInterventions(null);
        displayWelcome();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public void onNewInterventionPressed() {

    }

    public void displayWelcome() {
        // Hide map
        hideMap();

        // Unselect from list
        // TODO

        // Display in fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.detail_container, interventionWelcomeFragment)
                .commit();
    }

    public void hideMap() {
        if(!mapFragment.isHidden()) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_vert, R.anim.slide_out_vert)
                    .hide(mapFragment)
                    .commit();
        }
    }

    public void showMap() {
        if(mapFragment.isHidden()) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_vert, R.anim.slide_out_vert)
                    .show(mapFragment)
                    .commit();
        }
    }
}
