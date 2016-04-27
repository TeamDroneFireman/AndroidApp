package edu.istic.tdf.dfclient.activity;

import edu.istic.tdf.dfclient.fragment.MeansTableFragment;

import edu.istic.tdf.dfclient.domain.element.IElement;
import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.domain.element.mean.drone.Drone;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;
import edu.istic.tdf.dfclient.drawable.PictoFactory;
import edu.istic.tdf.dfclient.fragment.ContextualDrawerFragment;

import android.location.Address;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Element;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.support.v4.app.Fragment;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Observer;

import javax.annotation.Resource;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.UI.Tool;
import edu.istic.tdf.dfclient.fragment.MeansTableFragment;
import java.util.Observer;

import javax.inject.Inject;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.UI.Tool;
import edu.istic.tdf.dfclient.dao.domain.InterventionDao;
import edu.istic.tdf.dfclient.dao.handler.IDaoSelectReturnHandler;
import edu.istic.tdf.dfclient.dao.handler.IDaoWriteReturnHandler;
import edu.istic.tdf.dfclient.domain.geo.GeoPoint;
import edu.istic.tdf.dfclient.domain.geo.Location;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;
import edu.istic.tdf.dfclient.fragment.ContextualDrawerFragment;
import edu.istic.tdf.dfclient.fragment.SitacFragment;
import edu.istic.tdf.dfclient.fragment.ToolbarFragment;
import edu.istic.tdf.dfclient.observer.element.mean.drone.DroneObs;
import edu.istic.tdf.dfclient.observer.intervention.InterventionObs;
import eu.inloop.easygcm.EasyGcm;

public class SitacActivity extends BaseActivity implements
        SitacFragment.OnFragmentInteractionListener,
        ContextualDrawerFragment.OnFragmentInteractionListener,
        ToolbarFragment.OnFragmentInteractionListener,
        MeansTableFragment.OnFragmentInteractionListener{

    private Tool selectedTool;
    private View contextualDrawer;
    private View sitacContainer;

    private List<Element> elements;
    private Element selectedElement;

    private SitacFragment sitacFragment;
    private ToolbarFragment toolbarFragment;
    private ContextualDrawerFragment contextualDrawerFragment;
    private MeansTableFragment meansTableFragment;

    private android.support.v4.app.Fragment currentFragment;

    @Inject
    InterventionDao interventionDao;

    private InterventionObs intervention;

    private ArrayList<Observer> observers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //intervention = createInterventionBouton();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitac);

        // Inject dagger dependencies
        getApplicationComponent().inject(this);

        contextualDrawer = findViewById(R.id.contextual_drawer_container);
        sitacContainer = findViewById(R.id.sitac_container);

        String registrationPush = EasyGcm.getRegistrationId(this);
        Log.i("MAXIME", "Registration push : " + registrationPush);

        Intervention inter = Intervention.builder()
                .location(Location.builder()
                                .city("SAINT-GREGOIRE")
                                .postalCode("35760")
                                .street("Rue de l'étang du diable")
                                .geopoint(new GeoPoint(3.1234, 2.543, 3.1))
                                .build()
                )
                .creationDate(new Date())
                .name("Toto")
                .build();
        interventionDao.persist(inter, new IDaoWriteReturnHandler() {
            @Override
            public void onSuccess() {
                Log.i("MAXIME", "IT WORKED");
            }

            @Override
            public void onRepositoryFailure(Throwable e) {
                Log.i("MAXIME", "IT DID NOT WORKED");

            }

            @Override
            public void onRestFailure(Throwable e) {
                Log.i("MAXIME", "IT DID NOT WORKED");
            }
        });

        interventionDao.find("57207136b87e690100d7718f", new IDaoSelectReturnHandler<Intervention>() {
            @Override
            public void onRepositoryResult(Intervention r) {}

            @Override
            public void onRestResult(Intervention r) {
                intervention = new InterventionObs(r);
            }

            @Override
            public void onRepositoryFailure(Throwable e) {
                Log.e("MAXIME", "REPO FAILURE");
            }

            @Override
            public void onRestFailure(Throwable e) {
                Log.e("MAXIME", "REST FAILURE");
            }
        });

        sitacFragment = SitacFragment.newInstance();
        toolbarFragment = ToolbarFragment.newInstance();
        contextualDrawerFragment = ContextualDrawerFragment.newInstance();

        observers.add(sitacFragment);
        observers.add(toolbarFragment);
        observers.add(contextualDrawerFragment);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.sitac_container, sitacFragment, sitacFragment.getTag())
                .add(R.id.toolbar_container, toolbarFragment)
                .add(R.id.contextual_drawer_container, contextualDrawerFragment)
                .commit();

        currentFragment = sitacFragment;

    }

    @Override
    public void handleSelectedTool(Tool tool) {
        this.selectedTool = tool;
        Toast.makeText(SitacActivity.this, tool.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public Double getInterventionLatitude() {
        return 4.3;
    }

    @Override
    public Double getInterventionLongitude() {
        return -4.3;
    }

    @Override
    public Collection<IElement> getInterventionElements() {
        IElement element = new DroneObs();
        element.setForm(PictoFactory.ElementForm.MEAN_COLUMN);
        element.setRole(Role.PEOPLE);
        element.setName("DRONE");
        android.location.Location location =  new android.location.Location("");
        location.setLongitude(12.2);
        location.setLatitude(12.2);
        element.setLocation(location);
        Collection<IElement> liste = new ArrayList<>();
        liste.add(element);
        return liste;
    }

    @Override
    public Tool getSelectedTool() {
        return selectedTool;
    }

    @Override
    public void handleElementAdded() {
        this.selectedTool = null;
        showContextualDrawer();
    }

    @Override
    public void handleCancelSelection() {
        this.selectedTool = null;
        hideContextualDrawer();
    }

    private void showContextualDrawer(){
        contextualDrawer.animate().translationX(0);

    }
    private void hideContextualDrawer(){
        contextualDrawer.animate().translationX(contextualDrawer.getWidth());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sitac_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.switch_to_means_table:
                switchTo(meansTableFragment);
                break;
            case R.id.switch_to_sitac:
                switchTo(sitacFragment);
                break;
            case R.id.switch_to_drones_map:
                Intent intent = new Intent(this, DronesMapActivity.class);
                this.startActivity(intent);
                break;
            default:
                break;
        }

        return true;
    }

    private void notifyObservers(){
        for(Observer observer : observers){
            observer.notify();
        }
    }

    void switchTo (Fragment fragment)
    {
        if (fragment.isVisible())
            return;
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.setCustomAnimations(R.anim.frag_slide_in, R.anim.frag_slide_out);

        // Make sure the next view is below the current one
        fragment.getView().bringToFront();

        // Hide the current fragment
        t.hide(currentFragment);
        t.show(fragment);
        currentFragment = fragment;

        // You probably want to add the transaction to the backstack
        // so that user can use the back button
        t.addToBackStack(null);
        t.commit();
    }

}
