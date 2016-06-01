package edu.istic.tdf.dfclient.UI.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.domain.element.Element;
import edu.istic.tdf.dfclient.domain.element.mean.IMean;
import edu.istic.tdf.dfclient.domain.element.mean.MeanState;
import edu.istic.tdf.dfclient.domain.element.mean.interventionMean.InterventionMean;
import edu.istic.tdf.dfclient.fragment.InterventionDetailFragment;

/**
 * Created by guerin on 31/05/16.
 */
public class ValidationTableAdapter extends ArrayAdapter<String> {
    private List<IMean> meanList;
    private LayoutInflater inflater;
    private List<String> names;
    private InterventionDetailFragment.OnFragmentInteractionListener mListener;


    public ValidationTableAdapter(Context context, int resource) {
        super(context, resource);
    }

    public ValidationTableAdapter(Context context, LayoutInflater inflater,ArrayList<String> names, List<IMean> means,InterventionDetailFragment.OnFragmentInteractionListener mlistener){
        super(context, R.layout.validation_row_table,names);
        this.meanList=means;
        this.inflater=inflater;
        this.names=names;
        this.mListener=mlistener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.validation_row_table, null, true);
        TextView textView = (TextView) rowView.findViewById(R.id.textView8);
        final IMean mean=getMean(position);
        textView.setText(mean.getName());

        Button validation=(Button)rowView.findViewById(R.id.validation_details_inter_button);
        validation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mean.setState(MeanState.VALIDATED);
                mListener.handleValidation( mean);

            }
        });


        Button refuse=(Button)rowView.findViewById(R.id.refused_button);
        refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mean.setState(MeanState.RELEASED);
                mListener.handleValidation( mean);
            }
        });

        return rowView;
    }

    private IMean getMean(int position) {
        String name=names.get(position);

        for(int i=0; i<meanList.size();i++){
            if(meanList.get(i).getName().equals(name)){
                return meanList.get(i);
            }
        }
        return null;
    }
}
