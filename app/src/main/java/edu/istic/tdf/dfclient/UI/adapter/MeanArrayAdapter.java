package edu.istic.tdf.dfclient.UI.adapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.UI.Mean;
import edu.istic.tdf.dfclient.UI.ToolsGroup;
import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.drawable.PictoFactory;

/**
 * Created by tremo on 23/05/16.
 */
public class MeanArrayAdapter extends ArrayAdapter<Mean>{

    private ArrayList<Mean> means = new ArrayList<Mean>();
    private LayoutInflater inflater;

    public MeanArrayAdapter(Context context, int resource, ArrayList<Mean> means) {
        super(context, resource, means);
    }

    public MeanArrayAdapter(Context context,ArrayList<Mean> means) {
        super(context, R.layout.item_mean, means);
        inflater = LayoutInflater.from(context);
        this.means=means;
    }



    public View getCustomView(final int position, View convertView, ViewGroup parent){
        View row = convertView;
        ViewHolder holder = null;
        //inflate row layout
        if(row==null){
            row =inflater.inflate(R.layout.item_mean, parent,false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        }else{
            holder =(ViewHolder) row.getTag();
        }
        if(means == null){
            Log.d("MeanArrayAdapter", "Means are null");
        }
        final Mean mean = means.get(position);
        //show data
        holder.textViewTitle.setText(mean.name);
        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mean.number = mean.number + 1;
                means.set(position, mean);
                notifyDataSetChanged();
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mean.number>0){
                    mean.number=mean.number-1;
                    means.set(position, mean);
                    notifyDataSetChanged();
                }
            }
        });
        holder.textViewMeanNumber.setText(String.valueOf(mean.number));
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return getCustomView(position, convertView, parent);
    }

    class ViewHolder {
        TextView textViewTitle ;
        TextView textViewMeanNumber;
        Button deleteButton;
        Button addButton;
        public ViewHolder(View v){
            textViewTitle =(TextView) v.findViewById(R.id.name);
            textViewMeanNumber =(TextView) v.findViewById(R.id.number);
            deleteButton =(Button) v.findViewById(R.id.delete_btn);
            addButton =(Button) v.findViewById(R.id.add_btn);
        }
    }


}
