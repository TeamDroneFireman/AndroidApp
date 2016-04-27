package edu.istic.tdf.dfclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Element;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observer;

import javax.inject.Inject;

import edu.istic.tdf.dfclient.R;
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
import edu.istic.tdf.dfclient.domain.intervention.Intervention;
import edu.istic.tdf.dfclient.drawable.PictoFactory;
import edu.istic.tdf.dfclient.fragment.ContextualDrawerFragment;
import edu.istic.tdf.dfclient.fragment.MeansTableFragment;
import edu.istic.tdf.dfclient.fragment.SitacFragment;
import edu.istic.tdf.dfclient.fragment.ToolbarFragment;
import edu.istic.tdf.dfclient.observer.element.mean.drone.DroneObs;
import edu.istic.tdf.dfclient.observer.element.mean.interventionMean.InterventionMeanObs;
import edu.istic.tdf.dfclient.observer.element.pointOfInterest.PointOfInterestObs;
import eu.inloop.easygcm.EasyGcm;

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
    private Collection<DroneObs> drones;
    private Collection<InterventionMeanObs> means;
    private Collection<PointOfInterestObs> pointsOfInterest;

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

        // Load data
        DataLoader dataLoader = new DataLoader("5720c3b8358423010064ca33"); // TODO : Set the real intervention id
        dataLoader.loadData();

        contextualDrawer = findViewById(R.id.contextual_drawer_container);
        sitacContainer = findViewById(R.id.sitac_container);

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

        //add
        meansTableFragment=(MeansTableFragment.newInstance());

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
        Collection<IElement> liste = new ArrayList<>();
        /*
        IElement element = new DroneObs();
        element.setForm(PictoFactory.ElementForm.MEAN_COLUMN);
        //element.setRole(Role.COMMAND);
        //element.setRole(Role.FIRE);
        element.setName("DRONE");
        android.location.Location location =  new android.location.Location("");
        location.setLongitude(12.2);
        location.setLatitude(12.2);
        element.setLocation(location);
        liste.add(element);*/
        return liste;
    }

    @Override
    public Tool getSelectedTool() {
        return selectedTool;
    }

    @Override
    public void setSelectedElement(IElement element) {
        showContextualDrawer();
        Toast.makeText(SitacActivity.this, element.getName(), Toast.LENGTH_SHORT).show();
        contextualDrawerFragment.setSelectedElement(element);
    }

    @Override
    public IElement handleElementAdded(PictoFactory.ElementForm form) {
        IElement drone = new Drone();
        drone.setForm(form);
        drone.setName(form.toString());
        return drone;
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
        Intent intent;
        switch (item.getItemId()) {

            // action with ID action_refresh was selected
            case R.id.switch_to_means_table:
                intent = new Intent(this, MeansTableActivity.class);
                this.startActivity(intent);
                //switchTo(meansTableFragment);
                break;

            case R.id.switch_to_sitac:
                switchTo(sitacFragment);
                break;

            case R.id.switch_to_drones_map:
                intent = new Intent(this, DronesMapActivity.class);
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
                    // Wrap to observable
                    List<DroneObs> dronesObs = new ArrayList<DroneObs>();
                    for(Drone d : r) {
                        dronesObs.add(new DroneObs(d));
                    }
                    SitacActivity.this.drones = dronesObs;

                    // TODO : What to do when these are loaded ?
                }

                @Override
                public void onRepositoryFailure(Throwable e) {
                    // Nothing
                }

                @Override
                public void onRestFailure(Throwable e) {
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
                            // Wrap to observable
                            List<InterventionMeanObs> meansObs = new ArrayList<InterventionMeanObs>();
                            for(InterventionMean i : r) {
                                meansObs.add(new InterventionMeanObs());
                            }
                            SitacActivity.this.means = meansObs;

                            // TODO : What to do when these are loaded ?
                        }

                        @Override
                        public void onRepositoryFailure(Throwable e) {
                            // Nothing
                        }

                        @Override
                        public void onRestFailure(Throwable e) {
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
                            // Wrap to observable
                            List<PointOfInterestObs> poiObs = new ArrayList<PointOfInterestObs>();
                            for(PointOfInterest p : r) {
                                poiObs.add(new PointOfInterestObs(p));
                            }
                            SitacActivity.this.pointsOfInterest = poiObs;
                        }

                        @Override
                        public void onRepositoryFailure(Throwable e) {
                            // Nothing
                        }

                        @Override
                        public void onRestFailure(Throwable e) {
                            SitacActivity.this.displayNetworkError();
                        }
                    });
        }
    }

}
