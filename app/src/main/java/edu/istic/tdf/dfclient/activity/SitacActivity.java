package edu.istic.tdf.dfclient.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Observer;

import javax.inject.Inject;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.TdfApplication;
import edu.istic.tdf.dfclient.UI.Tool;
import edu.istic.tdf.dfclient.dao.Dao;
import edu.istic.tdf.dfclient.dao.DaoSelectionParameters;
import edu.istic.tdf.dfclient.dao.IDao;
import edu.istic.tdf.dfclient.dao.domain.ImageDroneDao;
import edu.istic.tdf.dfclient.dao.domain.InterventionDao;
import edu.istic.tdf.dfclient.dao.domain.element.DroneDao;
import edu.istic.tdf.dfclient.dao.domain.element.InterventionMeanDao;
import edu.istic.tdf.dfclient.dao.domain.element.PointOfInterestDao;
import edu.istic.tdf.dfclient.dao.handler.IDaoSelectReturnHandler;
import edu.istic.tdf.dfclient.dao.handler.IDaoWriteReturnHandler;
import edu.istic.tdf.dfclient.domain.image.ImageDrone;
import edu.istic.tdf.dfclient.domain.element.Element;
import edu.istic.tdf.dfclient.domain.element.ElementType;
import edu.istic.tdf.dfclient.domain.element.IElement;
import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.domain.element.mean.IMean;
import edu.istic.tdf.dfclient.domain.element.mean.MeanState;
import edu.istic.tdf.dfclient.domain.element.mean.drone.Drone;
import edu.istic.tdf.dfclient.domain.element.mean.drone.mission.Mission;
import edu.istic.tdf.dfclient.domain.element.mean.interventionMean.InterventionMean;
import edu.istic.tdf.dfclient.domain.element.pointOfInterest.PointOfInterest;
import edu.istic.tdf.dfclient.domain.geo.GeoPoint;
import edu.istic.tdf.dfclient.domain.geo.Location;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;
import edu.istic.tdf.dfclient.drawable.PictoFactory;
import edu.istic.tdf.dfclient.fragment.ContextualDrawerFragment;
import edu.istic.tdf.dfclient.fragment.GalleryDrawerFragment;
import edu.istic.tdf.dfclient.fragment.MeansTableFragment;
import edu.istic.tdf.dfclient.fragment.SitacFragment;
import edu.istic.tdf.dfclient.fragment.ToolbarFragment;
import edu.istic.tdf.dfclient.push.IPushCommand;

public class SitacActivity extends BaseActivity implements
        SitacFragment.OnFragmentInteractionListener,
        ContextualDrawerFragment.OnFragmentInteractionListener,
        ToolbarFragment.OnFragmentInteractionListener,
        MeansTableFragment.OnFragmentInteractionListener,
        GalleryDrawerFragment.OnFragmentInteractionListener {

    // UI
    private Tool selectedTool;
    private View contextualDrawer;
    private View galleryDrawer;
    private View sitacContainer;

    private SitacFragment sitacFragment;
    private ToolbarFragment toolbarFragment;
    private ContextualDrawerFragment contextualDrawerFragment;
    private MeansTableFragment meansTableFragment;
    private GalleryDrawerFragment galleryDrawerFragment;

    private android.support.v4.app.Fragment currentFragment;

    // Data
    private DataLoader dataLoader;

    private Intervention intervention;

    private boolean interventionIsArchived;

    private Element selectedElement;

    @Inject InterventionDao interventionDao;
    @Inject DroneDao droneDao;
    @Inject InterventionMeanDao interventionMeanDao;
    @Inject PointOfInterestDao pointOfInterestDao;
    @Inject ImageDroneDao imageDroneDao;

    /**
     * true if and only if the current user is the CODIS
     */
    private boolean isCodis;

    private ArrayList<Observer> observers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.isCodis = ((TdfApplication)this.getApplication()).loadCredentials().isCodisUser();

        //intervention = createInterventionBouton();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitac);

        // Inject dagger dependencies
        getApplicationComponent().inject(this);

        // Activity title
        setTitle(getString(R.string.activity_sitac_title));

        contextualDrawer = findViewById(R.id.contextual_drawer_container);
        sitacContainer = findViewById(R.id.sitac_container);
        galleryDrawer = findViewById(R.id.gallery_drawer_container);

        sitacFragment = SitacFragment.newInstance();
        toolbarFragment = ToolbarFragment.newInstance();
        contextualDrawerFragment = ContextualDrawerFragment.newInstance();
        meansTableFragment=(MeansTableFragment.newInstance());
        galleryDrawerFragment = GalleryDrawerFragment.newInstance();

        observers.add(sitacFragment);
        observers.add(toolbarFragment);
        observers.add(contextualDrawerFragment);
        observers.add(galleryDrawerFragment);

        //Initialize fragment in a transaction
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                .add(R.id.sitac_container, meansTableFragment, meansTableFragment.getTag())
                .hide(meansTableFragment)
                .add(R.id.sitac_container, sitacFragment, sitacFragment.getTag())
                .show(sitacFragment)
                .add(R.id.toolbar_container, toolbarFragment);

        if(this.isCodis) {
            fragmentTransaction.hide(toolbarFragment);
        }
        else
        {
            fragmentTransaction.show(toolbarFragment);
        }

        fragmentTransaction.add(R.id.contextual_drawer_container, contextualDrawerFragment)
                .hide(contextualDrawerFragment)
                .add(R.id.gallery_drawer_container, galleryDrawerFragment)
                .hide(galleryDrawerFragment);

        fragmentTransaction.commit();

        currentFragment = sitacFragment;

        // Load data
        String interventionId = (String) getIntent().getExtras().get("interventionId");
        interventionIsArchived = (boolean) getIntent().getExtras().get("interventionIsArchived");

        dataLoader = new DataLoader(interventionId);
        dataLoader.loadData();

        this.registerPushHandlers();
    }

    @Override
    public void onBackPressed() {
        if(!sitacFragment.equals(currentFragment))
        {
            if(this.isCodis || this.isInterventionArchived())
            {
                getSupportFragmentManager().beginTransaction()
                        .hide(toolbarFragment)
                        .hide(contextualDrawerFragment)
                        .hide(galleryDrawerFragment)
                        .commit();
                switchTo(sitacFragment);
            }
            else
            {
                getSupportFragmentManager().beginTransaction()
                        .show(toolbarFragment)
                        .hide(contextualDrawerFragment)
                        .hide(galleryDrawerFragment)
                        .commit();
                switchTo(sitacFragment);
            }
        }
        else
        {
            this.overridePendingTransition(R.anim.shake, R.anim.shake);
            Intent upIntent = NavUtils.getParentActivityIntent(this);
            upIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            NavUtils.navigateUpTo(this, upIntent);
        }
    }

    @Override
    public void handleSelectedToolUtils(Tool tool) {
        this.sitacFragment.cancelSelection();
        this.selectedTool = tool;
        hideContextualDrawer();
    }

    @Override
    public boolean isInterventionArchived() {
        return this.interventionIsArchived;
    }

    @Override
    public void handleImageDronesSelected(Collection<ImageDrone> imageDrones) {
        if(imageDrones != null)
        {
            this.galleryDrawerFragment.updateList(imageDrones);
        };

        showGalleryDrawer();
    }

    @Override
    public Tool getSelectedTool() {
        return selectedTool;
    }

    @Override
    public Element tryGetSelectedElement() {
        return toolbarFragment.tryGetElementFromTool(selectedTool);
    }

    @Override
    public void setSelectedElement(Element element) {
        sitacFragment.cancelSelection();

        if(element.getType() == ElementType.AIRMEAN)
        {
            showGalleryDrawer();
        }

        contextualDrawerFragment.setSelectedElement(element);
        if(!this.isCodis && !intervention.isArchived())
        {
            switch (element.getType())
            {
                case POINT_OF_INTEREST:
                    //disable contextual drawer for external SIG
                    if(!((PointOfInterest)element).isExternal())
                    {
                        showContextualDrawer();
                    }
                    break;
                default:
                    showContextualDrawer();
            }
        }
    }

    @Override
    public Element handleElementAdded(Tool tool, Double latitude, Double longitude) {
        Element element = this.toolbarFragment.tryGetElementFromTool(tool);

        if(element != null)
        {
            //It's an element that as been asked but never put on the map
            updateElement(element);
        }
        else {
            PictoFactory.ElementForm form = tool.getForm();
            switch (ElementType.getElementType(form)) {

                case AIRMEAN:
                    element = new Drone();
                    element.setName("Drone");
                    ((IMean) element).setState(MeanState.ASKED);
                    element.setForm(PictoFactory.ElementForm.AIRMEAN_PLANNED);
                    break;

                case MEAN:
                    element = new InterventionMean();
                    element.setName("Moyen SP");
                    ((IMean) element).setState(MeanState.ASKED);
                    ((IMean) element).setAction("Action par défaut");
                    element.setForm(PictoFactory.ElementForm.MEAN_PLANNED);
                    break;

                case MEAN_GROUP:
                    element = new InterventionMean();
                    element.setName("Moyen SP");
                    ((IMean) element).setState(MeanState.ASKED);
                    ((IMean) element).setAction("Action par défaut");
                    element.setForm(PictoFactory.ElementForm.MEAN_GROUP_PLANNED);
                    break;

                case MEAN_COLUMN:
                    element = new InterventionMean();
                    element.setName("Moyen SP");
                    ((IMean) element).setState(MeanState.ASKED);
                    ((IMean) element).setAction("Action par défaut");
                    element.setForm(PictoFactory.ElementForm.MEAN_COLUMN_PLANNED);
                    break;

                case MEAN_OTHER:
                case POINT_OF_INTEREST:
                    element = new PointOfInterest();
                    element.setForm(form);
                    element.setName("Moyen");
                    ((PointOfInterest) element).setExternal(false);
                    element.setForm(form);
                    break;

                case WATERPOINT:
                    element = new PointOfInterest();
                    element.setRole(Role.WATER);
                    element.setName("Point d'eau");
                    element.setForm(form);
                    break;

                default:
                    element = new InterventionMean();
                    element.setName("Moyen");
                    element.setForm(form);
            }

            element.setLocation(new Location(null, new GeoPoint(latitude, longitude, 0)));

            this.selectedTool = null;
            contextualDrawerFragment.setSelectedElement(element);
            showContextualDrawer();
        }

        return element;
    }

    @Override
    public void handleUpdatedElement(Element element) {
        updateElement(element);
    }

    @Override
    public void handleCancelSelection() {
        this.selectedTool = null;
        this.toolbarFragment.cancelSelection();
        if(isCodis || isInterventionArchived())
        {
            hideToolBar();
        }
        else
        {
            showToolBar();
        }

        hideContextualDrawer();
        hideGalleryDrawer();
    }

    private void showContextualDrawer(){
        getSupportFragmentManager().beginTransaction()
                .show(contextualDrawerFragment)
                .commit();
        contextualDrawer.animate().translationX(0);

    }

    private void showGalleryDrawer(){
        getSupportFragmentManager().beginTransaction()
                .show(galleryDrawerFragment)
                .commit();
    }

    private void hideContextualDrawer(){
        getSupportFragmentManager().beginTransaction()
                .hide(contextualDrawerFragment)
                .commit();
        contextualDrawer.animate().translationX(contextualDrawer.getWidth());
    }

    private void hideGalleryDrawer(){
        getSupportFragmentManager().beginTransaction()
                .hide(galleryDrawerFragment)
                .commit();
    }

    private void hideToolBar(){
        getSupportFragmentManager().beginTransaction()
                .hide(toolbarFragment)
                .commit();
    }

    private void showToolBar(){
        getSupportFragmentManager().beginTransaction()
                .show(toolbarFragment)
                .commit();
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
                        .hide(galleryDrawerFragment)
                        .setCustomAnimations(R.anim.frag_slide_in, R.anim.frag_slide_out)
                        .commit();

                switchTo(meansTableFragment);
                break;

            case R.id.switch_to_sitac:
                if(this.isCodis || isInterventionArchived())
                {
                    getSupportFragmentManager().beginTransaction()
                            .hide(toolbarFragment)
                            .hide(contextualDrawerFragment)
                            .hide(galleryDrawerFragment)
                            .setCustomAnimations(R.anim.frag_slide_in, R.anim.frag_slide_out)
                            .commit();
                    switchTo(sitacFragment);
                }
                else
                {
                    getSupportFragmentManager().beginTransaction()
                            .show(toolbarFragment)
                            .hide(contextualDrawerFragment)
                            .hide(galleryDrawerFragment)
                            .setCustomAnimations(R.anim.frag_slide_in, R.anim.frag_slide_out)
                            .commit();
                    switchTo(sitacFragment);
                }
                break;

            case R.id.logout_button:
                logout();
                break;
            case R.id.refresh_datas:
                dataLoader.loadData();
                break;
            default:
                break;
        }

        return true;
    }

    void switchTo (Fragment fragment)
    {
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
    public void updateElement(final Element element) {
        element.setIntervention(intervention.getId());
        switch (element.getType()) {
            case MEAN:
                this.updateInterventionMean((InterventionMean)element);
                break;
            case POINT_OF_INTEREST:
                this.updatePointOfInterest((PointOfInterest)element);
                break;
            case MEAN_OTHER:
                // TODO: 29/04/16
                break;
            case WATERPOINT:
                // TODO: 29/04/16
                break;
            case AIRMEAN:
                this.updateDrone((Drone)element);
                break;
        }
    }

    @Override
    public Mission getCurrentMission() {
        Mission mission = new Mission();
        ArrayList<GeoPoint> geoPoints = new ArrayList<>();
        for (LatLng latLng : sitacFragment.getCurrentDronePath()){
            geoPoints.add(new GeoPoint(latLng.latitude, latLng.longitude, 10));
        }
        mission.setPathPoints(geoPoints);
        return mission;
    }

    @Override
    public void setCreateDronePathMode(boolean isDronePathMode) {
        sitacFragment.setDronePathMode(isDronePathMode);
    }

    private void updateInterventionMean(final InterventionMean interventionMean) {

        interventionMeanDao.persist(interventionMean, new IDaoWriteReturnHandler<InterventionMean>() {
            @Override
            public void onSuccess(InterventionMean r) {
                final String meanId = r.getId();
                SitacActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dataLoader.loadMean(meanId, false);
                    }
                });
            }

            @Override
            public void onRepositoryFailure(Throwable e) {
                // TODO: Handle this better
                SitacActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SitacActivity.this, "Error repo", Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onRestFailure(Throwable e) {
                // TODO: Handle this better
                SitacActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SitacActivity.this, "Error rest", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void updateDrone(final Drone drone) {

        droneDao.persist(drone, new IDaoWriteReturnHandler<Drone>() {
            @Override
            public void onSuccess(Drone r) {
                final String droneId = r.getId();
                SitacActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dataLoader.loadDrone(droneId, false);
                    }
                });
            }

            @Override
            public void onRepositoryFailure(Throwable e) {
                // TODO: Handle this better
                SitacActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SitacActivity.this, "Error repo", Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onRestFailure(Throwable e) {
                // TODO: Handle this better
                SitacActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SitacActivity.this, "Error rest", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void updatePointOfInterest(final PointOfInterest pointOfInterest) {

        pointOfInterestDao.persist(pointOfInterest, new IDaoWriteReturnHandler<PointOfInterest>() {
            @Override
            public void onSuccess(PointOfInterest r) {
                final String pointOfInterestId = r.getId();
                SitacActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dataLoader.loadPointOfInterest(pointOfInterestId, false);
                    }
                });
            }

            @Override
            public void onRepositoryFailure(Throwable e) {
                // TODO: Handle this better
                SitacActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SitacActivity.this, "Error repo", Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onRestFailure(Throwable e) {
                // TODO: Handle this better
                SitacActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SitacActivity.this, "Error rest", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void cancelUpdate() {
        sitacFragment.cancelSelection();
    }

    @Override
    public void deleteElement(final Element element){
        new AlertDialog.Builder(this)
                .setTitle("Supprimer")
                .setMessage("Attention !\nCette action va libérer l'élément actuel et celui ci ne sera plus disponible.\nVoulez-vous continuer ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteIfMean(element);
                    }})
                .setNegativeButton(android.R.string.no, null).show();

    }

    private void deleteIfMean(final Element element){
        if (element.isMeanFromMeanTable()) {
            ((IMean) element).getStates().put(MeanState.RELEASED, new Date());
            IDao dao = this.dataLoader.getDaoOfElement(element);
            dao.persist(element, new IDaoWriteReturnHandler() {
                @Override
                public void onSuccess(Object r) {
                    deleteFromUI(element);
                }

                @Override
                public void onRepositoryFailure(Throwable e) {
                    SitacActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SitacActivity.this, "Error repo", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onRestFailure(Throwable e) {
                    SitacActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SitacActivity.this, "Error rest", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } else {
            if (element.getType() == ElementType.POINT_OF_INTEREST && ((PointOfInterest) element).isExternal()) {
                SitacActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SitacActivity.this, "Cet element ne peut être supprimé", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                IDao dao = this.dataLoader.getDaoOfElement(element);
                dao.delete(element, new IDaoWriteReturnHandler() {
                    @Override
                    public void onSuccess(Object r) {
                        deleteFromUI(element);
                    }

                    @Override
                    public void onRepositoryFailure(Throwable e) {
                        SitacActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SitacActivity.this, "Error repo", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onRestFailure(Throwable e) {
                        SitacActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SitacActivity.this, "Error rest", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }
    }

    private void deleteFromUI(final Element element){
        SitacActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sitacFragment.cancelSelection();
                hideContextualDrawer();
                sitacFragment.removeElement(element);
                Toast.makeText(SitacActivity.this, "Updated", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void handleValidation(Element element) {
        updateElement(element);
    }

    private void registerPushHandlers() {

        TdfApplication application = (TdfApplication) this.getApplication();

        // Means
        application.getPushHandler().addCatcher("mean/update/", new IPushCommand() {
            @Override
            public void execute(Bundle bundle) {
                SitacActivity.this.dataLoader.loadMean(bundle.getString("id"), true);
                Toast.makeText(SitacActivity.this, "Push update received for element", Toast.LENGTH_SHORT).show();
            }
        });

        // Drones
        application.getPushHandler().addCatcher("drone/update/", new IPushCommand() {
            @Override
            public void execute(Bundle bundle) {
                SitacActivity.this.dataLoader.loadDrone(bundle.getString("id"), true);
                Toast.makeText(SitacActivity.this, "Push update received for drone id", Toast.LENGTH_SHORT).show();
            }
        });

        // SIG
        application.getPushHandler().addCatcher("sig/update/", new IPushCommand() {
            @Override
            public void execute(Bundle bundle) {
                SitacActivity.this.dataLoader.loadPointOfInterest(bundle.getString("id"), true);
                Toast.makeText(SitacActivity.this, "Push update received for sig", Toast.LENGTH_SHORT).show();
            }
        });

        // SIG Extern
        application.getPushHandler().addCatcher("sigextern/update/", new IPushCommand() {
            @Override
            public void execute(Bundle bundle) {
                SitacActivity.this.dataLoader.loadPointOfInterest(bundle.getString("id"), true);
                Toast.makeText(SitacActivity.this, "Push update received for sigextern", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private class DataLoader {
        private String interventionId;

        public Collection<Drone> getDrones() {
            return drones;
        }

        public Collection<InterventionMean> getInterventionMeans() {
            return interventionMeans;
        }

        public Collection<PointOfInterest> getPointOfInterests() {
            return pointOfInterests;
        }

        public Collection<ImageDrone> getImageDrones() { return imageDrones; }

        // collection to save previous load datas
        private Collection<Drone> drones = new ArrayList<>();
        private Collection<InterventionMean> interventionMeans = new ArrayList<>();
        private Collection<PointOfInterest> pointOfInterests = new ArrayList<>();
        private Collection<ImageDrone> imageDrones = new ArrayList<>();

        public DataLoader(String interventionId) {
            this.interventionId = interventionId;
        }

        public void loadData() {
            this.loadIntervention();
            this.loadDrones();
            this.loadMeans();
            this.loadPointsOfInterest();

            //load images taken by drones
            this.loadImageDrones();
        }

        public Dao getDaoOfElement(IElement element) {
            Dao dao = null;
            switch (element.getType()) {
                case MEAN:
                    dao = SitacActivity.this.interventionMeanDao;
                    break;
                case POINT_OF_INTEREST:
                case MEAN_OTHER:
                case WATERPOINT:
                    dao = SitacActivity.this.pointOfInterestDao;
                    break;
                case AIRMEAN:
                    dao = SitacActivity.this.droneDao;
                    break;
            }

            return dao;
        }

        private void subscribeToIntervention() {
            TdfApplication tdfApplication = (TdfApplication) SitacActivity.this.getApplication();
            String pushRegistrationId = tdfApplication.getPushRegistrationId();

            SitacActivity.this.interventionDao.subscribe(SitacActivity.this.intervention, pushRegistrationId);
        }

        private void loadIntervention() {
            SitacActivity.this.interventionDao.find(this.interventionId, new IDaoSelectReturnHandler<Intervention>() {
                @Override
                public void onRepositoryResult(Intervention r) {
                    // Nothing
                }

                @Override
                public void onRestResult(final Intervention r) {
                    if (SitacActivity.this.isCodis || r.isArchived()) {
                        hideToolBar();
                    }

                    SitacActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Set intervention
                            SitacActivity.this.intervention = r;

                            // Subscribe to intervention
                            DataLoader.this.subscribeToIntervention();

                            // Center map view on location
                            meansTableFragment.initComponentForAddNewAskedMean();

                            if (sitacFragment.isLocationEmpty())
                                sitacFragment.setLocation(r.getLocation().getGeopoint());
                        }
                    });
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
                            // Cast to collection of elements
                            Collection<Element> colR = new ArrayList<Element>();
                            colR.addAll(r);

                            Collection<Element> colRRemove = new ArrayList<Element>();
                            colRRemove.addAll(drones);

                            drones = r;

                            removeElementsInUi(colRRemove);
                            updateElementsInUi(colR);

                            SitacActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    toolbarFragment.dispatchMeanByState(getInterventionMeans(), getDrones());
                                    sitacFragment.cancelSelection();
                                }
                            });
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

        private void loadDrone(String droneId, final boolean loadAfterPush)
        {
            SitacActivity.this.droneDao.find(droneId, new IDaoSelectReturnHandler<Drone>() {
                @Override
                public void onRepositoryResult(Drone r) {
                    // Nothing
                }

                @Override
                public void onRestResult(Drone r) {
                    Element elemToRemove = null;

                    //Collection usefull for remove in the loop
                    Collection<Drone> dronesCopy = new ArrayList<>();
                    dronesCopy.addAll(drones);

                    Iterator<Drone> it = drones.iterator();
                    Drone drone;
                    while (it.hasNext()) {
                        drone = it.next();
                        if (r.getId().equals(drone.getId())) {
                            elemToRemove = drone;
                            dronesCopy.remove(drone);
                        }
                    }

                    drones = dronesCopy;
                    drones.add(r);

                    if (elemToRemove != null) {
                        Collection<Element> elementsRemove = new ArrayList<Element>();
                        elementsRemove.add(elemToRemove);
                        removeElementsInUi(elementsRemove);
                    }

                    Collection<Element> elementsUpdate = new ArrayList<Element>();
                    elementsUpdate.add(r);
                    updateElementsInUi(elementsUpdate);

                    final Element element = r;
                    SitacActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toolbarFragment.dispatchMeanByState(getInterventionMeans(), getDrones());
                            if (loadAfterPush) {
                                //We keep the selection if modification come from an other tablet
                                Element currentElement = contextualDrawerFragment.tryGetElement();
                                sitacFragment.cancelSelectionAfterPushIfRequire(element, currentElement);
                            } else {
                                sitacFragment.cancelSelection();
                            }
                        }
                    });
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
                            // Cast to collection of elements
                            Collection<Element> colR = new ArrayList<Element>();
                            colR.addAll(r);

                            Collection<Element> colRRemove = new ArrayList<Element>();
                            colRRemove.addAll(interventionMeans);

                            interventionMeans = r;

                            removeElementsInUi(colRRemove);
                            updateElementsInUi(colR);

                            SitacActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    toolbarFragment.dispatchMeanByState(getInterventionMeans(), getDrones());
                                    sitacFragment.cancelSelection();
                                }
                            });
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

        private void loadMean(String meanId, final boolean loadAfterPush)
        {
            SitacActivity.this.interventionMeanDao.find(meanId, new IDaoSelectReturnHandler<InterventionMean>() {
                @Override
                public void onRepositoryResult(InterventionMean r) {
                    // Nothing
                }

                @Override
                public void onRestResult(InterventionMean r) {
                    Element elemToRemove = null;

                    //Collection usefull for remove in the loop
                    Collection<InterventionMean> interventionMeansCopy = new ArrayList<>();
                    interventionMeansCopy.addAll(interventionMeans);

                    Iterator<InterventionMean> it = interventionMeans.iterator();
                    InterventionMean interventionMean;
                    while (it.hasNext()) {
                        interventionMean = it.next();
                        if (r.getId().equals(interventionMean.getId())) {
                            elemToRemove = interventionMean;
                            interventionMeansCopy.remove(interventionMean);
                        }
                    }

                    interventionMeans = interventionMeansCopy;
                    interventionMeans.add(r);

                    if (elemToRemove != null) {
                        Collection<Element> elementsRemove = new ArrayList<Element>();
                        elementsRemove.add(elemToRemove);
                        removeElementsInUi(elementsRemove);
                    }

                    Collection<Element> elementsUpdate = new ArrayList<Element>();
                    elementsUpdate.add(r);
                    updateElementsInUi(elementsUpdate);

                    final Element element = r;
                    SitacActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toolbarFragment.dispatchMeanByState(getInterventionMeans(), getDrones());
                            if (loadAfterPush) {
                                //We keep the selection if modification come from an other tablet
                                Element currentElement = contextualDrawerFragment.tryGetElement();
                                sitacFragment.cancelSelectionAfterPushIfRequire(element, currentElement);
                            } else {
                                sitacFragment.cancelSelection();
                            }
                        }
                    });
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
                            // Cast to collection of elements
                            Collection<Element> colR = new ArrayList<Element>();
                            colR.addAll(r);

                            Collection<Element> colRRemove = new ArrayList<Element>();
                            colRRemove.addAll(pointOfInterests);

                            pointOfInterests = r;

                            removeElementsInUi(colRRemove);
                            updateElementsInUi(colR);

                            SitacActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    sitacFragment.cancelSelection();
                                }
                            });
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

        private void loadPointOfInterest(String pointOfInterestId, final boolean loadAfterPush) {
            SitacActivity.this.pointOfInterestDao.find(pointOfInterestId, new IDaoSelectReturnHandler<PointOfInterest>() {
                @Override
                public void onRepositoryResult(PointOfInterest r) {
                    // Nothing
                }

                @Override
                public void onRestResult(PointOfInterest r) {
                    Element elemToRemove = null;

                    //Collection usefull for remove in the loop
                    Collection<PointOfInterest> pointOfInterestsCopy = new ArrayList<>();
                    pointOfInterestsCopy.addAll(pointOfInterests);

                    Iterator<PointOfInterest> it = pointOfInterests.iterator();
                    PointOfInterest pointOfInterest;
                    while (it.hasNext()) {
                        pointOfInterest = it.next();
                        if (r.getId().equals(pointOfInterest.getId())) {
                            elemToRemove = pointOfInterest;
                            pointOfInterestsCopy.remove(pointOfInterest);
                        }
                    }

                    pointOfInterests = pointOfInterestsCopy;
                    pointOfInterests.add(r);

                    if (elemToRemove != null) {
                        Collection<Element> elementsRemove = new ArrayList<>();
                        elementsRemove.add(elemToRemove);
                        removeElementsInUi(elementsRemove);
                    }

                    Collection<Element> elementsUpdate = new ArrayList<>();
                    elementsUpdate.add(r);
                    updateElementsInUi(elementsUpdate);

                    final Element element = r;
                    SitacActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (loadAfterPush) {
                                //We keep the selection if modification come from an other tablet
                                Element currentElement = contextualDrawerFragment.tryGetElement();
                                sitacFragment.cancelSelectionAfterPushIfRequire(element, currentElement);
                            } else {
                                sitacFragment.cancelSelection();
                            }
                        }
                    });
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

        /**
         * Load all images of the intervention
         */
        private void loadImageDrones()
        {
            SitacActivity.this.imageDroneDao.findByIntervention(interventionId,
                    new DaoSelectionParameters(), new IDaoSelectReturnHandler<List<ImageDrone>>() {
                        @Override
                        public void onRepositoryResult(List<ImageDrone> r) {
                            // Nothing
                        }

                        @Override
                        public void onRestResult(List<ImageDrone> r) {
                            // Cast to collection of elements
                            Collection<ImageDrone> colR = new ArrayList<>();
                            colR.addAll(r);

                            Collection<ImageDrone> colRRemove = new ArrayList<>();
                            colRRemove.addAll(imageDrones);

                            imageDrones = r;

                            removeImagesDrone(colRRemove);
                            updateImagesDrone(colR);
                        }

                        @Override
                        public void onRepositoryFailure(Throwable e) {

                        }

                        @Override
                        public void onRestFailure(Throwable e) {

                        }
                    });
        }

        /**
         * Load an image of the intervention
         * @param pointOfInterestId
         * @param loadAfterPush
         */
        private void loadImageDrone(String pointOfInterestId, final boolean loadAfterPush)
        {
            SitacActivity.this.imageDroneDao.find(interventionId, new IDaoSelectReturnHandler<ImageDrone>() {
                        @Override
                        public void onRepositoryResult(ImageDrone r) {
                            // Nothing
                        }

                        @Override
                        public void onRestResult(ImageDrone r) {
                            ImageDrone imgToRemove = null;

                            //Collection usefull for remove in the loop
                            Collection<ImageDrone> imageDronesCopy = new ArrayList<>();
                            imageDronesCopy.addAll(imageDrones);

                            Iterator<ImageDrone> it = imageDrones.iterator();
                            ImageDrone imageDrone;
                            while (it.hasNext()) {
                                imageDrone = it.next();
                                if (r.getId().equals(imageDrone.getId())) {
                                    imgToRemove = imageDrone;
                                    imageDronesCopy.remove(imageDrone);
                                }
                            }

                            imageDrones = imageDronesCopy;
                            imageDrones.add(r);

                            if (imgToRemove != null) {
                                Collection<ImageDrone> imgsRemove = new ArrayList<>();
                                imgsRemove.add(imgToRemove);
                                removeImagesDrone(imgsRemove);
                            }

                            Collection<ImageDrone> imgsUpdate = new ArrayList<>();
                            imgsUpdate.add(r);
                            updateImagesDrone(imgsUpdate);
                        }

                        @Override
                        public void onRepositoryFailure(Throwable e) {

                        }

                        @Override
                        public void onRestFailure(Throwable e) {

                        }
                    });
        }

        /**
         * Update the sitacfragment
         * @param imageDrones
         */
        public void updateImagesDrone(final Collection<ImageDrone> imageDrones)
        {
            SitacActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Update Map
                    SitacActivity.this.sitacFragment.updateImageDrones(imageDrones);
                }
            });
        }


        /**
         * Update the sitacfragment
         * @param imageDrones
         */
        public void removeImagesDrone(final Collection<ImageDrone> imageDrones)
        {
            SitacActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Update Map
                    SitacActivity.this.sitacFragment.removeImageDrones(imageDrones);
                }
            });
        }

        public void updateElementsInUi(final Collection<Element> elements) {
            SitacActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Update Map
                    SitacActivity.this.sitacFragment.updateElements(elements);

                    // Update Means table
                    SitacActivity.this.meansTableFragment.updateElements(elements);
                }
            });
        }

        public void removeElementsInUi(final Collection<Element> elements) {
            SitacActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Update Map
                    SitacActivity.this.sitacFragment.removeElements(elements);

                    // Update Means table
                    SitacActivity.this.meansTableFragment.removeElementFromUi(elements);
                }
            });
        }
    }
}
