package edu.istic.tdf.dfclient.activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
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
    SupportMapFragment mapFragment;

    @Inject
    InterventionDetailFragment interventionDetailFragment;

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
                .replace(R.id.detail_container, interventionDetailFragment)
                .commit();

        // Map
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.intervention_detail_map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onInterventionSelect(Intervention intervention) {
        if(intervention != null)
        {
            // Detail fragment
            Intent intent = new Intent(this, SitacActivity.class);
            intent.putExtra("interventionId",intervention.getId());
            this.startActivity(intent);

            // Map
            final LatLng location = new LatLng(intervention.getLocation().getGeopoint().getLatitude(),
                    intervention.getLocation().getGeopoint().getLongitude());
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    googleMap.addMarker(new MarkerOptions().position(location).title("Marker in Sydney"));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                }
            });
        }
    }

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
        final Intervention interventionFinal = intervention;
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                interventionDetailFragment.setCurrentIntervention(interventionFinal);
            }
        });
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.detail_container, interventionDetailFragment)
                .commit();
    }

    @Override
    public void onCreateIntervention() {
        interventionListFragment.loadInterventions(null);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
