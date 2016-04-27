package edu.istic.tdf.dfclient.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.TdfApplication;
import edu.istic.tdf.dfclient.auth.Credentials;
import edu.istic.tdf.dfclient.dao.DaoSelectionParameters;
import edu.istic.tdf.dfclient.dao.domain.InterventionDao;
import edu.istic.tdf.dfclient.dao.handler.IDaoSelectReturnHandler;
import edu.istic.tdf.dfclient.domain.geo.Location;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;
import edu.istic.tdf.dfclient.domain.intervention.SinisterCode;

public class InterventionListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    @Bind(R.id.interventionCreationButton)
    Button interventionCreationBt;

    @Bind(R.id.interventions_list)
    ListView interventionsList;

    InterventionDao interventionDao;

    // for listView intervention
    private ArrayList<String> interventions = new ArrayList<String>();
    private ArrayAdapter<String> interventionsAdapter;
    private boolean interventionsIsDirty;

    // the collection of all object interventions
    Map<String,Intervention> nameInterventionMap = new HashMap<>();


    public InterventionListFragment() {
        // Required empty public constructor
    }

    public static InterventionListFragment newInstance(InterventionDao interventionDao) {
        InterventionListFragment fragment = new InterventionListFragment();
        fragment.interventionDao = interventionDao;
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
                Intervention intervention = nameInterventionMap.get(interventions.get(position));
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
        Credentials credentials = ((TdfApplication)this.getActivity().getApplication()).loadCredentials();
        return credentials.isCodisUser();
    }

    private void loadInterventions(){
        interventionDao.findAll(new DaoSelectionParameters(), new IDaoSelectReturnHandler<List<Intervention>>() {
            @Override
            public void onRepositoryResult(List<Intervention> interventionList) {
            }

            @Override
            public void onRestResult(List<Intervention> interventionList) {
                Iterator<Intervention> interventionIterator = interventionList.iterator();
                Intervention intervention;
                while (interventionIterator.hasNext()) {
                    intervention = interventionIterator.next();
                    nameInterventionMap.put(intervention.getName(), intervention);
                }

                addSortedInterventions();
            }

            @Override
            public void onRepositoryFailure(Throwable e) {
                Log.e("", "REPO FAILURE");
            }

            @Override
            public void onRestFailure(Throwable e) {
                Log.e("", "REST FAILURE");
            }
        });

        Intervention interventionBouchon = new Intervention();
        interventionBouchon.setName("Bouchon");
        interventionBouchon.setCreationDate(new Date());
        interventionBouchon.setSinisterCode(SinisterCode.FDF);
        interventionBouchon.setLocation(new Location());
        interventions.add(interventionBouchon.getName());

        nameInterventionMap.put(interventionBouchon.getName(),interventionBouchon);
    }

    private void addSortedInterventions(){
        ArrayList<Intervention> interventionArrayList = new ArrayList<>(nameInterventionMap.values());

        Collections.sort(interventionArrayList, new Comparator<Intervention>() {
            @Override
            public int compare(Intervention lhs, Intervention rhs) {
                Date date1 = lhs.getCreationDate();
                Date date2 = rhs.getCreationDate();

                return date2.compareTo(date1);
            }
        });

        Iterator<Intervention> it = interventionArrayList.iterator();
        Intervention intervention;
        while(it.hasNext())
        {
            intervention = it.next();
            interventions.add(intervention.getName());
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                interventionsAdapter.notifyDataSetChanged();
            }
        });
    }
}
