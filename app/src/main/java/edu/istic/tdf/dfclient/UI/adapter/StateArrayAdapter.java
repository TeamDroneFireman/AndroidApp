package edu.istic.tdf.dfclient.UI.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.domain.element.mean.MeanState;

/**
 * Created by tremo on 27/05/16.
 */
public class StateArrayAdapter extends ArrayAdapter<MeanState> {
    private List<MeanState> states = new ArrayList<>();
    private LayoutInflater inflater;

    public StateArrayAdapter(Context context, int resource, MeanState[] objects) {
        super(context, resource, objects);
    }

    public StateArrayAdapter(Context context, MeanState[] states){
        super(context, android.R.layout.simple_spinner_item, states);

        inflater = LayoutInflater.from(context);
        for(MeanState state : states){
            this.states.add(state);
        }

    }
}
