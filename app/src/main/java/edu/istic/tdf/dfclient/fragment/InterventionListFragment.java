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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.TdfApplication;
import edu.istic.tdf.dfclient.UI.adapter.InterventionListAdapter;
import edu.istic.tdf.dfclient.activity.MainMenuActivity;
import edu.istic.tdf.dfclient.dao.DaoSelectionParameters;
import edu.istic.tdf.dfclient.dao.domain.InterventionDao;
import edu.istic.tdf.dfclient.dao.domain.element.DroneDao;
import edu.istic.tdf.dfclient.dao.domain.element.InterventionMeanDao;
import edu.istic.tdf.dfclient.dao.handler.IDaoSelectReturnHandler;
import edu.istic.tdf.dfclient.domain.element.Element;
import edu.istic.tdf.dfclient.domain.element.ElementType;
import edu.istic.tdf.dfclient.domain.element.IElement;
import edu.istic.tdf.dfclient.domain.element.mean.IMean;
import edu.istic.tdf.dfclient.domain.element.mean.MeanState;
import edu.istic.tdf.dfclient.domain.element.mean.drone.Drone;
import edu.istic.tdf.dfclient.domain.element.mean.interventionMean.InterventionMean;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;
import su.levenetc.android.badgeview.BadgeView;


public class InterventionListFragment extends Fragment {


    // UI
    @Bind(R.id.interventionCreationButton) Button interventionCreationBt;
    @Bind(R.id.interventions_list) ListView interventionsList;
    @Bind(R.id.pull_to_refresh_interventions) SwipeRefreshLayout pullToRefresh;

    // Data
    InterventionDao interventionDao;
    private ArrayList<String> interventions = new ArrayList<>();    // for listView intervention
    private InterventionListAdapter interventionsAdapter;
    ArrayList<Intervention> interventionArrayList = new ArrayList<>();    // the collection of all object interventions
    private int countNotArchived = 0;
    // Fragment listener
    private OnFragmentInteractionListener fragmentInteractionListener;

    private boolean isCodis;
    private DroneDao droneDao;
    private InterventionMeanDao interventionMeanDao;


    /**
     * List of mean to validate for CODIS
     */
    private HashMap<String,List<IMean>> askedMeansList =new HashMap<>();


    public static InterventionListFragment newInstance(InterventionDao interventionDao,DroneDao droneD,InterventionMeanDao interventionMeanDao) {
        InterventionListFragment fragment = new InterventionListFragment();
        fragment.interventionDao = interventionDao;
        fragment.droneDao=droneD;
        fragment.interventionMeanDao=interventionMeanDao;
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.isCodis = ((TdfApplication)this.getActivity().getApplication()).loadCredentials().isCodisUser();

        View view = inflater.inflate(R.layout.fragment_intervention_list, container, false);
        ButterKnife.bind(this, view);// Inflate the layout for this fragment

        // Toggle creation button
        displayCreationBt();

//Data
        interventionsAdapter = new InterventionListAdapter(getActivity(),inflater,
                interventions,isCodis);

        interventionsList.setAdapter(interventionsAdapter);

        interventionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                selectItem(position);
            }

        });

        // Events
        interventionCreationBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentInteractionListener.handleInterventionCreation();
            }
        });

        loadAndDisplayInterventions(null);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadAndDisplayInterventions(new Runnable() {
                    @Override
                    public void run() {
                        InterventionListFragment.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                fragmentInteractionListener.welcomeToShow();
                                pullToRefresh.setRefreshing(false);
                            }
                        });
                    }
                });
            }
        });

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
        void handleInterventionSelected(Intervention intervention, List<IMean> meanList);

        void welcomeToShow();
    }

    private void displayCreationBt() {
        interventionCreationBt.setEnabled(this.isCodis);
    }

    public void loadAndDisplayInterventions(final Runnable onLoaded){
        ((MainMenuActivity) InterventionListFragment.this.getActivity()).showProgress();
        interventions.clear();
        interventionArrayList.clear();
        askedMeansList.clear();

        interventionDao.findAllWithAskedElement(new DaoSelectionParameters(), new IDaoSelectReturnHandler<List<Intervention>>() {

            @Override
            public void onRepositoryResult(List<Intervention> r) {

            }

            @Override
            public void onRestResult(List<Intervention> interventionList) {
                Iterator<Intervention> interventionIterator = interventionList.iterator();
                Intervention intervention;

                while (interventionIterator.hasNext()) {
                    intervention = interventionIterator.next();
                    interventionArrayList.add(intervention);

                    addMeanInHashMap(intervention,intervention.getDrones());
                    addMeanInHashMap(intervention, intervention.getMeans());
                }

                sortInterventions();

                interventionsAdapter.setHashMap(askedMeansList);
                interventionsAdapter.setArrayList(interventionArrayList);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MainMenuActivity) InterventionListFragment.this.getActivity()).hideProgress();

                        interventionsAdapter.notifyDataSetChanged();
                    }
                });

                // Run callback
                if (onLoaded != null) {
                    onLoaded.run();
                }

                if(interventionsList.isSelected()){
                    selectItem(interventionsList.getSelectedItemPosition());
                }
            }

            @Override
            public void onRepositoryFailure(Throwable e) {

            }

            @Override
            public void onRestFailure(Throwable e) {
                Log.e("TAG", "Rest error when loading interventions");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MainMenuActivity) InterventionListFragment.this.getActivity()).hideProgress();
                        /*Toast.makeText(InterventionListFragment.this.getActivity(),
                                "Network error when loading interventions", Toast.LENGTH_SHORT).show();*/
                    }
                });
            }
        });

    }


    private void addMeanInHashMap(Intervention intervention, Collection<? extends IMean> r) {
        String idI=intervention.getId();
        List<IMean> list=new ArrayList<>();
        if(askedMeansList.containsKey(idI)){
            list=askedMeansList.get(idI);
        }
        list.addAll(r);
        askedMeansList.put(idI, list);
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

        interventionsAdapter.setCountNotArchived(interventionArrayListNotArchived.size());
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
        Intervention intervention=interventionArrayList.get(i);
        List<IMean> meanList=askedMeansList.get(intervention.getId());
        fragmentInteractionListener.handleInterventionSelected(intervention,meanList);
    }
}
