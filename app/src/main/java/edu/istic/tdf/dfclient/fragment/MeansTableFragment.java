package edu.istic.tdf.dfclient.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.domain.element.Element;
import edu.istic.tdf.dfclient.domain.element.ElementType;
import edu.istic.tdf.dfclient.domain.element.IElement;
import edu.istic.tdf.dfclient.domain.element.mean.IMean;
import edu.istic.tdf.dfclient.domain.element.mean.MeanState;
import edu.istic.tdf.dfclient.domain.element.mean.drone.Drone;
import edu.istic.tdf.dfclient.domain.element.mean.drone.IDrone;
import edu.istic.tdf.dfclient.domain.element.mean.interventionMean.IInterventionMean;
import edu.istic.tdf.dfclient.domain.element.mean.interventionMean.InterventionMean;
import edu.istic.tdf.dfclient.domain.geo.Location;
import edu.istic.tdf.dfclient.domain.intervention.IIntervention;

public class MeansTableFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private TableLayout meanTab;

    private List<IMean> means=new ArrayList<>();

    private Spinner spinner;

    private HashMap<String,TableRow> link=new HashMap<>();

    private final int NBCOLUMS=6;
    private List meanList;

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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view=inflater.inflate(R.layout.fragment_means_table, container, false);
        meanTab=(TableLayout)view.findViewById(R.id.meanTab);


        spinner=(Spinner)view.findViewById(R.id.spinner);


        final List meanList = getDefaultMeanList();
        ArrayAdapter adapter = new ArrayAdapter(this.getContext(),android.R.layout.simple_spinner_item,meanList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        Button validation=(Button)view.findViewById(R.id.meanTableValidationbtn);
        validation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Element element;
                if(spinner.getSelectedItem().toString().equals("DRONE")){
                    Drone mean=new Drone();
                    mean.setState(MeanState.ASKED);
                    mean.setLocation(new Location());
                    element=mean;
                }else{
                    InterventionMean mean=new InterventionMean();
                    mean.setState(MeanState.ASKED);
                    //TODO voir comment gerer les différents type d'intervention mean
                    mean.setName(spinner.getSelectedItem().toString());
                    mean.setLocation(new Location());
                    element=mean;
                }
                mListener.handleValidation(element);
            }
        });
        //meanTab.addView(createEmptyRow());
        return view;

    }

    private void loadMeans(/*List<List<String>> means*/) {

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


    public void updateElement(IElement element) {
        if (element.getType()== ElementType.AIRMEAN || element.getType()==ElementType.MEAN){

            if(link.containsKey(element.getId())){
                updateElem((IMean) element);
            }else{
                addElment((IMean) element);
                means.add((IMean)element);
            }
        }
    }

    public void updateElements(Collection<Element> element) {
        Iterator<Element> iterator= element.iterator();
        while (iterator.hasNext()){
            updateElement(iterator.next());
        }
    }


    private void addElment(IMean element) {

        TableRow tableRow=new TableRow(meanTab.getContext());

        addTextViews(element,tableRow);

        link.put(element.getId(),tableRow);
        meanTab.addView(tableRow);
    }

    private void updateElem(IMean element) {
        TableRow tableRow =link.get(element.getId());
        tableRow.removeAllViews();

        addTextViews(element, tableRow);

    }

    private void addTextViews(IMean element,TableRow tableRow) {
        TextView name = new TextView(meanTab.getContext());
        name.setText(element.getName());
        name.setGravity(Gravity.CENTER_HORIZONTAL);
        tableRow.addView(name);

        tableRow.setBackgroundColor(element.getRole().getColor());
        HashMap<MeanState, Date> currentStates = element.getStates();

        Date d = currentStates.get(MeanState.ASKED);
        addMeanState(tableRow, d);
        d = currentStates.get(MeanState.VALIDATED);
        addMeanState(tableRow, d);
        d = currentStates.get(MeanState.ARRIVED);
        addMeanState(tableRow, d);
        d = currentStates.get(MeanState.ENGAGED);
        addMeanState(tableRow, d);
        d = currentStates.get(MeanState.RELEASED);
        addMeanState(tableRow, d);
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

    @Deprecated
    private TableRow createEmptyRow(){
        TableRow tableRow=new TableRow(meanTab.getContext());
        addTextViewsEditable(tableRow);

        return tableRow;
    }

    @Deprecated
    private void addTextViewsEditable(TableRow tableRow) {
        for(int i=0;i<NBCOLUMS;i++){
            TextView textView=new TextView(meanTab.getContext());
            textView.setText("");
            textView.setGravity(Gravity.CENTER_HORIZONTAL);

            tableRow.addView(textView);
            if(i==0) {
                textView.setEnabled(true);
            }
        }
    }

    public List getDefaultMeanList() {
        //TODO recup dans la base la liste des moyens
        List<String> list=new ArrayList<>();
        list.add("FPT");
        list.add("VSAV");
        list.add("EPA");
        list.add("VLCG");
        list.add("CCF");
        list.add("CCGC");
        list.add("VLHR");
        list.add("DRONE");
        return list;
    }


    public interface OnFragmentInteractionListener {

        public void handleValidation(Element element);

    }

    public void removeElement(Element element){
        // TODO: 29/04/16  
    }

    public void removeElements(Collection<Element> elements){
        // TODO: 29/04/16  
    }
}
