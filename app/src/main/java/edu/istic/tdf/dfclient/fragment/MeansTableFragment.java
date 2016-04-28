package edu.istic.tdf.dfclient.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.domain.element.Element;
import edu.istic.tdf.dfclient.domain.element.IElement;
import edu.istic.tdf.dfclient.domain.element.mean.IMean;
import edu.istic.tdf.dfclient.domain.element.mean.MeanState;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;

public class MeansTableFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private TableLayout meanTab;

    private Intervention intervention;

    //Bouchon
   // private List<List<String>> existedMeans=new ArrayList<>();

    private List<IMean> means=new ArrayList<>();

    public MeansTableFragment() {
        // Required empty public constructor
    }

    public static MeansTableFragment newInstance() {
        MeansTableFragment fragment = new MeansTableFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getExistedMeans();

        View view=inflater.inflate(R.layout.fragment_means_table, container, false);
        meanTab=(TableLayout)view.findViewById(R.id.meanTab);
        //loadMeans(existedMeans);
        loadMeans();

        return view;

    }

    private void loadMeans(/*List<List<String>> means*/) {
       /*for(int i=0;i< means.size();i++){
            List<String> current=means.get(i);
            TableRow tableRow=new TableRow(meanTab.getContext());
            for(int j=0;j<current.size();j++){
                TextView textView=new TextView(meanTab.getContext());
                textView.setText(current.get(j));
                tableRow.addView(textView);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
            }

            meanTab.addView(tableRow);
        }*/

        for(int i=0;i<means.size();i++){
            HashMap<MeanState,Date> currentStates=means.get(i).getStates();
            //HashMap<MeanState,Date> currentStates=new HashMap<>();
            TableRow tableRow=new TableRow(meanTab.getContext());

            Set<MeanState> stateSet=currentStates.keySet();
            Iterator<MeanState> iterator=stateSet.iterator();


            while(iterator.hasNext()){
                MeanState current=iterator.next();
                TextView textView=new TextView(meanTab.getContext());
                textView.setText(currentStates.get(current).toString());
                tableRow.addView(textView);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);

            }

            meanTab.addView(tableRow);


        }




    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void getExistedMeans() {
        /*List<String> element=new ArrayList<>();
        element.add("SAP1");
        element.add("11:15");
        element.add("12:00");
        element.add("");
        element.add("");

        List<String> element1=new ArrayList<>();
        element1.add("SAP2");
        element1.add("11:16");
        element1.add("");
        element1.add("");
        element1.add("");

        existedMeans.add(element);
        existedMeans.add(element1);*/
        List<IElement> elements= (List) intervention.getElements();
        for(int i=0;i<elements.size();i++){
            if(elements.get(i) instanceof IMean){
                means.add((IMean)elements.get(i));
            }
        }
    }

    public interface OnFragmentInteractionListener {
    }
}
