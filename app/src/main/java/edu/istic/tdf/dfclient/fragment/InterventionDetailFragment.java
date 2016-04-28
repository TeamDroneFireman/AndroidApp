package edu.istic.tdf.dfclient.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.dao.domain.InterventionDao;
import edu.istic.tdf.dfclient.dao.handler.IDaoWriteReturnHandler;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;

public class InterventionDetailFragment extends Fragment {

    // UI
    @Bind(R.id.interventionSelectionButton) Button selectionBt;
    @Bind(R.id.interventionArchiveButton) Button archiveBt;
    @Bind(R.id.intervention_name)TextView interventionName;
    @Bind(R.id.intervention_address) TextView interventionAddress;
    @Bind(R.id.intervention_date) TextView interventionDate;

    // Data
    InterventionDao interventionDao;
    private static Intervention intervention;

    // Fragments
    private OnFragmentInteractionListener fragmentInteractionListener;

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

    public interface OnFragmentInteractionListener {
        void onInterventionSelected(Intervention intervention);
        void onInterventionArchived();
    }

    public void setIntervention(Intervention intervention){
        InterventionDetailFragment.intervention = intervention;
    }

    // TODO: 28/04/16 from creation then select an item, date bug
    // TODO: 28/04/16 on creation the first element as to be selected but it bug

    /**
     * Gets intervention data to display it in view
     */
    public void displayIntervention() {
        if(intervention != null) {

            // UI
            interventionName.setText(intervention.getName());
            interventionAddress.setText(intervention.getLocation().getAddress());
            Date date = intervention.getCreationDate();
            String strDate = new SimpleDateFormat(
                    InterventionDetailFragment.this.getContext().getString(R.string.intervention_detail_dateformat)
                    , Locale.FRANCE)
                    .format(date);
            interventionDate.setText(strDate);

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


    /*protected void displayIntervention(){
        if(intervention != null) {

            // UI
            // TODO: 27/04/16 remove name ? and xml
            // name
            interventionName.setText(intervention.getName());

            // TODO: 27/04/16 add sinisterCode ?

            // TODO: 27/04/16 remove address ? and xml
            // address
            interventionAddress.setText(intervention.getLocation().getAddress());

            // Listeners
            archiveBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleArchiveIntervention();
                }
            });
        }
    }*/

    /**
     * Archives or unarchives an intervention
     */
    private void toggleArchiveIntervention(){
        if(intervention != null) {
            intervention.setArchived(!intervention.isArchived());
            interventionDao.persist(intervention, new IDaoWriteReturnHandler() {
                @Override
                public void onSuccess() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
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
