package edu.istic.tdf.dfclient.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Observer;

import javax.inject.Inject;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.TdfApplication;
import edu.istic.tdf.dfclient.UI.Tool;
import edu.istic.tdf.dfclient.dao.DaoSelectionParameters;
import edu.istic.tdf.dfclient.dao.IDao;
import edu.istic.tdf.dfclient.dao.domain.ImageDroneDao;
import edu.istic.tdf.dfclient.dao.domain.InterventionDao;
import edu.istic.tdf.dfclient.dao.domain.PushMessageDao;
import edu.istic.tdf.dfclient.dao.domain.element.DroneDao;
import edu.istic.tdf.dfclient.dao.domain.element.InterventionMeanDao;
import edu.istic.tdf.dfclient.dao.domain.element.PointOfInterestDao;
import edu.istic.tdf.dfclient.dao.handler.IDaoSelectReturnHandler;
import edu.istic.tdf.dfclient.dao.handler.IDaoWriteReturnHandler;
import edu.istic.tdf.dfclient.dataloader.DataLoader;
import edu.istic.tdf.dfclient.domain.element.Element;
import edu.istic.tdf.dfclient.domain.element.ElementType;
import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.domain.element.mean.IMean;
import edu.istic.tdf.dfclient.domain.element.mean.MeanState;
import edu.istic.tdf.dfclient.domain.element.mean.drone.Drone;
import edu.istic.tdf.dfclient.domain.element.mean.drone.mission.Mission;
import edu.istic.tdf.dfclient.domain.element.mean.interventionMean.InterventionMean;
import edu.istic.tdf.dfclient.domain.element.pointOfInterest.PointOfInterest;
import edu.istic.tdf.dfclient.domain.geo.GeoPoint;
import edu.istic.tdf.dfclient.domain.geo.Location;
import edu.istic.tdf.dfclient.domain.image.ImageDrone;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;
import edu.istic.tdf.dfclient.drawable.PictoFactory;
import edu.istic.tdf.dfclient.fragment.ContextualDrawerFragment;
import edu.istic.tdf.dfclient.fragment.GalleryDrawerFragment;
import edu.istic.tdf.dfclient.fragment.MeansTableFragment;
import edu.istic.tdf.dfclient.fragment.SitacFragment;
import edu.istic.tdf.dfclient.fragment.ToolbarFragment;
import edu.istic.tdf.dfclient.push.IPushCommand;
import edu.istic.tdf.dfclient.domain.PushMessage;
import edu.istic.tdf.dfclient.push.PushSubscriptionData;
import edu.istic.tdf.dfclient.rest.serializer.RestSerializerBuilder;
import lombok.Getter;
import lombok.Setter;

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

    @Getter
    private SitacFragment sitacFragment;
    @Getter
    private ToolbarFragment toolbarFragment;
    @Getter
    private ContextualDrawerFragment contextualDrawerFragment;
    @Getter
    private MeansTableFragment meansTableFragment;
    @Getter
    private GalleryDrawerFragment galleryDrawerFragment;

    private android.support.v4.app.Fragment currentFragment;

    // Data
    private DataLoader dataLoader;

    @Getter@Setter
    private Intervention intervention;

    private boolean interventionIsArchived;

    private Element selectedElement;

    @Inject @Getter
    InterventionDao interventionDao;

    @Inject @Getter
    DroneDao droneDao;

    @Inject @Getter
    InterventionMeanDao interventionMeanDao;

    @Inject @Getter
    PointOfInterestDao pointOfInterestDao;

    @Inject @Getter
    ImageDroneDao imageDroneDao;

    @Inject @Getter
    PushMessageDao pushMessageDao;

    /**
     * true if and only if the current user is the CODIS
     */
    @Getter
    private boolean isCodis;

    private ArrayList<Observer> observers = new ArrayList<>();
    private boolean sitacQuitted = true;
    private Date lastUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.isCodis = ((TdfApplication)this.getApplication()).loadCredentials().isCodisUser();

        // Used for the Asynchrone database synchronisation with push
        sitacQuitted=false;
        lastUpdate = new Date();

        //intervention = createInterventionBouton();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitac);

        // Inject dagger dependencies
        getApplicationComponent().inject(this);

        //remove back button from action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

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

        // Get extras from previous activity
        String interventionId = (String) getIntent().getExtras().get("interventionId");
        interventionIsArchived = (boolean) getIntent().getExtras().get("interventionIsArchived");

        //initialize the data loader
        dataLoader = new DataLoader(interventionId,
                this);

        //Load the datas in the dataloader
        dataLoader.loadData();

        Bundle bundlePush = new Bundle();
        PushSubscriptionData pushSubscriptionData = new PushSubscriptionData();

        new AsyncPullRefresh().execute();

        //Register to the push handler
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
            this.galleryDrawerFragment.updateList(imageDrones, this.dataLoader.getDrones());
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

        hideContextualDrawer();
        hideGalleryDrawer();
    }

    public void showContextualDrawer(){
        getSupportFragmentManager().beginTransaction()
                .show(contextualDrawerFragment)
                .commit();
        contextualDrawer.animate().translationX(0);

    }

    public void showGalleryDrawer() {
        getSupportFragmentManager().beginTransaction()
                .show(galleryDrawerFragment)
                .commit();
    }

    public void hideContextualDrawer(){
        getSupportFragmentManager().beginTransaction()
                .hide(contextualDrawerFragment)
                .commit();
        contextualDrawer.animate().translationX(contextualDrawer.getWidth());
    }

    public void hideGalleryDrawer() {
        getSupportFragmentManager().beginTransaction()
                .hide(galleryDrawerFragment)
                .commit();
    }

    public void hideToolBar(){
        getSupportFragmentManager().beginTransaction()
                .hide(toolbarFragment)
                .commit();
    }

    public void showToolBar() {
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

    public void displayNetworkError() {
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

    @Override
    public void startMission(Drone drone) {
        this.dataLoader.startMission(drone);
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

                //update the meanTable
                meansTableFragment.updateElement(element);
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

    @Override
    protected void onPause() {
        sitacQuitted=true;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        sitacQuitted=true;
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        sitacQuitted=true;
        super.onStop();
    }

    @Override
    protected void onRestart() {
        sitacQuitted=false;
        super.onRestart();
    }

    private class AsyncPullRefresh extends AsyncTask<Void, Void, Void> {
        private final long pullRefreshTime = 5000;
        @Override
        protected Void doInBackground(Void ... params) {
            try {
                Looper.prepare();
                while(!sitacQuitted){
                    synchronized (this){
                        wait(pullRefreshTime);
                    }
                    // TODO : handle dao
                    // Request on intervention and on the date of the last update

                    DaoSelectionParameters parameters = new DaoSelectionParameters();

                    DateFormat formatter =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                    java.sql.Timestamp lastUpdateTimeStamp = new Timestamp(lastUpdate.getTime()-10000000);
                    parameters.addFilter("timestamp", formatter.format(lastUpdateTimeStamp)); //();
                    pushMessageDao.findMessageByInterventionAndDate(
                            intervention.getId(),
                            lastUpdate,
                            parameters,
                            new IDaoSelectReturnHandler<List<PushMessage>>() {
                                @Override
                                public void onRepositoryResult(List<PushMessage> r) {
                                    Log.d("pushMessageDao", "onRepositoryResult");
                                }

                                @Override
                                public void onRestResult(List<PushMessage> r) {
                                    Log.d("pushMessageDao", "onRestResult");

                                    for (PushMessage p : r) {
                                        if(p==null){

                                        }else if(p.getId()==null){

                                        }else if(p.getTopic()==null){

                                        }
                                        Bundle monBundle = new Bundle();
                                        Log.d("push message", "----------------------------------------------");
                                        Log.d("push message", "ID : "+p.getId());
                                        Log.d("push message", "TOPIC : " + p.getTopic());

                                        monBundle.putString("topic", p.getTopic());
                                        monBundle.putString("id", p.getId());
                                        ((TdfApplication) getApplication()).getPushHandler().handlePush("api", monBundle);
                                        Log.d("push message", "-----------------------------------------------");
                                    }
                                }

                                @Override
                                public void onRepositoryFailure(Throwable e) {
                                    Log.e("pushMessageDao", "onRepositoryFailure  " + e.getMessage());
                                }

                                @Override
                                public void onRestFailure(Throwable e) {
                                    Log.e("pushMessageDao", "onRestFailure  " + e.getMessage());
                                }
                            });

                    //pushMessages.add(m);
                    // TODO : SUPPRESS BOUCHOUN BELOW

                }
            }catch (InterruptedException e){
                Log.e("Async Push Refresh", e.getMessage());
            }
            return null;
        }
    }
}
