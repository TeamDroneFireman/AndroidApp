package edu.istic.tdf.dfclient.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;

public class InterventionDetailFragment extends Fragment {

    // UI
    @Bind(R.id.interventionSelectionButton)
    Button interventionSelectionBt;

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

    public InterventionDetailFragment() {
        // Required empty public constructor
    }

    public static InterventionDetailFragment newInstance() {
        InterventionDetailFragment fragment = new InterventionDetailFragment();
        return fragment;
    }

    public static InterventionDetailFragment newInstance(Intervention intervention) {
        // TODO: 26/04/16
        //currentIntervention = intervention;
        currentIntervention = new Intervention();
        InterventionDetailFragment fragment = new InterventionDetailFragment();
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

        interventionSelectionBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onInterventionSelect(currentIntervention);
            }
        });
        
        if(currentIntervention != null) {
            // TODO: 26/04/16 name
            currentIntervention.setName("SAP_Interventon");

            interventionName.setText(currentIntervention.getName());

            // TODO: 26/04/16  address
            interventionAddress.setText("50 rue du crapeau, 66 666 le Marais");

            //// TODO: 27/04/16 date
            interventionDate.setText(new Date().toString());
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
        void onInterventionSelect(Intervention intervention);
    }
}
