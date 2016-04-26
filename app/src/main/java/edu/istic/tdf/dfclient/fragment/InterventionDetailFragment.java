package edu.istic.tdf.dfclient.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
                mListener.onInterventionSelect();
            }
        });
        
        if(currentIntervention != null) {
            // TODO: 26/04/16 get the intervention name
            currentIntervention.setName("Test");
            
            
            interventionName.setText(currentIntervention.getName());

            // TODO: 26/04/16  
            interventionAddress.setText("adresse de test");
            
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
        void onInterventionSelect();
    }
}
