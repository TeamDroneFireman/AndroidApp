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
import edu.istic.tdf.dfclient.domain.geo.Location;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;
import edu.istic.tdf.dfclient.domain.intervention.SinisterCode;

public class InterventionListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    @Bind(R.id.interventionCreationButton)
    Button interventionCreationBt;

    @Bind(R.id.interventions_list)
    ListView interventionsList;

    @Bind(R.id.pull_to_refresh_interventions)
    SwipeRefreshLayout pullToRefresh;

    InterventionDao interventionDao;

    // for listView intervention
    private ArrayList<String> interventions = new ArrayList<>();
    private ArrayAdapter<String> interventionsAdapter;

    // the collection of all object interventions
    ArrayList<Intervention> interventionArrayList = new ArrayList<>();

    private TextView currentSelectedView;

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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
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
        interventionsAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1,
                interventions);

        interventionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!view.equals(currentSelectedView)) {
                    if (currentSelectedView != null) {
                        //reset color of last selected item
                        unhighlight(currentSelectedView,parent);
                    }

                    selectItem(position, (TextView) view);
                }
            }
        });

        interventionsList.setAdapter(interventionsAdapter);

        // Pull to refresh
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadInterventions(new Runnable() {
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

        // TODO : Runnable that selects the first item when loaded ?  -> it's done biatch
        loadInterventions(null);

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

        // when an interventions is selected
        void handleInterventionSelected(Intervention intervention);
    }

    private boolean isCodis(){
        Credentials credentials = ((TdfApplication)this.getActivity().getApplication()).loadCredentials();
        return credentials.isCodisUser();
    }

    public void loadInterventions(final Runnable onLoaded){
        interventions.clear();
        interventionArrayList.clear();

        // TODO: 27/04/16 bouchon à enlever
        Intervention interventionBouchon = new Intervention();
        interventionBouchon.setName("Bouchon");
        interventionBouchon.setCreationDate(new Date());
        interventionBouchon.setSinisterCode(SinisterCode.FDF);
        Location location = new Location();
        location.setAddress("12 rue de papouille, 777 BisounoursLand");
        interventionBouchon.setLocation(location);
        interventionBouchon.setArchived(true);
        interventionArrayList.add(interventionBouchon);

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

                addSortedInterventions();

                // Run callback
                if(onLoaded != null) {
                    onLoaded.run();
                }
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
    }

    private void addSortedInterventions(){
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

        //add first interventions not archived
        it = interventionArrayListNotArchived.iterator();
        while(it.hasNext())
        {
            intervention = it.next();
            interventions.add(intervention.getName() + "\n" + intervention.getLocation().getAddress());
        }

        //then add interventions archived
        it = interventionArrayListArchived.iterator();
        while(it.hasNext())
        {
            intervention = it.next();
            interventions.add(intervention.getName() + "\n" + intervention.getLocation().getAddress());
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                interventionsAdapter.notifyDataSetChanged();
            }
        });

        //select the first intervention
        if(interventionsList.getCount() > 0 && currentSelectedView == null)
        {
            selectFirstItem();
        }
    }

    private void selectFirstItem(){

        int firstListItemPosition = interventionsList.getFirstVisiblePosition();

        // TODO: 27/04/16 chopper la view de l'item qui correspond a firstListItemPosition
        int wantedPosition = firstListItemPosition;
        int firstPosition = interventionsList.getFirstVisiblePosition() - interventionsList.getHeaderViewsCount();
        int wantedChild = wantedPosition - firstPosition;
        TextView view = (TextView)interventionsAdapter.getView(wantedChild,null,interventionsList);

        /*final int lastListItemPosition = firstListItemPosition + interventionsList.getChildCount() - 1;

        if (wantedPosition < firstListItemPosition || wantedPosition > lastListItemPosition ) {
            view = (TextView)interventionsList.getAdapter().getView(wantedPosition, null, interventionsList);
        } else {
            final int childIndex = wantedPosition - firstListItemPosition;
            view = (TextView)interventionsList.getChildAt(childIndex);
        }*/

        selectItem(firstListItemPosition, view);
    }

    private void selectItem(int i, TextView view){
        mListener.handleInterventionSelected(interventionArrayList.get(i));
        currentSelectedView = view;

        //color the selected item
        highlight(view);
    }

    private void highlight(TextView view) {
        // TODO: 27/04/16 color ?
        view.setBackgroundColor(Color.parseColor("#212121"));
        // TODO: 28/04/16 typeface ?
        //view.setTypeface(Typeface.DEFAULT_BOLD);
    }

    private void unhighlight(TextView view, AdapterView adapterView) {
        view.setBackgroundColor(adapterView.getSolidColor());
        // TODO: 28/04/16 typeface ?
        //view.setTypeface(Typeface.DEFAULT);
    }
}
