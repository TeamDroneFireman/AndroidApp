package edu.istic.tdf.dfclient.UI.adapter;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.domain.element.mean.IMean;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;

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
    private LayoutInflater inflater;

    private HashMap<String,List<IMean>> hashMap;
    private ArrayList<Intervention> interventions;
    private Boolean isCodis;



    public InterventionListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public InterventionListAdapter(Context context, LayoutInflater inflater, ArrayList<String> interventions, boolean isCodis){
        super(context, R.layout.intervention_row, interventions);
        this.hashMap=new HashMap<>();
        this.inflater=inflater;
        this.interventions=new ArrayList<>();
        this.isCodis=isCodis;
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

        if(interventions.get(position)!=null) {

            View rowView = inflater.inflate(R.layout.intervention_row, null, true);

            TextView textView = (TextView) rowView.findViewById(R.id.textView7);
            textView.setText(interventions.get(position).getName() + "\n" + interventions.get(position).getLocation().getAddress());
            TextView badge = (TextView) rowView.findViewById(R.id.badge);

            if(isCodis) {

                int nb = getNbAskedMean(position);

                if (nb == 0) {
                    badge.setVisibility(View.INVISIBLE);
                } else {
                    try {
                        badge.setVisibility(View.VISIBLE);
                        badge.setText(String.valueOf(nb));
                    } catch (Exception e) {
                        System.out.print(e);
                    }
                }
            }else{
                badge.setVisibility(View.INVISIBLE);
            }

            switch (getItemViewType(position)) {
                case TYPE_ITEM_COLORED:
                    rowView.setAlpha(fa);
                    break;
                case TYPE_ITEM_NORMAL:
                    rowView.setAlpha(notfa);
                    break;
            }
            return rowView;
        }


        return convertView;
    }

    private int getNbAskedMean(int position) {
        Intervention intervention=interventions.get(position);
        if(hashMap.containsKey(intervention.getId())){
            List<IMean> list=hashMap.get(intervention.getId());
            if(!list.isEmpty()){
                return list.size();
            }
        }

        return 0;
    }


    public void setHashMap(HashMap<String, List<IMean>> hashMap) {
        this.hashMap = hashMap;
    }

    public void setArrayList(ArrayList<Intervention> arrayList) {
        this.interventions = arrayList;
    }
}
