package edu.istic.tdf.dfclient.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.istic.tdf.dfclient.R;

public class MeansTableFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private TableLayout meanTab;

    //Bouchon
    private List<List<String>> existedMeans;

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
        loadMeans(existedMeans);

        return view;

    }

    private void loadMeans(List<List<String>> means) {
        for(int i=0;i< means.size();i++){
            List<String> current=means.get(i);
            TableRow tableRow=new TableRow(meanTab.getContext());
            for(int j=0;j<current.size();j++){
                TextView textView=new TextView(meanTab.getContext());
                textView.setText(current.get(i));
                tableRow.addView(textView);
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
        List<String> element=new ArrayList<>();
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
        existedMeans.add(element1);
    }

    public interface OnFragmentInteractionListener {
    }
}
