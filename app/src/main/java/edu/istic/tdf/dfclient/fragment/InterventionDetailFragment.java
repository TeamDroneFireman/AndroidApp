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
    @Bind(R.id.interventionSelectionButton)
    Button interventionSelectionBt;
    // UI
    @Bind(R.id.interventionArchiveButton)
    Button interventionArchiveBt;

    // UI
    @Bind(R.id.intervention_name)
    TextView interventionName;

    // UI
    @Bind(R.id.intervention_address)
    TextView interventionAddress;

    // UI
    @Bind(R.id.intervention_date)
    TextView interventionDate;

    private OnFragmentInteractionListener mListener;

    private static Intervention currentIntervention;

    InterventionDao interventionDao;

    public InterventionDetailFragment() {
        // Required empty public constructor
    }

    public static InterventionDetailFragment newInstance() {
        InterventionDetailFragment fragment = new InterventionDetailFragment();
        return fragment;
    }

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

        View view = inflater.inflate(R.layout.fragment_intervention_detail, container, false);
        ButterKnife.bind(this, view);

        // Events
        interventionSelectionBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onInterventionSelection(currentIntervention);
            }
        });

        // View binding
        if(currentIntervention != null) {

            // TODO: 27/04/16 remove name ? and xml
            // name
            interventionName.setText(currentIntervention.getName());

            // TODO: 27/04/16 add sinisterCode ? 

            // TODO: 27/04/16 remove address ? and xml
            // address
            interventionAddress.setText(currentIntervention.getLocation().getAddress());

            interventionArchiveBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    archiveCurrentIntervention();
                }
            });
        }

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
        void onInterventionSelection(Intervention intervention);

        void onInterventionArchived();
    }

    private void archiveCurrentIntervention(){
        currentIntervention.setArchived(!currentIntervention.isArchived());
        interventionDao.persist(currentIntervention, new IDaoWriteReturnHandler() {
            @Override
            public void onSuccess() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mListener.onInterventionArchived();
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

    public void setCurrentIntervention(Intervention intervention){
        currentIntervention = intervention;
        loadInfos();
    }

    // TODO: 28/04/16 from creation then select an item, date bug 
    // TODO: 28/04/16 on creation the first element as to be selected but it bug 
    private void loadInfos() {
        if(currentIntervention != null) {

            // TODO: 27/04/16 remove name ? and xml
            // name
            interventionName.setText(currentIntervention.getName());

            // TODO: 27/04/16 add sinisterCode ?

            // TODO: 27/04/16 remove address ? and xml
            // address
            interventionAddress.setText(currentIntervention.getLocation().getAddress());

            // date creation
            Date date = currentIntervention.getCreationDate();
            String strDate = new SimpleDateFormat("yyyy-MM-dd'-'HH:mm:ss", Locale.FRANCE).format(date);
            interventionDate.setText(strDate);

            // TODO: 27/04/16 add map ?

            // archived button
            if(currentIntervention.isArchived()) {
                interventionArchiveBt.setText("Désarchiver");
            } else {
                interventionArchiveBt.setText(" Archiver ");
            }
        }
    }
}
