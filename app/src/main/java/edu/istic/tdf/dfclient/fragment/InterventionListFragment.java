package edu.istic.tdf.dfclient.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.TdfApplication;
import edu.istic.tdf.dfclient.auth.Credentials;
import edu.istic.tdf.dfclient.dao.DaoSelectionParameters;
import edu.istic.tdf.dfclient.dao.domain.InterventionDao;
import edu.istic.tdf.dfclient.dao.handler.IDaoSelectReturnHandler;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;

public class InterventionListFragment extends Fragment {

    // UI
    @Bind(R.id.interventionCreationButton) Button interventionCreationBt;
    @Bind(R.id.interventions_list) ListView interventionsList;
    @Bind(R.id.pull_to_refresh_interventions) SwipeRefreshLayout pullToRefresh;

    // Data
    InterventionDao interventionDao;
    private ArrayList<String> interventions = new ArrayList<>();    // for listView intervention
    private ArrayAdapter<String> interventionsAdapter;
    ArrayList<Intervention> interventionArrayList = new ArrayList<>();    // the collection of all object interventions

    // Fragment listener
    private OnFragmentInteractionListener fragmentInteractionListener;


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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_intervention_list, container, false);
        ButterKnife.bind(this, view);// Inflate the layout for this fragment

        // Toggle creation button
        displayCreationBt();

        //Data
        interventionsAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1,
                interventions);
        interventionsList.setAdapter(interventionsAdapter);

        // Events
        interventionCreationBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentInteractionListener.handleInterventionCreation();
            }
        });

        interventionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                //if (!view.equals(currentSelectedView)) {
                    //if (currentSelectedView != null) {
                        //reset color of last selected item
                        //unhighlight(currentSelectedView, parent);
                    //}

                    selectItem(position);
                }
            //}
        });

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadAndDisplayInterventions(new Runnable() {
                    @Override
                    public void run() {
                        InterventionListFragment.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pullToRefresh.setRefreshing(false);
                            }
                        });
                    }
                });
            }
        });

        loadAndDisplayInterventions(null);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            fragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentInteractionListener = null;
    }

    public interface OnFragmentInteractionListener {

        // When button creation intervention clicked
        void handleInterventionCreation();

        // when an interventions is selected
        void handleInterventionSelected(Intervention intervention);
    }

    private void displayCreationBt() {
        interventionCreationBt.setEnabled(isCodis());
    }

    private boolean isCodis(){
        Credentials credentials = ((TdfApplication)this.getActivity().getApplication()).loadCredentials();
        return credentials.isCodisUser();
    }

    public void loadAndDisplayInterventions(final Runnable onLoaded){
        interventions.clear();
        interventionArrayList.clear();

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
                    interventionArrayList.add(intervention);
                }

                sortInterventions();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        interventionsAdapter.notifyDataSetChanged();
                    }
                });

                // Run callback
                if (onLoaded != null) {
                    onLoaded.run();
                }
            }

            @Override
            public void onRepositoryFailure(Throwable e) {
            }

            @Override
            public void onRestFailure(Throwable e) {
                // TODO : display toast error
                Log.e("TAG","Rest error when loading interventions");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(InterventionListFragment.this.getActivity(),
                                "Network error when loading interventions", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void sortInterventions(){
        ArrayList<Intervention> interventionArrayListNotArchived = new ArrayList<>();
        ArrayList<Intervention> interventionArrayListArchived = new ArrayList<>();

        Iterator<Intervention> it = interventionArrayList.iterator();
        Intervention intervention;
        while(it.hasNext())
        {
            intervention = it.next();
            if(intervention.isArchived()){
                interventionArrayListArchived.add(intervention);
            }
            else {
                interventionArrayListNotArchived.add(intervention);
            }
        }

        Comparator<Intervention> interventionComparator = new Comparator<Intervention>() {
            @Override
            public int compare(Intervention lhs, Intervention rhs) {
                //compare date
                Date date1 = lhs.getCreationDate();
                Date date2 = rhs.getCreationDate();

                return date2.compareTo(date1);
            }
        };

        Collections.sort(interventionArrayListNotArchived, interventionComparator);
        Collections.sort(interventionArrayListArchived, interventionComparator);

        //put in interventionArrayList ordered interventions
        interventionArrayList.clear();
        interventionArrayList.addAll(interventionArrayListNotArchived);
        interventionArrayList.addAll(interventionArrayListArchived);

        //add first interventions not archived
        it = interventionArrayListNotArchived.iterator();
        while(it.hasNext())
        {
            intervention = it.next();
            // TODO: 28/04/16 better address
            interventions.add(intervention.getName() + "\n" + intervention.getLocation().getAddress());
        }

        //then add interventions archived
        it = interventionArrayListArchived.iterator();
        while(it.hasNext())
        {
            intervention = it.next();
            // TODO: 28/04/16 better address
            interventions.add(intervention.getName() + "\n" + intervention.getLocation().getAddress());
        }
    }

    private void selectItem(int i){
        // TODO: 28/04/16 bug, when call on create, doesn't select the first intervention 
        fragmentInteractionListener.handleInterventionSelected(interventionArrayList.get(i));
    }
}
