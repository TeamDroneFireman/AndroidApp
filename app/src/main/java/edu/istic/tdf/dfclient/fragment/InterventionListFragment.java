package edu.istic.tdf.dfclient.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.TdfApplication;
import edu.istic.tdf.dfclient.auth.Credentials;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;

public class InterventionListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    @Bind(R.id.interventionCreationButton)
    Button interventionCreationBt;

    @Bind(R.id.interventions_list)
    ListView interventionsList;

    // for listView sinister_code
    private ArrayList<String> interventions = new ArrayList<String>();
    private ArrayAdapter<String> interventionsAdapter;

    // the collection of all ibject interventions
    Map<String,Intervention> nameInterventionMap = new HashMap<>();


    public InterventionListFragment() {
        // Required empty public constructor
    }

    public static InterventionListFragment newInstance() {
        InterventionListFragment fragment = new InterventionListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_intervention_list, container, false);
        ButterKnife.bind(this, view);// Inflate the layout for this fragment

        //interventionCreationBt
        interventionCreationBt.setEnabled(isCodis());

        interventionCreationBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.handleInterventionCreation();
            }
        });

        //interventionsList
        interventionsAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                interventions);

        interventionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: 26/04/16
                //Intervention intervention = nameInterventionMap.get(interventions.get(position));
                Intervention intervention = null;
                mListener.handleInterventionSelected(intervention);
            }
        });

                interventionsList.setAdapter(interventionsAdapter);


        loadInterventions();

        return view;
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

    public interface OnFragmentInteractionListener {

        // When button creation intervention clicked
        void handleInterventionCreation();

        //
        void handleInterventionSelected(Intervention intervention);
    }

    private boolean isCodis(){
        // TODO: 26/04/16
        Credentials credentials = ((TdfApplication)this.getActivity().getApplication()).loadCredentials();
        return credentials.isCodisUser();
    }

    private void loadInterventions(){
        // TODO: 26/04/16 request to get all intervention
        // add all intervention.name
        // add all intervention the map
        nameInterventionMap.put("SAP 1", null);
        interventions.add("SAP 1");
        nameInterventionMap.put("SAP 2", null);
        interventions.add("SAP 2");

        nameInterventionMap.put("INC 1", null);
        interventions.add("INC 1");
        nameInterventionMap.put("INC 2", null);
        interventions.add("INC 2");

        interventionsAdapter.notifyDataSetChanged();
    }
}
