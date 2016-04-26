package edu.istic.tdf.dfclient.fragment;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.UI.Tool;
import edu.istic.tdf.dfclient.UI.ToolsGroup;
import edu.istic.tdf.dfclient.UI.adapter.ToolsListAdapter;
import edu.istic.tdf.dfclient.drawable.element.DomainType;

public class ToolbarFragment extends Fragment implements ToolsListAdapter.OnToolsListAdapterInteractionListener {

    // UI
    @Bind(R.id.toolbar_listview) ExpandableListView listView;

    private SparseArray<ToolsGroup> groups = new SparseArray<ToolsGroup>();
    private OnFragmentInteractionListener mListener;

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

    public void handleSelectedTool(Tool tool){
        mListener.handleSelectedTool(tool);
    }



    public void createData() {

        ToolsGroup group;

        group = new ToolsGroup("Outils", false);
        group.addTool(new Tool("Moyen", DomainType.INTERVENTIONMEAN));
        group.addTool(new Tool("Point d'eau", DomainType.WATERPOINT));
        group.addTool(new Tool("Risque", DomainType.RISK));
        group.addTool(new Tool("Drone", DomainType.DRONE));
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


    public interface OnFragmentInteractionListener {
        void handleSelectedTool(Tool tool);
    }
}
