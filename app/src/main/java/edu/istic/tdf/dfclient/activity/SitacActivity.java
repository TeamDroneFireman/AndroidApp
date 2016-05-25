package edu.istic.tdf.dfclient.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Observer;

import javax.inject.Inject;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.UI.Tool;
import edu.istic.tdf.dfclient.dao.Dao;
import edu.istic.tdf.dfclient.dao.DaoSelectionParameters;
import edu.istic.tdf.dfclient.dao.IDao;
import edu.istic.tdf.dfclient.dao.domain.InterventionDao;
import edu.istic.tdf.dfclient.dao.domain.element.DroneDao;
import edu.istic.tdf.dfclient.dao.domain.element.InterventionMeanDao;
import edu.istic.tdf.dfclient.dao.domain.element.PointOfInterestDao;
import edu.istic.tdf.dfclient.dao.handler.IDaoSelectReturnHandler;
import edu.istic.tdf.dfclient.dao.handler.IDaoWriteReturnHandler;
import edu.istic.tdf.dfclient.domain.element.Element;
import edu.istic.tdf.dfclient.domain.element.ElementType;
import edu.istic.tdf.dfclient.domain.element.IElement;
import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.domain.element.mean.IMean;
import edu.istic.tdf.dfclient.domain.element.mean.MeanState;
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
    private DataLoader dataLoader;

    private Intervention intervention;

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
        dataLoader = new DataLoader(interventionId); //"5720c3b8358423010064ca33"); // TODO : Set the real intervention id
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
        hideContextualDrawer();
    }

    @Override
    public Tool getSelectedTool() {
        return selectedTool;
    }

    @Override
    public void setSelectedElement(Element element) {
        contextualDrawerFragment.setSelectedElement(element);
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

    @Override
    public Element handleElementAdded(PictoFactory.ElementForm form, Double latitude, Double longitude) {
        Element element;

        switch(ElementType.getElementType(form)){

            case AIRMEAN:
                element = new Drone();
                element.setName("Drone");
                ((IMean)element).setState(MeanState.ASKED);
                break;

            case MEAN:
                element = new InterventionMean();
                element.setName("Moyen SP");
                ((IMean)element).setState(MeanState.ASKED);
                // TODO: 23/05/16 action bouchon
                ((IMean)element).setAction("Action par défaut");
                break;

            case MEAN_OTHER:
            case POINT_OF_INTEREST:
                element = new PointOfInterest();
                element.setForm(form);
                element.setName("Moyen");
                ((PointOfInterest)element).setExternal(false);
                break;

            case WATERPOINT:
                element = new PointOfInterest();
                element.setRole(Role.WATER);
                element.setName("Point d'eau");
                break;

            default:
                element = new InterventionMean();
                element.setName("Moyen");
        }

        element.setForm(form);
        element.setLocation(new Location(null, new GeoPoint(latitude, longitude, 0)));

        this.selectedTool = null;
        contextualDrawerFragment.setSelectedElement(element);
        showContextualDrawer();
        return element;
    }

    @Override
    public void handleUpdatedElement(Element element) {
        updateElement(element);
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
    public void updateElement(final Element element) {
        sitacFragment.updateElement(element);
        meansTableFragment.updateElement(element);
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

    private void updateInterventionMean(final InterventionMean interventionMean) {

        interventionMeanDao.persist(interventionMean, new IDaoWriteReturnHandler<InterventionMean>() {
            @Override
            public void onSuccess(InterventionMean r) {
                SitacActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dataLoader.loadMeans();
                        hideContextualDrawer();
                        sitacFragment.cancelSelection();
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
                SitacActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dataLoader.loadDrones();
                        hideContextualDrawer();
                        sitacFragment.cancelSelection();
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
                SitacActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dataLoader.loadPointsOfInterest();
                        hideContextualDrawer();
                        sitacFragment.cancelSelection();
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
        hideContextualDrawer();
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
        if (isMeanFromMeanTable(element)) {
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
                        Toast.makeText(SitacActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onRepositoryFailure(Throwable e) {
                        Toast.makeText(SitacActivity.this, "Error repo", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onRestFailure(Throwable e) {
                        Toast.makeText(SitacActivity.this, "Error rest", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private boolean isMeanFromMeanTable(Element element){
        ElementType elementType = element.getType();
        boolean isMean = elementType == ElementType.MEAN;
        isMean |= elementType == ElementType.AIRMEAN;
        return isMean;
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

    private void addElement(Element element){
        switch (element.getType()) {
            case MEAN:
                this.dataLoader.getInterventionMeans().add((InterventionMean)element);
                break;
            case POINT_OF_INTEREST:
                this.dataLoader.getPointOfInterests().add((PointOfInterest)element);
                break;
            case MEAN_OTHER:
                // TODO: 29/04/16
                break;
            case WATERPOINT:
                // TODO: 29/04/16
                break;
            case AIRMEAN:
                this.dataLoader.getDrones().add((Drone)element);
                break;
        }
    }

    @Override
    public void handleValidation(Element element) {
        updateElement(element);
    }

    private class DataLoader {
        private String interventionId;

        public Collection<Drone> getDrones() {
            return drones;
        }

        public void setDrones(Collection<Drone> drones) {
            this.drones = drones;
        }

        public Collection<InterventionMean> getInterventionMeans() {
            return interventionMeans;
        }

        public void setInterventionMeans(Collection<InterventionMean> interventionMeans) {
            this.interventionMeans = interventionMeans;
        }

        public Collection<PointOfInterest> getPointOfInterests() {
            return pointOfInterests;
        }

        public void setPointOfInterests(Collection<PointOfInterest> pointOfInterests) {
            this.pointOfInterests = pointOfInterests;
        }

        // collection to save previous load datas
        private Collection<Drone> drones = new ArrayList<>();
        private Collection<InterventionMean> interventionMeans = new ArrayList<>();
        private Collection<PointOfInterest> pointOfInterests = new ArrayList<>();

        public DataLoader(String interventionId) {
            this.interventionId = interventionId;
        }

        public void loadData() {
            this.loadIntervention();
            this.loadDrones();
            this.loadMeans();
            this.loadPointsOfInterest();
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

        public void persistElement(Element element, IDaoWriteReturnHandler handler) {
            Dao dao = getDaoOfElement(element);
            if(dao != null){
                dao.persist(element, handler);
            }

        }

        private void loadIntervention() {
            SitacActivity.this.interventionDao.find(this.interventionId, new IDaoSelectReturnHandler<Intervention>() {
                @Override
                public void onRepositoryResult(Intervention r) {
                    // Nothing
                }

                @Override
                public void onRestResult(final Intervention r) {

                    SitacActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SitacActivity.this.intervention = r;
                            sitacFragment.setLocation(r.getLocation().getGeopoint());
                        }
                    });

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
                            // Cast to collection of elements
                            Collection<Element> colR = new ArrayList<Element>();
                            colR.addAll(r);

                            Collection<Element> colRRemove = new ArrayList<Element>();
                            colRRemove.addAll(drones);
                            for (int i = 0; i < r.size(); i++) {
                                Drone drone;
                                Iterator<Drone> it = r.iterator();
                                while (it.hasNext()) {
                                    drone = it.next();
                                    Drone droneR = r.get(i);
                                    if (droneR.getId().equals(drone.getId())) {
                                        colRRemove.remove(droneR);
                                    }
                                }
                            }

                            drones = r;

                            removeElementsInUi(colRRemove);
                            updateElementsInUi(colR);
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
                            for (int i = 0; i < r.size(); i++) {
                                InterventionMean interventionMean;
                                Iterator<InterventionMean> it = r.iterator();
                                while(it.hasNext()){
                                    interventionMean = it.next();
                                    InterventionMean interventionMeanR = r.get(i);
                                    if(interventionMeanR.getId().equals(interventionMean.getId())){
                                        colRRemove.remove(interventionMeanR);
                                    }
                                }
                            }

                            interventionMeans = r;

                            removeElementsInUi(colRRemove);
                            updateElementsInUi(colR);
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
                            for (int i = 0; i < r.size(); i++) {
                                PointOfInterest pointOfInterest;
                                Iterator<PointOfInterest> it = r.iterator();
                                while(it.hasNext()){
                                    pointOfInterest = it.next();
                                    PointOfInterest pointOfInterestR = r.get(i);
                                    if(pointOfInterestR.getId().equals(pointOfInterest.getId())){
                                        colRRemove.remove(pointOfInterestR);
                                    }
                                }
                            }

                            pointOfInterests = r;

                            removeElementsInUi(colRRemove);

                            updateElementsInUi(colR);
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

        private void updateElementsInUi(final Collection<Element> elements) {

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

        private void removeElementsInUi(final Collection<Element> elements) {

            SitacActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Update Map
                    SitacActivity.this.sitacFragment.removeElements(elements);

                    // Update Means table
                    SitacActivity.this.meansTableFragment.removeElements(elements);
                }
            });

        }
    }

}
