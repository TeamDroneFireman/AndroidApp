package edu.istic.tdf.dfclient.UI.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.UI.ToolsGroup;
import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.drawable.PictoFactory;

/**
 * Created by tremo on 23/05/16.
 */
public class MeanArrayAdapter extends ArrayAdapter<String> {

    private ArrayList<String> means = new ArrayList<String>();
    private LayoutInflater inflater;




    public MeanArrayAdapter(Context context, ArrayList<String> means){
        super(context, R.layout.item_mean, means);

        for(String mean : means){
            this.means.add(mean);
        }
        inflater = LayoutInflater.from(context);
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

        String mean = means.get(position);

        //show data
        holder.textViewTitle.setText(mean);
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                means.remove(position); //or some other task
                notifyDataSetChanged();
            }
        });


        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    class ViewHolder {
        TextView textViewTitle ;
        Button deleteButton;
        public ViewHolder(View v){
            textViewTitle =(TextView) v.findViewById(R.id.mean_name);
            deleteButton =(Button) v.findViewById(R.id.delete_btn);

        }
    }
}
