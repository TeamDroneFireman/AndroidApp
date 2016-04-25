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

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.UI.Tool;
import edu.istic.tdf.dfclient.UI.ToolsGroup;
import edu.istic.tdf.dfclient.UI.adapter.ToolsListAdapter;

public class ToolbarFragment extends Fragment implements ToolsListAdapter.OnToolsListAdapterInteractionListener {

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

        createData();
        ExpandableListView listView = (ExpandableListView) view.findViewById(R.id.toolbar_listview);
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
        group.addTool(new Tool("Test1"));
        group.addTool(new Tool("Test1"));
        group.addTool(new Tool("Test13"));
        group.addTool(new Tool("Test1"));
        group.addTool(new Tool("Test2"));
        group.addTool(new Tool("Test1"));
        group.addTool(new Tool("Test1"));
        group.addTool(new Tool());
        group.addTool(new Tool());
        group.addTool(new Tool());
        groups.append(0, group);

        group = new ToolsGroup("Demandés");
        group.addTool(new Tool());
        group.addTool(new Tool());
        group.addTool(new Tool());
        group.addTool(new Tool());
        groups.append(1, group);

        group = new ToolsGroup("En transit");
        group.addTool(new Tool());
        groups.append(2, group);

        group = new ToolsGroup("Inactifs");
        groups.append(3, group);

        group = new ToolsGroup("Actifs");
        group.addTool(new Tool());
        group.addTool(new Tool());
        group.addTool(new Tool());
        group.addTool(new Tool());
        group.addTool(new Tool());
        groups.append(4, group);

    }


    public interface OnFragmentInteractionListener {
        void handleSelectedTool(Tool tool);
    }
}
