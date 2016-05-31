package edu.istic.tdf.dfclient.fragment;

import android.content.Context;
import android.graphics.Path;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.UI.adapter.RoleArrayAdapter;
import edu.istic.tdf.dfclient.UI.adapter.ShapeArrayAdapter;
import edu.istic.tdf.dfclient.domain.element.Element;
import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.domain.element.mean.IMean;
import edu.istic.tdf.dfclient.domain.element.mean.MeanState;
import edu.istic.tdf.dfclient.domain.element.mean.drone.Drone;
import edu.istic.tdf.dfclient.domain.element.mean.drone.mission.Mission;
import edu.istic.tdf.dfclient.domain.geo.GeoPoint;
import edu.istic.tdf.dfclient.drawable.PictoFactory;

public class ContextualDrawerFragment extends Fragment implements Observer {

    private OnFragmentInteractionListener mListener;

    private RoleArrayAdapter roleArrayAdapter;
    private ShapeArrayAdapter shapeArrayAdapter;

    @Bind(R.id.ElementLabelEdit)
    EditText ElementLabelEdit;

    @Bind(R.id.ElementSubmitButton)
    Button elementSubmitButton;

    @Bind(R.id.ElementCancelButton)
    Button elementCancelButton;

    @Bind(R.id.ElementDeleteButton)
    Button elementDeleteButton;

    @Bind(R.id.DroneCreatePathButton)
    Button droneCreatePathButton;

    @Bind(R.id.DroneStartMission)
    Button droneStartMission;

    @Bind(R.id.DronePathModeSpinner)
    Spinner dronePathModeSpinner;

    @Bind(R.id.RoleSpinner)
    Spinner roleSpinner;

    @Bind(R.id.FormSpinner)
    Spinner formSpinner;

    @Bind(R.id.ArrivedState)
    CheckBox arrivedStateCheckBox;
    @Bind(R.id.EngagedState)
    CheckBox engagedStateCheckBox;
    @Bind(R.id.InTransitState)
    CheckBox inTransit;
    @Bind(R.id.StateView)
    TextView stateTextView;

    private View view;
    private Element element;
    private boolean createDronePathMode = false;
    private boolean elementToNextState = false;

    public ContextualDrawerFragment() {
        // Required empty public constructor
    }

    public static ContextualDrawerFragment newInstance() {
        ContextualDrawerFragment fragment = new ContextualDrawerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        roleArrayAdapter = new RoleArrayAdapter(getContext(), Role.values());

        view = inflater.inflate(R.layout.fragment_contextual_drawer, container, false);

        ButterKnife.bind(this, view);

        elementSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                element.setName(ElementLabelEdit.getText().toString());
                element.setRole((Role) roleSpinner.getSelectedItem());
                element.setForm((PictoFactory.ElementForm) formSpinner.getSelectedItem());

                mListener.updateElement(element);
            }
        });

        elementCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDronePathMode = false;
                mListener.setCreateDronePathMode(false);
                mListener.cancelUpdate();
            }
        });

        elementDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.deleteElement(element);
            }
        });

        droneStartMission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((Drone)element).hasMission()){
                    mListener.startMission((Drone)element);
                }
            }
        });

        droneCreatePathButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (createDronePathMode) {

                    dronePathModeSpinner.setVisibility(View.GONE);
                    droneCreatePathButton.setText("Créer chemin");

                    Mission currentMission = mListener.getCurrentMission();

                    if(currentMission != null){

                        ArrayList<GeoPoint> pathPoints = mListener.getCurrentMission().getPathPoints();
                        Mission.PathMode pathMode = (Mission.PathMode) dronePathModeSpinner.getSelectedItem();

                        switch (pathMode){
                            case SIMPLE:
                            case CYCLE:
                                pathPoints.add(pathPoints.get(0));
                                break;
                            case ZONE:
                                break;
                        }

                        ((Drone) element).setMission(
                                new Mission(
                                        pathPoints,
                                        (Mission.PathMode)dronePathModeSpinner.getSelectedItem()
                                )
                        );
                    }

                    mListener.setCreateDronePathMode(false);
                } else {
                    dronePathModeSpinner.setVisibility(View.VISIBLE);
                    droneCreatePathButton.setText("Confirmer chemin");
                    mListener.setCreateDronePathMode(true);
                }
                createDronePathMode = !createDronePathMode;
            }
        });

        roleSpinner.setAdapter(roleArrayAdapter);
        roleArrayAdapter.notifyDataSetChanged();
        formSpinner.setAdapter(new ShapeArrayAdapter(getContext(), PictoFactory.ElementForm.values()));
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

    @Override
    public void update(Observable observable, Object data) {

    }

    /**
     * Set the element which can be modified by the drawer
     * @param element
     */
    public void setSelectedElement(final Element element) {
        this.element = element;
        ElementLabelEdit.setText(element.getName());

        //If it is a mean, can't change the name
        ElementLabelEdit.setEnabled(!element.isMeanFromMeanTable());

        roleSpinner.setSelection(Arrays.asList(Role.values()).indexOf(element.getRole()));
        roleArrayAdapter.notifyDataSetChanged();
        PictoFactory.ElementForm[] forms = PictoFactory.ElementForm.values();
        final MeanState[] states = MeanState.values();
        droneCreatePathButton.setVisibility(View.GONE);
        dronePathModeSpinner.setVisibility(View.GONE);

        MeanState meanState;
        switch (element.getType()){
            case MEAN:
                meanState = ((IMean) element).getState();
                if(meanState.equals(MeanState.ENGAGED)){
                    forms = new PictoFactory.ElementForm[]{
                                PictoFactory.ElementForm.MEAN,
                                PictoFactory.ElementForm.MEAN_GROUP,
                                PictoFactory.ElementForm.MEAN_COLUMN
                            };
                }else{
                    forms = new PictoFactory.ElementForm[]{
                                PictoFactory.ElementForm.MEAN_PLANNED,
                                PictoFactory.ElementForm.MEAN_GROUP_PLANNED,
                                PictoFactory.ElementForm.MEAN_COLUMN_PLANNED
                             };
                }
                 break;
            case AIRMEAN:
                meanState = ((IMean) element).getState();
                droneCreatePathButton.setVisibility(View.VISIBLE);
                if(meanState.equals(MeanState.ENGAGED)){
                    forms = new PictoFactory.ElementForm[]{
                            PictoFactory.ElementForm.AIRMEAN};
                }else{
                    forms = new PictoFactory.ElementForm[]{
                            PictoFactory.ElementForm.AIRMEAN_PLANNED};
                }
                break;
            case MEAN_OTHER:
                meanState = ((IMean) element).getState();
                if(meanState.equals(MeanState.ENGAGED)){
                    forms = new PictoFactory.ElementForm[]{
                            PictoFactory.ElementForm.MEAN_OTHER};
                }else{
                    forms = new PictoFactory.ElementForm[]{
                            PictoFactory.ElementForm.MEAN_OTHER_PLANNED};
                }
                break;

            default:
                    forms = new PictoFactory.ElementForm[]{
                            PictoFactory.ElementForm.SOURCE,
                            PictoFactory.ElementForm.TARGET,
                            PictoFactory.ElementForm.WATERPOINT,
                            PictoFactory.ElementForm.WATERPOINT_SUPPLY,
                            PictoFactory.ElementForm.WATERPOINT_SUSTAINABLE};


        }
        shapeArrayAdapter = new ShapeArrayAdapter(getContext(), forms);
        dronePathModeSpinner.setAdapter(new ArrayAdapter<Mission.PathMode>(getContext(), android.R.layout.simple_spinner_item, Mission.PathMode.values()));

        formSpinner.setAdapter(shapeArrayAdapter);
        formSpinner.setSelection(Arrays.asList(forms).indexOf(element.getForm()));
        shapeArrayAdapter.notifyDataSetChanged();
        initializeStateSelection();
        //if element has an id we can suppress it
        if(element.getId() != null)
        {
            switch (element.getType()) {
                //if it's a mean, we don't suppress we juste release, so we change the button label
                case AIRMEAN:
                    elementDeleteButton.setText(R.string.supprimer_button_for_mean);
                    break;
                case MEAN:
                    elementDeleteButton.setText(R.string.supprimer_button_for_mean);
                    break;
                default:
                    elementDeleteButton.setText(R.string.supprimer_button_for_defaut);
            }

            elementDeleteButton.setVisibility(View.VISIBLE);
            elementDeleteButton.setEnabled(true);
        }
        else
        {
            elementDeleteButton.setVisibility(View.GONE);
            elementDeleteButton.setEnabled(false);
        }
    }


    /**
     *  Method to help initializeStateSelection below
      */
    private void setCheckBoxProperties(CheckBox button, boolean enable, boolean checked){
        button.setChecked(checked);
        button.setEnabled(enable);
        if(enable){
            button.setVisibility(View.VISIBLE);
        }else{
            button.setVisibility(View.GONE);
        }
    }

    /**
     * Method to update the state with the checkbox and the actual states
     */
    private void updateState(){
        MeanState meanState = ((IMean) element).getState();
        if( meanState.equals(MeanState.ARRIVED)||
                meanState.equals(MeanState.ENGAGED)||
                meanState.equals(MeanState.VALIDATED)||
                meanState.equals(MeanState.INTRANSIT)){
            if (inTransit.isChecked()) {
                ((IMean) element).setState(MeanState.INTRANSIT);
            }else if (engagedStateCheckBox.isChecked()) {
                ((IMean) element).setState(MeanState.ENGAGED);
            }else{
                ((IMean) element).setState(MeanState.ARRIVED);
            }
        }
    }
    private void initializeStateSelection() {
        inTransit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateState();
                setSelectedElement(element);
            }
        });
        engagedStateCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateState();
                setSelectedElement(element);
            }
        });
        arrivedStateCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateState();
                setSelectedElement(element);
            }
        });
        switch(element.getType()){
            case MEAN_OTHER:
            case MEAN:
            case AIRMEAN:
                MeanState m = ((IMean) element).getState();
                stateTextView.setText(m.getMeanAsReadableText());
                switch (m){
                    case ASKED:
                        setCheckBoxProperties(arrivedStateCheckBox, false, false);
                        setCheckBoxProperties(engagedStateCheckBox, false, false);
                        setCheckBoxProperties(inTransit, false, false);
                        break;
                    case VALIDATED:
                        setCheckBoxProperties(arrivedStateCheckBox, true, false);
                        setCheckBoxProperties(engagedStateCheckBox, true, false);
                        setCheckBoxProperties(inTransit, true, false);
                        break;
                    case ARRIVED:
                        setCheckBoxProperties(arrivedStateCheckBox, false, false);
                        setCheckBoxProperties(engagedStateCheckBox, true, false);
                        setCheckBoxProperties(inTransit, true, false);
                        break;
                    case ENGAGED:
                        setCheckBoxProperties(arrivedStateCheckBox, false, false);
                        setCheckBoxProperties(engagedStateCheckBox, true, true);
                        setCheckBoxProperties(inTransit, true, false);
                        break;
                    case INTRANSIT:
                        setCheckBoxProperties(arrivedStateCheckBox, false, false);
                        setCheckBoxProperties(engagedStateCheckBox, true, true);
                        setCheckBoxProperties(inTransit, true, true);
                        break;
                    case RELEASED:
                        setCheckBoxProperties(arrivedStateCheckBox, false, false);
                        setCheckBoxProperties(engagedStateCheckBox, false, false);
                        setCheckBoxProperties(inTransit, false, false);
                        break;
                }
                break;
            case POINT_OF_INTEREST:
            case WATERPOINT:
            default:
                stateTextView.setVisibility(View.GONE);
                arrivedStateCheckBox.setVisibility(View.GONE);
                engagedStateCheckBox.setVisibility(View.GONE);
                inTransit.setVisibility(View.GONE);
                break;
        }


    }

    public interface OnFragmentInteractionListener {
        void updateElement(Element element);
        Mission getCurrentMission();
        void setCreateDronePathMode(boolean isDronePathMode);
        void cancelUpdate();
        void deleteElement(Element element);
        void startMission(Drone drone);
    }

    /**
     *
     * @return the current element if it exist, or null if no current element
     */
    public Element tryGetElement()
    {
        return this.element;
    }
}
