package edu.istic.tdf.dfclient.UI.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.UI.Tool;
import edu.istic.tdf.dfclient.UI.ToolsGroup;
import edu.istic.tdf.dfclient.drawable.PictoFactory;

/**
 * Created by Alexandre on 22/04/2016.
 */
public class ToolsListAdapter extends BaseExpandableListAdapter {

    private SparseArray<ToolsGroup> toolsGroups;
    private Context context;
    private LayoutInflater inflater;
    private OnToolsListAdapterInteractionListener listener;

    public ToolsListAdapter(Context context, SparseArray<ToolsGroup> toolsGroups, OnToolsListAdapterInteractionListener listener) {
        this.context = context;
        this.toolsGroups = toolsGroups;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public int getGroupCount() {
        return toolsGroups.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return toolsGroups.get(groupPosition).getTools().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return toolsGroups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return toolsGroups.get(groupPosition).getTools().get(childPosition);
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

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_toolbar_group, null);
        }
        ToolsGroup group = (ToolsGroup) getGroup(groupPosition);
        String groupTitle = group.getTitle();
        if (group.isCountable()) {
            groupTitle += " (" + group.getTools().size() + ")";
        }
        ((CheckedTextView) convertView).setText(groupTitle);
        ((CheckedTextView) convertView).setChecked(isExpanded);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final Tool children = (Tool) getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_toolbar_item, null);
        }

        ImageView icon = (ImageView) convertView.findViewById(R.id.imageView);

        icon.setImageDrawable(PictoFactory.createPicto(context).setDrawable(children.getForm().getDrawable()).toDrawable());
        icon.setColorFilter(Color.WHITE);

        View.OnClickListener onClickListener = null;
        switch (groupPosition)
        {
            case 0:
                onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.handleSelectedToolUtils(children);
                    }
                };
                break;
            case 1:
                onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.handleSelectedToolAsked(children);
                    }
                };
                break;
            case 2:
                onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.handleSelectedToolInTransit(children);
                    }
                };
                break;
            case 3:
                onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.handleSelectedToolInactif(children);
                    }
                };
                break;
            case 4:
                onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.handleSelectedToolActif(children);
                    }
                };
                break;
        }

        convertView.setOnClickListener(onClickListener);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public interface OnToolsListAdapterInteractionListener {
        void handleSelectedToolUtils(Tool tool);
        void handleSelectedToolAsked(Tool tool);
        void handleSelectedToolInTransit(Tool tool);
        void handleSelectedToolInactif(Tool tool);
        void handleSelectedToolActif(Tool tool);
    }
}