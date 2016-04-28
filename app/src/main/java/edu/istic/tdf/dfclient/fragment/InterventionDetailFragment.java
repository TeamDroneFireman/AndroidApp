package edu.istic.tdf.dfclient.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

        currentIntervention = intervention;
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

        // Events
        interventionSelectionBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onInterventionSelect(currentIntervention);
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

            // date creation
            Date date = currentIntervention.getCreationDate();
            String strDate = new SimpleDateFormat("yyyy-MM-dd'-'HH:mm:ss", Locale.FRANCE).format(date);
            interventionDate.setText(strDate);

            // TODO: 27/04/16 add map ?
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
