package edu.istic.tdf.dfclient.UI.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.util.ArrayList;

import edu.istic.tdf.dfclient.R;

/**
 * Created by tremo on 26/05/16.
 */
public class InterventionListAdapter extends ArrayAdapter<String> {

    private static final int a = 3;
    private static final int  b = 7;
    private static final float fa = (float) a / (float) b;
    private static final float notfa = 1;
    private static int COUNT_NOT_ARCHIVED = 4;
    private static final int TYPE_ITEM_COLORED = 1;
    private static final int TYPE_ITEM_NORMAL = 0;


    public InterventionListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public InterventionListAdapter(Context context,ArrayList<String> interventions){
        super(context, android.R.layout.simple_list_item_1, interventions);
    }

    @Override
    public int getItemViewType(int position) {
        return (position >= COUNT_NOT_ARCHIVED) ? TYPE_ITEM_COLORED : TYPE_ITEM_NORMAL;
    }

    public void setCountNotArchived(int i){
        COUNT_NOT_ARCHIVED=i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);

        switch (getItemViewType(position)) {
            case TYPE_ITEM_COLORED:
                v.setAlpha(fa);
                break;
            case TYPE_ITEM_NORMAL:
                v.setAlpha(notfa);
                break;
        }
        return v;

    }
}
