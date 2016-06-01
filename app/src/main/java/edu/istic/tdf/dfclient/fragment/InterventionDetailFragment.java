package edu.istic.tdf.dfclient.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.TdfApplication;
import edu.istic.tdf.dfclient.activity.MainMenuActivity;
import edu.istic.tdf.dfclient.dao.domain.InterventionDao;
import edu.istic.tdf.dfclient.dao.handler.IDaoWriteReturnHandler;
import edu.istic.tdf.dfclient.domain.element.Element;
import edu.istic.tdf.dfclient.domain.element.mean.IMean;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;

public class InterventionDetailFragment extends Fragment {

    // UI
    @Bind(R.id.interventionSelectionButton) Button selectionBt;
    @Bind(R.id.interventionArchiveButton) Button archiveBt;
    @Bind(R.id.intervention_name)TextView interventionName;
    @Bind(R.id.intervention_address) TextView interventionAddress;
    @Bind(R.id.intervention_date) TextView interventionDate;
    @Bind(R.id.validation_table)ListView validationTable;

    // Data
    InterventionDao interventionDao;
    private static Intervention intervention;

    // Fragments
    private OnFragmentInteractionListener fragmentInteractionListener;
    private List<IMean> meanList;
    private boolean isCodis;

    public static InterventionDetailFragment newInstance(InterventionDao interventionDao) {
        InterventionDetailFragment fragment = new InterventionDetailFragment();
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
        this.isCodis = ((TdfApplication)this.getActivity().getApplication()).loadCredentials().isCodisUser();
        // View
        View view = inflater.inflate(R.layout.fragment_intervention_detail, container, false);
        ButterKnife.bind(this, view);

        // Events
        selectionBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentInteractionListener.onInterventionSelected(intervention);
            }
        });

        archiveBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                archiveCurrentIntervention();
            }
        });

        // View binding
        displayIntervention();

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

    public void setMeanList(List<IMean> meanList) {
        this.meanList=meanList;
    }

    public List<String> getMeansNames() {
        List<String> list=new ArrayList<>();
        for(int i=0;i<meanList.size();i++){
            list.add(meanList.get(i).getName());
        }
        return list;
    }

    public interface OnFragmentInteractionListener {
        void onInterventionSelected(Intervention intervention);
        void onInterventionArchived();

        void handleValidation(IMean mean);
    }

    public void setIntervention(Intervention intervention){
        InterventionDetailFragment.intervention = intervention;
    }

    /**
     * Gets intervention data to display it in view
     */
    public void displayIntervention() {
        if(intervention != null) {

            // UI
            interventionName.setText(intervention.getName());
            interventionAddress.setText(intervention.getLocation().getAddress());
            Date date = intervention.getCreationDate();
            // TODO : Get format from String resource
            String strDate = new SimpleDateFormat(
                    "yyyy-MM-dd'-'HH:mm:ss"
                    , Locale.FRANCE)
                    .format(date);
            interventionDate.setText(strDate);

            if(meanList!=null && !isCodis && !intervention.isArchived()){
                addMeansInValidationTable();
            }


            // archived button
            if(intervention.isArchived()) {
                archiveBt.setText(R.string.intervention_detail_unarchive);
            } else {
                archiveBt.setText(R.string.intervention_detail_archive);
            }

            // Listeners
            archiveBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleArchiveIntervention();
                }
            });
        }
    }

    private void addMeansInValidationTable() {
        List<String> names=getMeansNames();



    }

    private void archiveCurrentIntervention(){
        if (intervention != null){
            intervention.setArchived(!intervention.isArchived());
            ((MainMenuActivity) InterventionDetailFragment.this.getActivity()).showProgress();

            
            interventionDao.persist(intervention, new IDaoWriteReturnHandler<Intervention>() {
                @Override
                public void onSuccess(Intervention intervention) {
                    ((MainMenuActivity) InterventionDetailFragment.this.getActivity()).hideProgress();
                    displayIntervention();
                    fragmentInteractionListener.onInterventionArchived();
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
    }

    /**
     * Archives or unarchives an intervention
     */
    private void toggleArchiveIntervention(){
        if(intervention != null) {
            intervention.setArchived(!intervention.isArchived());
            interventionDao.persist(intervention, new IDaoWriteReturnHandler<Intervention>() {
                @Override
                public void onSuccess(Intervention intervention) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            displayIntervention();
                            fragmentInteractionListener.onInterventionArchived();
                        }
                    });
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

    }
}
