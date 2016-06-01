package edu.istic.tdf.dfclient.dataloader;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import edu.istic.tdf.dfclient.TdfApplication;
import edu.istic.tdf.dfclient.activity.SitacActivity;
import edu.istic.tdf.dfclient.dao.Dao;
import edu.istic.tdf.dfclient.dao.DaoSelectionParameters;
import edu.istic.tdf.dfclient.dao.handler.IDaoSelectReturnHandler;
import edu.istic.tdf.dfclient.domain.element.Element;
import edu.istic.tdf.dfclient.domain.element.IElement;
import edu.istic.tdf.dfclient.domain.element.mean.drone.Drone;
import edu.istic.tdf.dfclient.domain.element.mean.interventionMean.InterventionMean;
import edu.istic.tdf.dfclient.domain.element.pointOfInterest.PointOfInterest;
import edu.istic.tdf.dfclient.domain.image.ImageDrone;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Loader for sitac activity
 */
public class DataLoader {
    /**
     * the activity that call the dataloader
     */
    SitacActivity sitacActivity;

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

    public DataLoader(String interventionId,
                      SitacActivity sitacActivity) {
        this.interventionId = interventionId;
        this.sitacActivity = sitacActivity;
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
                dao = sitacActivity.getInterventionMeanDao();
                break;
            case POINT_OF_INTEREST:
            case MEAN_OTHER:
            case WATERPOINT:
                dao = sitacActivity.getPointOfInterestDao();
                break;
            case AIRMEAN:
                dao = sitacActivity.getDroneDao();
                break;
        }

        return dao;
    }

    public void subscribeToIntervention() {
        TdfApplication tdfApplication = (TdfApplication) this.sitacActivity.getApplication();
        String pushRegistrationId = tdfApplication.getPushRegistrationId();

        sitacActivity.getInterventionDao().subscribe(this.sitacActivity.getIntervention(), pushRegistrationId);
    }

    public void loadIntervention() {
        sitacActivity.getInterventionDao().find(this.interventionId, new IDaoSelectReturnHandler<Intervention>() {
            @Override
            public void onRepositoryResult(Intervention r) {
                // Nothing
            }

            @Override
            public void onRestResult(final Intervention r) {
                if (sitacActivity.isCodis() || r.isArchived()) {
                    sitacActivity.hideToolBar();
                }

                sitacActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Set intervention
                        sitacActivity.setIntervention(r);

                        // Subscribe to intervention
                        DataLoader.this.subscribeToIntervention();

                        // Center map view on location
                        sitacActivity.getMeansTableFragment().initComponentForAddNewAskedMean();

                        if (sitacActivity.getSitacFragment().isLocationEmpty())
                            sitacActivity.getSitacFragment().setLocation(r.getLocation().getGeopoint());
                    }
                });
            }

            @Override
            public void onRepositoryFailure(Throwable e) {
                // Nothing
            }

            @Override
            public void onRestFailure(Throwable e) {
                sitacActivity.displayNetworkError();
            }
        });
    }

    public void loadDrones() {
        sitacActivity.getDroneDao().findByIntervention(this.interventionId, new DaoSelectionParameters(),
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

                        sitacActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sitacActivity.getToolbarFragment().dispatchMeanByState(getInterventionMeans(), getDrones());
                                sitacActivity.getSitacFragment().cancelSelection();
                            }
                        });
                    }

                    @Override
                    public void onRepositoryFailure(Throwable e) {
                        // Nothing
                    }

                    @Override
                    public void onRestFailure(Throwable e) {
                        sitacActivity.displayNetworkError();
                    }
                });
    }

    public void startMission(final Drone drone) {
        sitacActivity.getDroneDao().startMission(drone, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("DRONE MISSION :", "ERROR WHEN STARTING MISSION FOR DRONE[" + drone.getId() + "]");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("DRONE MISSION : ", "MISSION STARTED FOR DRONE [" + drone.getId() + "]");

            }
        });
    }


    public void loadDrone(String droneId, final boolean loadAfterPush)
    {
        sitacActivity.getDroneDao().find(droneId, new IDaoSelectReturnHandler<Drone>() {
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
                sitacActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        sitacActivity.getToolbarFragment().dispatchMeanByState(getInterventionMeans(), getDrones());
                        if (loadAfterPush) {
                            //We keep the selection if modification come from an other tablet
                            Element currentElement = sitacActivity.getContextualDrawerFragment().tryGetElement();
                            sitacActivity.getSitacFragment().cancelSelectionAfterPushIfRequire(element, currentElement);
                        } else {
                            sitacActivity.getSitacFragment().cancelSelection();
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
                sitacActivity.displayNetworkError();
            }
        });
    }

    public void loadMeans() {
        sitacActivity.getInterventionMeanDao().findByIntervention(this.interventionId, new DaoSelectionParameters(),
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

                        sitacActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sitacActivity.getToolbarFragment().dispatchMeanByState(getInterventionMeans(), getDrones());
                                sitacActivity.getSitacFragment().cancelSelection();
                            }
                        });
                    }

                    @Override
                    public void onRepositoryFailure(Throwable e) {
                        // Nothing
                    }

                    @Override
                    public void onRestFailure(Throwable e) {
                        sitacActivity.displayNetworkError();
                    }
                });
    }

    public void loadMean(String meanId, final boolean loadAfterPush)
    {
        sitacActivity.getInterventionMeanDao().find(meanId, new IDaoSelectReturnHandler<InterventionMean>() {
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
                sitacActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sitacActivity.getToolbarFragment().dispatchMeanByState(getInterventionMeans(), getDrones());
                        if (loadAfterPush) {
                            //We keep the selection if modification come from an other tablet
                            Element currentElement = sitacActivity.getContextualDrawerFragment().tryGetElement();
                            sitacActivity.getSitacFragment().cancelSelectionAfterPushIfRequire(element, currentElement);
                        } else {
                            sitacActivity.getSitacFragment().cancelSelection();
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
                sitacActivity.displayNetworkError();
            }
        });
    }

    public void loadPointsOfInterest() {
        sitacActivity.getPointOfInterestDao().findByIntervention(this.interventionId, new DaoSelectionParameters(),
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

                        sitacActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sitacActivity.getSitacFragment().cancelSelection();
                            }
                        });
                    }

                    @Override
                    public void onRepositoryFailure(Throwable e) {
                        // Nothing
                    }

                    @Override
                    public void onRestFailure(Throwable e) {
                        sitacActivity.displayNetworkError();
                    }
                });
    }

    public void loadPointOfInterest(String pointOfInterestId, final boolean loadAfterPush) {
        sitacActivity.getPointOfInterestDao().find(pointOfInterestId, new IDaoSelectReturnHandler<PointOfInterest>() {
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
                sitacActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (loadAfterPush) {
                            //We keep the selection if modification come from an other tablet
                            Element currentElement = sitacActivity.getContextualDrawerFragment().tryGetElement();
                            sitacActivity.getSitacFragment().cancelSelectionAfterPushIfRequire(element, currentElement);
                        } else {
                            sitacActivity.getSitacFragment().cancelSelection();
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
                sitacActivity.displayNetworkError();
            }
        });
    }

    /**
     * Load all images of the intervention
     */
    public void loadImageDrones()
    {
        sitacActivity.getImageDroneDao().findByIntervention(interventionId,
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
    public void loadImageDrone(String pointOfInterestId, final boolean loadAfterPush)
    {
        sitacActivity.getImageDroneDao().find(interventionId, new IDaoSelectReturnHandler<ImageDrone>() {
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
        sitacActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Update Map
                sitacActivity.getSitacFragment().updateImageDrones(imageDrones);
            }
        });
    }


    /**
     * Update the sitacfragment
     * @param imageDrones
     */
    public void removeImagesDrone(final Collection<ImageDrone> imageDrones)
    {
        sitacActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Update Map
                sitacActivity.getSitacFragment().removeImageDrones(imageDrones);
            }
        });
    }

    public void updateElementsInUi(final Collection<Element> elements) {
        sitacActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Update Map
                sitacActivity.getSitacFragment().updateElements(elements);

                // Update Means table
                sitacActivity.getMeansTableFragment().updateElements(elements);
            }
        });
    }

    public void removeElementsInUi(final Collection<Element> elements) {
        sitacActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Update Map
                sitacActivity.getSitacFragment().removeElements(elements);

                // Update Means table
                sitacActivity.getMeansTableFragment().removeElementFromUi(elements);
            }
        });
    }
}
