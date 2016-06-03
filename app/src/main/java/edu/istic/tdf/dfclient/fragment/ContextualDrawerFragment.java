package edu.istic.tdf.dfclient.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.UI.adapter.RoleArrayAdapter;
import edu.istic.tdf.dfclient.UI.adapter.ShapeArrayAdapter;
import edu.istic.tdf.dfclient.domain.element.Element;
import edu.istic.tdf.dfclient.domain.element.ElementType;
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

    @Bind(R.id.DroneOptionsWrapper)
    LinearLayout droneOptionsWrapper;

    @Bind(R.id.PropertiesWrapper)
    LinearLayout propertiesWrapper;

    @Bind(R.id.StatesWrapper)
    LinearLayout statesWrapper;

    @Bind(R.id.ElementLabelEdit)
    EditText ElementLabelEdit;

    @Bind(R.id.ElementSubmitButton)
    Button elementSubmitButton;

    @Bind(R.id.ElementCancelButton)
    ImageButton elementCancelButton;

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

    @Bind(R.id.AskedState)
    CheckBox askedStateCheckbox;
    @Bind(R.id.ArrivedState)
    CheckBox arrivedStateCheckBox;
    @Bind(R.id.EngagedState)
    CheckBox engagedStateCheckBox;
    @Bind(R.id.InTransitState)
    CheckBox inTransitCheckBox;

    private View view;
    private Element element;

    private boolean createDronePathMode = false;

    public ContextualDrawerFragment() {}

    public static ContextualDrawerFragment newInstance() {
        ContextualDrawerFragment fragment = new ContextualDrawerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_contextual_drawer, container, false);

        ButterKnife.bind(this, view);

        roleSpinner.setAdapter(new RoleArrayAdapter(getContext(), Role.values()));

        elementSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                element.setName(ElementLabelEdit.getText().toString());
                element.setRole((Role) roleSpinner.getSelectedItem());
                element.setForm((PictoFactory.ElementForm) formSpinner.getSelectedItem());

                if (element.getType() == ElementType.MEAN || element.getType() == ElementType.AIRMEAN) {
                    updateElementStates((IMean) element);
                }

                mListener.updateElement(element);
            }
        });

        elementCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDronePathMode = false;
                mListener.setCreateDronePathMode(createDronePathMode);
                mListener.cancelUpdate();
            }
        });

        engagedStateCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fillStateCheckBox(MeanState.INTRANSIT, false, true);
                    formSpinner.setAdapter(new ShapeArrayAdapter(getContext(), getAvailableForms(element, false)));
                }
            }
        });

        inTransitCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fillStateCheckBox(MeanState.ENGAGED, false, true);
                    formSpinner.setAdapter(new ShapeArrayAdapter(getContext(), getAvailableForms(element, true)));
                }
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
                    mListener.startMission((Drone) element);
                }
            }
        });

        droneCreatePathButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (createDronePathMode) {
                    droneStartMission.setVisibility(View.GONE);
                    if (((Drone) element).hasMission()) {
                        droneStartMission.setVisibility(View.VISIBLE);
                    }

                    dronePathModeSpinner.setVisibility(View.GONE);
                    droneCreatePathButton.setText("Créer chemin");

                    Mission currentMission = mListener.getCurrentMission();

                    if (!currentMission.getPathPoints().isEmpty()) {
                        ArrayList<GeoPoint> pathPoints = mListener.getCurrentMission().getPathPoints();
                        Mission.PathMode pathMode = (Mission.PathMode) dronePathModeSpinner.getSelectedItem();

                        // If cycle or simple we close the path
                        switch (pathMode) {
                            case CYCLE:
                            case ZONE:
                                pathPoints.add(pathPoints.get(0));
                                break;
                            case SIMPLE:
                                break;
                        }

                        ((Drone) element).setMission(
                                new Mission(
                                        pathPoints,
                                        (Mission.PathMode) dronePathModeSpinner.getSelectedItem()
                                )
                        );
                    }

                    mListener.setCreateDronePathMode(false);
                } else {
                    droneStartMission.setVisibility(View.GONE);
                    dronePathModeSpinner.setVisibility(View.VISIBLE);
                    droneCreatePathButton.setText("Confirmer chemin");
                    mListener.setCreateDronePathMode(true);
                }

                createDronePathMode = !createDronePathMode;
            }
        });

        dronePathModeSpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, Mission.PathMode.values()));

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

        // Reset UI elements
        propertiesWrapper.setVisibility(View.VISIBLE);
        statesWrapper.setVisibility(View.VISIBLE);
        droneOptionsWrapper.setVisibility(View.VISIBLE);

        // Show only available options for this type of element
        switch (element.getType()){

            case MEAN:
                droneOptionsWrapper.setVisibility(View.GONE);
                break;

            case POINT_OF_INTEREST:
            case WATERPOINT:
                droneOptionsWrapper.setVisibility(View.GONE);
                statesWrapper.setVisibility(View.GONE);
                break;

        }

        // Label init
        ElementLabelEdit.setText(element.getName());
        ElementLabelEdit.setEnabled(!element.isMeanFromMeanTable());

        // Shape and role init
        roleSpinner.setSelection(Arrays.asList(Role.values()).indexOf(element.getRole()));

        boolean isPlanned = false;

        // States init
        if(element.getType() == ElementType.MEAN || element.getType() == ElementType.AIRMEAN){
            isPlanned = ((IMean)element).getState() != MeanState.ENGAGED;
            fillStateCheckboxes((IMean) element);
        }

        formSpinner.setAdapter(new ShapeArrayAdapter(getContext(), getAvailableForms(element, isPlanned)));
        /*int spinnerIndex = Arrays.asList(PictoFactory.ElementForm.values()).indexOf(element.getForm());
        if(formSpinner.getItemAtPosition(spinnerIndex) != null){
            formSpinner.setSelection(spinnerIndex);
        }*/
    }

    private void updateElementStates(IMean mean){
        switch (mean.getState()){
            case ARRIVED:
                if(engagedStateCheckBox.isChecked()){
                    mean.setState(MeanState.ENGAGED);
                }
                if(inTransitCheckBox.isChecked()){
                    mean.setState(MeanState.INTRANSIT);
                }
                break;
            case ENGAGED:
                if(inTransitCheckBox.isChecked()){
                    mean.setState(MeanState.INTRANSIT);
                }
                break;
            case INTRANSIT:
                if(engagedStateCheckBox.isChecked()){
                    mean.setState(MeanState.ENGAGED);
                }
                break;
        }
    }


    private PictoFactory.ElementForm[] getAvailableForms(Element element, boolean isPlanned){

        switch (element.getType()){

            case MEAN:

                if(isPlanned){
                    return new PictoFactory.ElementForm[]{
                            PictoFactory.ElementForm.MEAN_PLANNED,
                            PictoFactory.ElementForm.MEAN_GROUP_PLANNED,
                            PictoFactory.ElementForm.MEAN_COLUMN_PLANNED
                    };
                }

                return new PictoFactory.ElementForm[]{
                        PictoFactory.ElementForm.MEAN,
                        PictoFactory.ElementForm.MEAN_GROUP,
                        PictoFactory.ElementForm.MEAN_COLUMN
                };


            case AIRMEAN:
                if(isPlanned){

                    return new PictoFactory.ElementForm[]{
                            PictoFactory.ElementForm.AIRMEAN_PLANNED
                    };
                }
                return new PictoFactory.ElementForm[]{
                        PictoFactory.ElementForm.AIRMEAN
                };

            case MEAN_OTHER:
                return new PictoFactory.ElementForm[]{
                        PictoFactory.ElementForm.MEAN_OTHER,
                        PictoFactory.ElementForm.MEAN_OTHER_PLANNED
                };
            default:
                return new PictoFactory.ElementForm[]
                        {
                                PictoFactory.ElementForm.MEAN_OTHER,
                                PictoFactory.ElementForm.SOURCE,
                                PictoFactory.ElementForm.TARGET,
                                PictoFactory.ElementForm.WATERPOINT,
                                PictoFactory.ElementForm.WATERPOINT_SUPPLY,
                                PictoFactory.ElementForm.WATERPOINT_SUSTAINABLE
                        };
        }
    }

    private CheckBox getCheckBox(MeanState state){
        switch (state){
            case ARRIVED:
                return arrivedStateCheckBox;
            case ENGAGED:
                return engagedStateCheckBox;
            case INTRANSIT:
                return inTransitCheckBox;
        }
        return askedStateCheckbox;
    }

    private void fillStateCheckBox(MeanState state, boolean isChecked, boolean isEnabled){
        getCheckBox(state).setChecked(isChecked);
        getCheckBox(state).setEnabled(isEnabled);
    }

    private void fillStateCheckboxes(IMean mean) {

        fillStateCheckBox(MeanState.ASKED, true, false);

        switch (mean.getState()){

            case ASKED:
                fillStateCheckBox(MeanState.ARRIVED, false, false);
                fillStateCheckBox(MeanState.ENGAGED, false, false);
                fillStateCheckBox(MeanState.INTRANSIT, false, false);
                break;

            case VALIDATED:
                fillStateCheckBox(MeanState.ARRIVED, false, true);
                fillStateCheckBox(MeanState.ENGAGED, false, false);
                fillStateCheckBox(MeanState.INTRANSIT, true, false);
                break;

            case ARRIVED:
                fillStateCheckBox(MeanState.ARRIVED, true, false);
                fillStateCheckBox(MeanState.ENGAGED, false, true);
                fillStateCheckBox(MeanState.INTRANSIT, false, true);
                break;

            case ENGAGED:
                fillStateCheckBox(MeanState.ARRIVED, true, false);
                fillStateCheckBox(MeanState.ENGAGED, true, false);
                fillStateCheckBox(MeanState.INTRANSIT, false, true);
                break;

            case INTRANSIT:
                fillStateCheckBox(MeanState.ARRIVED, true, false);
                fillStateCheckBox(MeanState.ENGAGED, false, true);
                fillStateCheckBox(MeanState.INTRANSIT, true, false);
                break;

            case RELEASED:
                fillStateCheckBox(MeanState.ASKED, false, false);
                fillStateCheckBox(MeanState.ARRIVED, false, false);
                fillStateCheckBox(MeanState.ENGAGED, false, false);
                fillStateCheckBox(MeanState.INTRANSIT, false, false);
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

    public String[] getDefaultMeanList() {
        //TODO recup dans la base la liste des moyens
        return new String[]{
                "FPT",
                "VSAV",
                "EPA",
                "VLCG",
                "CCF",
                "CCGC",
                "VLHR"
        };
    }
}
