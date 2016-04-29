package edu.istic.tdf.dfclient.fragment;

import android.content.Context;
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

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.domain.element.Element;
import edu.istic.tdf.dfclient.domain.element.ElementType;
import edu.istic.tdf.dfclient.domain.element.IElement;
import edu.istic.tdf.dfclient.domain.element.mean.IMean;
import edu.istic.tdf.dfclient.domain.element.mean.MeanState;

public class MeansTableFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private TableLayout meanTab;

    private List<IMean> means=new ArrayList<>();

    private HashMap<String,TableRow> link=new HashMap<>();

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


        View view=inflater.inflate(R.layout.fragment_means_table, container, false);
        meanTab=(TableLayout)view.findViewById(R.id.meanTab);

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

        addTextViews(element,tableRow);

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

    public interface OnFragmentInteractionListener {

    }

    public void removeElement(Element element){
        // TODO: 29/04/16  
    }

    public void removeElements(Collection<Element> elements){
        // TODO: 29/04/16  
    }
}
