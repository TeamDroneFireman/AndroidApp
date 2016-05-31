package edu.istic.tdf.dfclient.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
import edu.istic.tdf.dfclient.domain.element.Element;
import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.domain.element.mean.IMean;
import edu.istic.tdf.dfclient.domain.element.mean.MeanState;
import edu.istic.tdf.dfclient.domain.element.mean.drone.Drone;
import edu.istic.tdf.dfclient.domain.element.mean.interventionMean.InterventionMean;
import edu.istic.tdf.dfclient.drawable.PictoFactory;

public class ToolbarFragment extends Fragment implements ToolsListAdapter.OnToolsListAdapterInteractionListener, Observer {

    // UI
    @Bind(R.id.toolbar_listview) ExpandableListView listView;

    ToolsListAdapter toolsListAdapter;

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
        toolsListAdapter = new ToolsListAdapter(getContext(), groups, this);
        listView.setAdapter(toolsListAdapter);
        refreshGroups();

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

    public void handleSelectedTools(Tool tool){
        mListener.handleSelectedToolUtils(tool);
    }

    public void createData() {

        ToolsGroup group;

        group = new ToolsGroup("Outils", false);
        group.addTool(new Tool(PictoFactory.ElementForm.MEAN, Role.WHITE));
        group.addTool(new Tool(PictoFactory.ElementForm.MEAN_GROUP, Role.WHITE));
        group.addTool(new Tool(PictoFactory.ElementForm.MEAN_COLUMN, Role.WHITE));

        group.addTool(new Tool(PictoFactory.ElementForm.MEAN_OTHER, Role.WHITE));

        group.addTool(new Tool(PictoFactory.ElementForm.AIRMEAN, Role.WHITE));

        group.addTool(new Tool(PictoFactory.ElementForm.WATERPOINT, Role.WHITE));
        group.addTool(new Tool(PictoFactory.ElementForm.WATERPOINT_SUSTAINABLE, Role.WHITE));
        group.addTool(new Tool(PictoFactory.ElementForm.WATERPOINT_SUPPLY, Role.WHITE));

        group.addTool(new Tool(PictoFactory.ElementForm.SOURCE, Role.WHITE));
        group.addTool(new Tool(PictoFactory.ElementForm.TARGET, Role.WHITE));

        groups.append(0, group);

        group = new ToolsGroup("Demandés");
        groups.append(1, group);

        group = new ToolsGroup("Va");
        groups.append(2, group);

        group = new ToolsGroup("Inactifs");
        groups.append(3, group);

        group = new ToolsGroup("Actifs");
        groups.append(4, group);

    }

    @Override
    public void update(Observable observable, Object data) {
    }

    public void cancelSelection() {
        this.toolsListAdapter.cancelSelection();
    }

    public interface OnFragmentInteractionListener {
        void handleSelectedToolUtils(Tool tool);
    }

    /**
     * Dispatch all interventionmeans and drones into the 4 map, then call refresh()
     * @param interventionMeans
     * @param drones
     */
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

    /**
     * refresh the listview with the 4 map content
     */
    private void refreshGroups(){
        ToolsGroup groupAsked;
        ToolsGroup groupInTransit;
        ToolsGroup groupInactif;
        ToolsGroup groupActif;

        groupAsked = new ToolsGroup("Demandés");
        Iterator<Tool> itAsked = this.mapGroupAsked.keySet().iterator();
        ArrayList<Tool> groupAskedArrayList = new ArrayList<>();
        while(itAsked.hasNext())
        {
            groupAskedArrayList.add(itAsked.next());
        }

        this.sortToolsByMeanAskedDate(groupAskedArrayList);

        for (Tool tool: groupAskedArrayList) {
            groupAsked.addTool(tool);
        }

        groups.remove(1);
        groups.append(1, groupAsked);

        groupInTransit = new ToolsGroup("En transit");
        Iterator<Tool> itInTransit = this.mapGroupInTransit.keySet().iterator();
        ArrayList<Tool> groupInTransitArrayList = new ArrayList<>();
        while(itInTransit.hasNext())
        {
            groupInTransitArrayList.add(itInTransit.next());
        }

        this.sortToolsByMeanAskedDate(groupInTransitArrayList);

        for (Tool tool: groupInTransitArrayList ) {
            groupInTransit.addTool(tool);
        }

        groups.remove(2);
        groups.append(2, groupInTransit);

        groupInactif = new ToolsGroup("Inactifs");
        Iterator<Tool> itInactif = this.mapGroupInactif.keySet().iterator();
        ArrayList<Tool> groupInactifArrayList = new ArrayList<>();
        while(itInactif.hasNext())
        {
            groupInactifArrayList.add(itInTransit.next());
        }

        this.sortToolsByMeanAskedDate(groupInactifArrayList);

        for (Tool tool: groupInactifArrayList ) {
            groupInactif.addTool(tool);
        }

        groups.remove(3);
        groups.append(3, groupInactif);

        groupActif = new ToolsGroup("Actifs");
        Iterator<Tool> itActif = this.mapGroupActif.keySet().iterator();
        ArrayList<Tool> groupActifArrayList = new ArrayList<>();
        while(itActif.hasNext())
        {
            groupActifArrayList.add(itInTransit.next());
        }

        this.sortToolsByMeanAskedDate(groupActifArrayList);

        for (Tool tool: groupActifArrayList ) {
            groupActif.addTool(tool);
        }

        groups.remove(4);
        groups.append(4, groupActif);

        if(toolsListAdapter != null)
        {
            toolsListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * sort tools using the asked date
     * @param tools
     */
    private void sortToolsByMeanAskedDate(ArrayList<Tool> tools){
        Comparator<Tool> toolComparator = new Comparator<Tool>() {
            @Override
            public int compare(Tool lhs, Tool rhs) {
                //compare date
                Element e1 = tryGetElementFromTool(lhs);
                Element e2 = tryGetElementFromTool(rhs);

                if(e1 == null)
                {
                    return 1;
                }

                if(e2 == null)
                {
                    return -1;
                }

                Date date1 = ((IMean)e1).getStates().get(MeanState.ASKED);
                Date date2 = ((IMean)e2).getStates().get(MeanState.ASKED);

                return date2.compareTo(date1);
            }
        };

        Collections.sort(tools, toolComparator);
    }

    /**
     *
     * @return an element of type Drone or Mean that is present in the toolbar
     */
    public Element tryGetElementFromTool(Tool tool)
    {
        Element element;

        element = tryGetElementAskedFromTool(tool);
        if(element != null)
        {
            return element;
        }

        element = tryGetElementInTransitFromTool(tool);
        if(element != null)
        {
            return element;
        }

        element = tryGetElementInactifFromTool(tool);
        if(element != null)
        {
            return element;
        }

        element = tryGetElementActifFromTool(tool);
        if(element != null)
        {
            return element;
        }

        return null;
    }

    private Element tryGetElementAskedFromTool(Tool tool)
    {
        return (Element)this.mapGroupAsked.get(tool);
    }

    private Element tryGetElementInTransitFromTool(Tool tool)
    {
        return (Element)this.mapGroupInTransit.get(tool);
    }

    private Element tryGetElementInactifFromTool(Tool tool)
    {
        return (Element)this.mapGroupInactif.get(tool);
    }

    private Element tryGetElementActifFromTool(Tool tool)
    {
        return (Element)this.mapGroupActif.get(tool);
    }
}
