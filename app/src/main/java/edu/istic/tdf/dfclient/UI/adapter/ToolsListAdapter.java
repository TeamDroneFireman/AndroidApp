package edu.istic.tdf.dfclient.UI.adapter;

import android.content.Context;
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
import edu.istic.tdf.dfclient.drawable.PictoFactory;

/**
 * Created by Alexandre on 22/04/2016.
 */
public class ToolsListAdapter extends BaseExpandableListAdapter {

    private SparseArray<ToolsGroup> toolsGroups;
    private Context context;
    private LayoutInflater inflater;
    private OnToolsListAdapterInteractionListener listener;

    /**
     * The current view, usefull for color
     */
    private View currentView;

    /**
     * The current tool, usefull for repaint when scrolling the expandable list view
     */
    private Tool currentTool;

    public ToolsListAdapter(Context context, SparseArray<ToolsGroup> toolsGroups, OnToolsListAdapterInteractionListener listener) {
        this.context = context;
        this.toolsGroups= toolsGroups;
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
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        int result = 0;
        switch (groupPosition)
        {
            case 0:
                result = childPosition;
                break;

            case 1:
                result = toolsGroups.get(0).getTools().size() + childPosition;
                break;

            case 2:
                result = toolsGroups.get(0).getTools().size() +
                        toolsGroups.get(1).getTools().size() + childPosition;
                break;

            case 3:
                result = toolsGroups.get(0).getTools().size() +
                        toolsGroups.get(1).getTools().size() +
                        toolsGroups.get(2).getTools().size() + childPosition;
                break;

            case 4:
                result = toolsGroups.get(0).getTools().size() +
                        toolsGroups.get(1).getTools().size() +
                        toolsGroups.get(2).getTools().size() +
                        toolsGroups.get(3).getTools().size() + childPosition;
                break;
        }

        return result;
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
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {
        final Tool children = (Tool) getChild(groupPosition, childPosition);

        convertView = inflater.inflate(R.layout.fragment_toolbar_item, null);

        //Set the color if this is the current selected tool
        //Require because scroll of the expandable list view repaint all element with default color
        if(children.equals(currentTool))
        {
            //it is the current selected list view, set to the selected view color
            convertView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
            currentView = convertView;
        }
        else
        {
            //it is not the current selected tool, set to the default color
            convertView.setBackgroundColor(context.getResources().getColor(R.color.darkGrey));
        }

        ImageView icon = (ImageView) convertView.findViewById(R.id.imageView);

        Element element = listener.tryGetElementFromTool(children);

        if(element == null)
        {
            icon.setImageBitmap(
                    PictoFactory.createPicto(context)
                            .setLabel("")
                            .setRole(children.getRole())
                            .setForm(children.getForm())
                            .toBitmap()
            );
        }
        else {
            icon.setImageBitmap(PictoFactory.createPicto(context).setElement(element).toBitmap());
            icon.setColorFilter(element.getRole().getColor());
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.handleSelectedTools(children);

                //Set the color of the previous selected view
                if(currentView != null)
                {
                    currentView.setBackgroundColor(context.getResources().getColor(R.color.darkGrey));
                }

                //Set the color of the new selected view
                view.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
                currentView = view;
                currentTool = children;
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
        if(currentView != null)
        {
            currentView.setBackgroundColor(context.getResources().getColor(R.color.darkGrey));
        }

        currentView = null;
        currentTool = null;
    }

    public interface OnToolsListAdapterInteractionListener {
        void handleSelectedTools(Tool tool);
        Element tryGetElementFromTool(Tool tool);
    }
}