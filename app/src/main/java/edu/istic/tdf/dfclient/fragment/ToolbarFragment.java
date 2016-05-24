package edu.istic.tdf.dfclient.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.UI.Tool;
import edu.istic.tdf.dfclient.UI.ToolsGroup;
import edu.istic.tdf.dfclient.UI.adapter.ToolsListAdapter;
import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.domain.element.mean.IMean;
import edu.istic.tdf.dfclient.domain.element.mean.drone.Drone;
import edu.istic.tdf.dfclient.domain.element.mean.interventionMean.InterventionMean;
import edu.istic.tdf.dfclient.drawable.PictoFactory;

public class ToolbarFragment extends Fragment implements ToolsListAdapter.OnToolsListAdapterInteractionListener, Observer {

    // UI
    @Bind(R.id.toolbar_listview) ExpandableListView listView;

    private SparseArray<ToolsGroup> groups = new SparseArray<ToolsGroup>();
    private OnFragmentInteractionListener mListener;

    //collections of object in each group
    private Map<Tool,IMean> mapGroupAsked = new HashMap<>();
    private Map<Tool,IMean> mapGroupInTransit = new HashMap<>();
    private Map<Tool,IMean> mapGroupInactif = new HashMap<>();
    private Map<Tool,IMean> mapGroupActif = new HashMap<>();

    public ToolbarFragment() {}

    public static ToolbarFragment newInstance() {
        ToolbarFragment fragment = new ToolbarFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_toolbar, container, false);
        ButterKnife.bind(this, view);

        createData();
        ToolsListAdapter adapter = new ToolsListAdapter(getContext(), groups, this);
        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void handleSelectedToolUtils(Tool tool){
        mListener.handleSelectedToolUtils(tool);
    }

    public void handleSelectedToolAsked(Tool tool){
        // TODO: 24/05/16
        mListener.handleSelectedToolAsked(tool);
    }

    public void handleSelectedToolInTransit(Tool tool){
        // TODO: 24/05/16
        mListener.handleSelectedToolInTransit(tool);
    }

    public void handleSelectedToolInactif(Tool tool){
        // TODO: 24/05/16
        mListener.handleSelectedToolInactif(tool);
    }

    public void handleSelectedToolActif(Tool tool){
        // TODO: 24/05/16
        mListener.handleSelectedToolActif(tool);
    }

    public void createData() {

        ToolsGroup group;

        group = new ToolsGroup("Outils", false);
        group.addTool(new Tool(PictoFactory.ElementForm.MEAN, Role.DEFAULT));
        group.addTool(new Tool(PictoFactory.ElementForm.MEAN_PLANNED, Role.DEFAULT));
        group.addTool(new Tool(PictoFactory.ElementForm.MEAN_GROUP, Role.DEFAULT));
        group.addTool(new Tool(PictoFactory.ElementForm.MEAN_COLUMN, Role.DEFAULT));

        group.addTool(new Tool(PictoFactory.ElementForm.MEAN_OTHER, Role.DEFAULT));
        group.addTool(new Tool(PictoFactory.ElementForm.MEAN_OTHER_PLANNED, Role.DEFAULT));

        group.addTool(new Tool(PictoFactory.ElementForm.AIRMEAN, Role.DEFAULT));
        group.addTool(new Tool(PictoFactory.ElementForm.AIRMEAN_PLANNED, Role.DEFAULT));

        group.addTool(new Tool(PictoFactory.ElementForm.WATERPOINT, Role.DEFAULT));
        group.addTool(new Tool(PictoFactory.ElementForm.WATERPOINT_SUSTAINABLE, Role.DEFAULT));
        group.addTool(new Tool(PictoFactory.ElementForm.WATERPOINT_SUPPLY, Role.DEFAULT));

        group.addTool(new Tool(PictoFactory.ElementForm.SOURCE, Role.DEFAULT));
        group.addTool(new Tool(PictoFactory.ElementForm.TARGET, Role.DEFAULT));

        groups.append(0, group);

        group = new ToolsGroup("Demandés");
        groups.append(1, group);

        group = new ToolsGroup("En transit");
        groups.append(2, group);

        group = new ToolsGroup("Inactifs");
        groups.append(3, group);

        group = new ToolsGroup("Actifs");
        groups.append(4, group);

    }

    @Override
    public void update(Observable observable, Object data) {
    }

    public interface OnFragmentInteractionListener {
        void handleSelectedToolUtils(Tool tool);
        void handleSelectedToolAsked(Tool tool);
        void handleSelectedToolInTransit(Tool tool);
        void handleSelectedToolInactif(Tool tool);
        void handleSelectedToolActif(Tool tool);
    }

    public void dispatchMeanByState(Collection<InterventionMean> interventionMeans, Collection<Drone> drones)
    {
        this.mapGroupAsked.clear();
        this.mapGroupActif.clear();
        this.mapGroupInactif.clear();
        this.mapGroupInTransit.clear();

        Iterator<InterventionMean> itInterventionMean = interventionMeans.iterator();
        InterventionMean interventionMean;
        Tool tool;
        while(itInterventionMean.hasNext())
        {
            interventionMean = itInterventionMean.next();

            if(interventionMean.getLocation().getGeopoint() == null) {
                tool = new Tool(interventionMean.getForm(), interventionMean.getRole());
                switch (interventionMean.getState()) {
                    case ASKED:
                        this.mapGroupAsked.put(tool, interventionMean);
                        break;
                    case VALIDATED:
                        this.mapGroupInTransit.put(tool, interventionMean);
                        break;
                    case ARRIVED:
                        this.mapGroupInactif.put(tool, interventionMean);
                        break;
                    case ENGAGED:
                        this.mapGroupActif.put(tool, interventionMean);
                        break;
                }
            }
        }

        Iterator<Drone> itDrone = drones.iterator();
        Drone drone;
        while(itDrone.hasNext())
        {
            drone = itDrone.next();

            if(drone.getLocation().getGeopoint() == null){

                tool = new Tool(drone.getForm(), drone.getRole());
                switch (drone.getState())
                {
                    case ASKED:
                        this.mapGroupAsked.put(tool, drone);
                        break;
                    case VALIDATED:
                        this.mapGroupInTransit.put(tool, drone);
                        break;
                    case ARRIVED:
                        this.mapGroupInactif.put(tool, drone);
                        break;
                    case ENGAGED:
                        this.mapGroupActif.put(tool, drone);
                        break;
                }
            }
        }

        refreshGroups();
    }

    private void refreshGroups(){
        ToolsGroup groupAsked;
        ToolsGroup groupInTransit;
        ToolsGroup groupInactif;
        ToolsGroup groupActif;

        groupAsked = new ToolsGroup("Demandés");
        Iterator<Tool> itAsked = this.mapGroupAsked.keySet().iterator();
        while(itAsked.hasNext())
        {
            groupAsked.addTool(itAsked.next());
        }

        groups.remove(1);
        groups.append(1, groupAsked);

        groupInTransit = new ToolsGroup("En transit");
        Iterator<Tool> itInTransit = this.mapGroupInTransit.keySet().iterator();
        while(itInTransit.hasNext())
        {
            groupInTransit.addTool(itInTransit.next());
        }

        groups.remove(2);
        groups.append(2, groupInTransit);

        groupInactif = new ToolsGroup("Inactifs");
        Iterator<Tool> itInactif = this.mapGroupInactif.keySet().iterator();
        while(itInactif.hasNext())
        {
            groupInactif.addTool(itInactif.next());
        }

        groups.remove(3);
        groups.append(3, groupInactif);

        groupActif = new ToolsGroup("Actifs");
        Iterator<Tool> itActif = this.mapGroupActif.keySet().iterator();
        while(itActif.hasNext())
        {
            groupActif.addTool(itActif.next());
        }

        groups.remove(4);
        groups.append(4, groupActif);
    }
}
