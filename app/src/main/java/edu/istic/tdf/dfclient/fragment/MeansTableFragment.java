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
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.domain.element.Element;
import edu.istic.tdf.dfclient.domain.element.IElement;
import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.domain.element.mean.IMean;
import edu.istic.tdf.dfclient.domain.element.mean.MeanState;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;

public class MeansTableFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private TableLayout meanTab;

    //private Intervention intervention;

    //Bouchon
   // private List<List<String>> existedMeans=new ArrayList<>();

    private List<IMean> means=new ArrayList<>();

    private HashMap<IMean,TableRow> link=new HashMap<>();

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
        //this.intervention=mListener.getIntervention();
        //getExistedMeans();

        View view=inflater.inflate(R.layout.fragment_means_table, container, false);
        meanTab=(TableLayout)view.findViewById(R.id.meanTab);
        //loadMeans(existedMeans);
        //loadMeans();

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
            addElment(means.get(i));
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

   /* public void getExistedMeans() {
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
       /* List<IElement> elements= (List) intervention.getElements();
        for(int i=0;i<elements.size();i++){
            if(elements.get(i) instanceof IMean){
                means.add((IMean)elements.get(i));
            }
        }
    }*/

    public void updateElement(IElement element) {
        if (element instanceof IMean){//TODO Here change condition
            if(means.contains(element)){
                updateElem((IMean) element);
            }else{
                addElment((IMean) element);
                means.add((IMean)element);
            }
        }
    }

    public void updateElements(Collection<IElement> element) {
        Iterator<IElement> iterator= element.iterator();
        while (iterator.hasNext()){
            updateElement(iterator.next());
        }
    }


    private void addElment(IMean element) {
        HashMap<MeanState,Date> currentStates=element.getStates();
        TableRow tableRow=new TableRow(meanTab.getContext());

        TextView name=new TextView(meanTab.getContext());
        name.setText(element.getName());
        name.setGravity(Gravity.CENTER_HORIZONTAL);
        tableRow.addView(name);

        tableRow.setBackgroundColor(element.getRole().getColor());

        Date d=currentStates.get(MeanState.ASKED);
        addMeanState(tableRow,d);
        d=currentStates.get(MeanState.ARRIVED);
        addMeanState(tableRow,d);
        d=currentStates.get(MeanState.ENGAGED);
        addMeanState(tableRow,d);
        d=currentStates.get(MeanState.RELEASED);
        addMeanState(tableRow,d);

        link.put(element,tableRow);
        meanTab.addView(tableRow);
    }

    private void updateElem(IMean element) {
        TableRow tableRow =link.get(element);
        tableRow.removeAllViews();
        tableRow.setBackgroundColor(element.getRole().getColor());

        TextView name=new TextView(meanTab.getContext());
        name.setText(element.getName());
        name.setGravity(Gravity.CENTER_HORIZONTAL);
        tableRow.addView(name);

        HashMap<MeanState,Date> currentStates=element.getStates();

        Date d=currentStates.get(MeanState.ASKED);
        addMeanState(tableRow,d);
        d=currentStates.get(MeanState.ARRIVED);
        addMeanState(tableRow,d);
        d=currentStates.get(MeanState.ENGAGED);
        addMeanState(tableRow,d);
        d=currentStates.get(MeanState.RELEASED);
        addMeanState(tableRow,d);



    }

    private void addMeanState(TableRow tableRow,Date d){

        TextView textView=new TextView(meanTab.getContext());
        if(d!=null) {
            textView.setText(d.toString());
        }else{
            textView.setText("");
        }
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        tableRow.addView(textView);
    }

    public interface OnFragmentInteractionListener {

    }
}
