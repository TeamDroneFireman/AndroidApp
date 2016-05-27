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
import edu.istic.tdf.dfclient.domain.element.Element;
import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.drawable.PictoFactory;

/**
 * Created by Alexandre on 22/04/2016.
 */
public class ToolsListAdapter extends BaseExpandableListAdapter {

    private SparseArray<ToolsGroup> toolsGroups;
    private Context context;
    private int selectedChildPosition;
    private LayoutInflater inflater;
    private OnToolsListAdapterInteractionListener listener;

    public ToolsListAdapter(Context context, SparseArray<ToolsGroup> toolsGroups, OnToolsListAdapterInteractionListener listener) {
        this.context = context;
        this.toolsGroups = toolsGroups;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    private void setSelectedChildPosition(int position){
        ((Tool)getChild(0, position)).setRole(Role.PEOPLE);
        selectedChildPosition = position;
    }

    private int getSelectedChildPosition(){
        return selectedChildPosition;
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
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {

        final Tool children = (Tool) getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_toolbar_item, null);
        }

        ImageView icon = (ImageView) convertView.findViewById(R.id.imageView);

        Element element = listener.tryGetElementFromTool(children);

        if(element == null)
        {
            icon.setImageBitmap(PictoFactory.createPicto(context).setLabel("").setColor(children.getRole().getColor()).setDrawable(children.getForm().getDrawable()).toBitmap());
            //icon.setImageDrawable(PictoFactory.createPicto(context).setDrawable(children.getForm().getDrawable()).toDrawable());
            //icon.setImageDrawable(PictoFactory.createPicto(context).setDrawable(children.getForm().getDrawable()).toDrawable());
            //icon.setColorFilter(children.getRole().getColor());
        }
        else
        {
            icon.setImageDrawable(PictoFactory.createPicto(context).setElement(element).toDrawable());
            icon.setColorFilter(element.getRole().getColor());

        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setSelectedChildPosition(childPosition);
                        listener.handleSelectedTools(children);
                    }
                };

        convertView.setOnClickListener(onClickListener);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void cancelSelection() {
        ((Tool)getChild(0, selectedChildPosition)).setRole(Role.WHITE);
    }

    public interface OnToolsListAdapterInteractionListener {
        void handleSelectedTools(Tool tool);
        Element tryGetElementFromTool(Tool tool);
    }
}