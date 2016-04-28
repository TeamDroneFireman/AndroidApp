package edu.istic.tdf.dfclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Element;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observer;

import javax.inject.Inject;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.TdfApplication;
import edu.istic.tdf.dfclient.UI.Tool;
import edu.istic.tdf.dfclient.dao.DaoSelectionParameters;
import edu.istic.tdf.dfclient.dao.domain.InterventionDao;
import edu.istic.tdf.dfclient.dao.domain.element.DroneDao;
import edu.istic.tdf.dfclient.dao.domain.element.InterventionMeanDao;
import edu.istic.tdf.dfclient.dao.domain.element.PointOfInterestDao;
import edu.istic.tdf.dfclient.dao.handler.IDaoSelectReturnHandler;
import edu.istic.tdf.dfclient.domain.element.IElement;
import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.domain.element.mean.drone.Drone;
import edu.istic.tdf.dfclient.domain.element.mean.interventionMean.InterventionMean;
import edu.istic.tdf.dfclient.domain.element.pointOfInterest.PointOfInterest;
import edu.istic.tdf.dfclient.domain.geo.GeoPoint;
import edu.istic.tdf.dfclient.domain.geo.Location;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;
import edu.istic.tdf.dfclient.drawable.PictoFactory;
import edu.istic.tdf.dfclient.fragment.ContextualDrawerFragment;
import edu.istic.tdf.dfclient.fragment.MeansTableFragment;
import edu.istic.tdf.dfclient.fragment.SitacFragment;
import edu.istic.tdf.dfclient.fragment.ToolbarFragment;
import edu.istic.tdf.dfclient.http.exception.HttpException;
import edu.istic.tdf.dfclient.rest.handler.IRestReturnHandler;
import edu.istic.tdf.dfclient.rest.service.logout.LogoutRestService;
import edu.istic.tdf.dfclient.rest.service.logout.response.LogoutResponse;

public class SitacActivity extends BaseActivity implements
        SitacFragment.OnFragmentInteractionListener,
        ContextualDrawerFragment.OnFragmentInteractionListener,
        ToolbarFragment.OnFragmentInteractionListener,
        MeansTableFragment.OnFragmentInteractionListener{

    // UI
    private Tool selectedTool;
    private View contextualDrawer;
    private View sitacContainer;

    private SitacFragment sitacFragment;
    private ToolbarFragment toolbarFragment;
    private ContextualDrawerFragment contextualDrawerFragment;
    private MeansTableFragment meansTableFragment;

    // Data
    // TODO : Remove elemnts
    private List<Element> elements;

    private Intervention intervention;
    private Collection<Drone> drones;
    private Collection<InterventionMean> means;
    private Collection<PointOfInterest> pointsOfInterest;

    private Element selectedElement;

    private android.support.v4.app.Fragment currentFragment;

    @Inject InterventionDao interventionDao;
    @Inject DroneDao droneDao;
    @Inject InterventionMeanDao interventionMeanDao;
    @Inject PointOfInterestDao pointOfInterestDao;

    private ArrayList<Observer> observers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //intervention = createInterventionBouton();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitac);

        // Inject dagger dependencies
        getApplicationComponent().inject(this);

        // Activity title
        setTitle(getString(R.string.activity_sitac_title));

        // Load data
        String interventionId = (String) getIntent().getExtras().get("interventionId");
        DataLoader dataLoader = new DataLoader(interventionId); //"5720c3b8358423010064ca33"); // TODO : Set the real intervention id
        dataLoader.loadData();

        contextualDrawer = findViewById(R.id.contextual_drawer_container);
        sitacContainer = findViewById(R.id.sitac_container);

        sitacFragment = SitacFragment.newInstance();
        toolbarFragment = ToolbarFragment.newInstance();
        contextualDrawerFragment = ContextualDrawerFragment.newInstance();
        meansTableFragment=(MeansTableFragment.newInstance());

        observers.add(sitacFragment);
        observers.add(toolbarFragment);
        observers.add(contextualDrawerFragment);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.sitac_container, meansTableFragment, meansTableFragment.getTag())
                .hide(meansTableFragment)
                .add(R.id.sitac_container, sitacFragment, sitacFragment.getTag())
                .show(sitacFragment)
                .add(R.id.toolbar_container, toolbarFragment)
                .add(R.id.contextual_drawer_container, contextualDrawerFragment)
                .hide(contextualDrawerFragment)
                .commit();

        hideContextualDrawer();
        List<IElement> elements = new ArrayList<>();
/*
        IElement interventionMean = new InterventionMean();
        interventionMean.setName("CC3");
        interventionMean.setLocation(new Location("", new GeoPoint(48.1152739, -1.6381364, 12.0)));
*/
        IElement drone = new Drone();
        drone.setName("Drone1");
        drone.setLocation(new Location("", new GeoPoint(49.1152739, -1.6381364, 12.0)));
        drone.setForm(PictoFactory.ElementForm.AIRMEAN);

        //elements.add(interventionMean);
        elements.add(drone);
//        sitacFragment.updateElements(elements);

        currentFragment = sitacFragment;

        //add


    }

    @Override
    public void handleSelectedTool(Tool tool) {
        this.selectedTool = tool;
        Toast.makeText(SitacActivity.this, tool.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public Double getInterventionLatitude() {
        return this.intervention.getLocation().getGeopoint().getLatitude();
        //return 48.1151489;
    }

    @Override
    public Double getInterventionLongitude() {
        return this.intervention.getLocation().getGeopoint().getLongitude();
        //return -1.6380783;
    }

    @Override
    public Tool getSelectedTool() {
        return selectedTool;
    }

    @Override
    public void setSelectedElement(IElement element) {
        contextualDrawerFragment.setSelectedElement(element);
        showContextualDrawer();
    }

    @Override
    public IElement handleElementAdded(PictoFactory.ElementForm form, Double latitude, Double longitude) {

        //Todo: Rendre tout ça dynamique ;^)
        IElement drone = new Drone();
        drone.setRole(Role.DEFAULT);
        drone.setForm(form);
        drone.setId("TEST");
        drone.setName("azerty");
        drone.setLocation(new Location(null, new GeoPoint(latitude, longitude, 0)));


        this.selectedTool = null;
        contextualDrawerFragment.setSelectedElement(drone);
        showContextualDrawer();
        return drone;
    }

    @Override
    public void handleCancelSelection() {
        this.selectedTool = null;
        hideContextualDrawer();
    }

    private void showContextualDrawer(){
        getSupportFragmentManager().beginTransaction()
                .show(contextualDrawerFragment)
                .commit();
        contextualDrawer.animate().translationX(0);

    }
    private void hideContextualDrawer(){
        getSupportFragmentManager().beginTransaction()
                .hide(contextualDrawerFragment)
                .commit();
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
        Intent intent;
        switch (item.getItemId()) {

            // action with ID action_refresh was selected
            case R.id.switch_to_means_table:
                getSupportFragmentManager().beginTransaction()
                        .hide(toolbarFragment)
                        .hide(contextualDrawerFragment)
                        .hide(sitacFragment)
                        .commit();
                /*intent = new Intent(this, MeansTableActivity.class);
                this.startActivity(intent);*/
                switchTo(meansTableFragment);
                break;

            case R.id.switch_to_sitac:
                getSupportFragmentManager().beginTransaction()
                        .show(toolbarFragment)
                        .show(contextualDrawerFragment)
                        .show(sitacFragment)
                        .commit();
                switchTo(sitacFragment);
                break;

            case R.id.switch_to_drones_map:
                intent = new Intent(this, DronesMapActivity.class);
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

    private void displayNetworkError() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO : This to string.xml
                Toast.makeText(SitacActivity.this, "A network error occured. Please retry in a few seconds.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void updateElement(IElement element) {
        sitacFragment.updateElement(element);
        meansTableFragment.updateElement(element);
    }



    private class DataLoader {
        private String interventionId;

        public DataLoader(String interventionId) {
            this.interventionId = interventionId;
        }

        public void loadData() {
            this.loadIntervention();
            this.loadDrones();
            this.loadMeans();
            this.loadPointsOfInterest();
        }

        private void loadIntervention() {
            SitacActivity.this.interventionDao.find(this.interventionId, new IDaoSelectReturnHandler<Intervention>() {
                @Override
                public void onRepositoryResult(Intervention r) {
                    // Nothing
                }

                @Override
                public void onRestResult(Intervention r) {
                    SitacActivity.this.intervention = r;

                    // TODO : What to do when it is loaded ?
                }

                @Override
                public void onRepositoryFailure(Throwable e) {
                    // Nothing
                }

                @Override
                public void onRestFailure(Throwable e) {
                    Log.e("INTER", " --> DNE");
                    SitacActivity.this.displayNetworkError();
                }
            });
        }

        private void loadDrones() {
            SitacActivity.this.droneDao.findByIntervention(this.interventionId, new DaoSelectionParameters(),
                    new IDaoSelectReturnHandler<List<Drone>>() {
                @Override
                public void onRepositoryResult(List<Drone> r) {
                    // Nothing
                }

                @Override
                public void onRestResult(List<Drone> r) {
                    SitacActivity.this.drones = r;

                    // TODO : What to do when these are loaded ?
                }

                @Override
                public void onRepositoryFailure(Throwable e) {
                    // Nothing
                }

                @Override
                public void onRestFailure(Throwable e) {
                    Log.e("DRONE", " --> DNE");
                    SitacActivity.this.displayNetworkError();
                }
            });
        }

        private void loadMeans() {
            SitacActivity.this.interventionMeanDao.findByIntervention(this.interventionId, new DaoSelectionParameters(),
                    new IDaoSelectReturnHandler<List<InterventionMean>>() {
                        @Override
                        public void onRepositoryResult(List<InterventionMean> r) {
                            // Nothing
                        }

                        @Override
                        public void onRestResult(List<InterventionMean> r) {
                            SitacActivity.this.means = r;

                            // TODO : What to do when these are loaded ?
                        }

                        @Override
                        public void onRepositoryFailure(Throwable e) {
                            // Nothing
                        }

                        @Override
                        public void onRestFailure(Throwable e) {
                            Log.e("MEANS", " --> DNE");
                            SitacActivity.this.displayNetworkError();
                        }
                    });
        }

        private void loadPointsOfInterest() {
            SitacActivity.this.pointOfInterestDao.findByIntervention(this.interventionId, new DaoSelectionParameters(),
                    new IDaoSelectReturnHandler<List<PointOfInterest>>() {
                        @Override
                        public void onRepositoryResult(List<PointOfInterest> r) {
                            // Nothing
                        }

                        @Override
                        public void onRestResult(List<PointOfInterest> r) {
                            SitacActivity.this.pointsOfInterest = r;
                        }

                        @Override
                        public void onRepositoryFailure(Throwable e) {
                            // Nothing
                        }

                        @Override
                        public void onRestFailure(Throwable e) {
                            Log.e("POINT", " --> DNE");
                            SitacActivity.this.displayNetworkError();
                        }
                    });
        }
    }

}
