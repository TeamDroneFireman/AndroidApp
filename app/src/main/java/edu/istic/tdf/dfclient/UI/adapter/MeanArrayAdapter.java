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

import java.util.ArrayList;
import java.util.List;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.UI.ToolsGroup;
import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.drawable.PictoFactory;

/**
 * Created by tremo on 23/05/16.
 */
public class MeanArrayAdapter extends BaseAdapter{

    private ArrayList<Mean> means = new ArrayList<Mean>();
    private LayoutInflater inflater;


    public MeanArrayAdapter(Context context, ArrayList<Mean> means) {
        this.inflater = LayoutInflater.from(context);
        this.means = means;
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

        Mean mean = means.get(position);

        //show data
        holder.textViewTitle.setText(mean.name);
        holder.textViewMeanNumber.setText(mean.number);
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Test deleteButton", "Test deleteButton");
                notifyDataSetChanged();
            }
        });
        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Test addButton", "Test addButton");
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
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
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
            textViewTitle =(TextView) v.findViewById(R.id.mean_name);
            textViewMeanNumber =(TextView) v.findViewById(R.id.mean_count);
            deleteButton =(Button) v.findViewById(R.id.delete_btn);
            addButton =(Button) v.findViewById(R.id.add_btn);
        }
    }

    class Mean{
        String name;
        int number;
    }
}
