package edu.istic.tdf.dfclient.UI.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;
import java.util.List;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.domain.element.mean.IMean;
import edu.istic.tdf.dfclient.fragment.InterventionDetailFragment;

/**
 * Created by guerin on 01/06/16.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private List<IMean> meanList;
    private LayoutInflater inflater;
    private List<String> names;
    private InterventionDetailFragment.OnFragmentInteractionListener mListener;





    public ExpandableListAdapter(Context context, LayoutInflater inflater,ArrayList<String> names, List<IMean> means,InterventionDetailFragment.OnFragmentInteractionListener mlistener){

        this.meanList=means;
        this.inflater=inflater;
        this.names=names;
        this.mListener=mlistener;

    }


    @Override
    public int getGroupCount() {
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
