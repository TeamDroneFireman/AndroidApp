package edu.istic.tdf.dfclient.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.UI.Mean;
import edu.istic.tdf.dfclient.UI.adapter.RoleArrayAdapter;
import edu.istic.tdf.dfclient.UI.adapter.ShapeArrayAdapter;
import edu.istic.tdf.dfclient.UI.adapter.StateArrayAdapter;
import edu.istic.tdf.dfclient.domain.element.Element;
import edu.istic.tdf.dfclient.domain.element.ElementType;
import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.domain.element.mean.IMean;
import edu.istic.tdf.dfclient.domain.element.mean.MeanState;
import edu.istic.tdf.dfclient.domain.element.mean.drone.Drone;
import edu.istic.tdf.dfclient.domain.element.mean.drone.mission.Mission;
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

    @Bind(R.id.RoleSpinner)
    Spinner roleSpinner;

    @Bind(R.id.FormSpinner)
    Spinner formSpinner;

    @Bind(R.id.ArrivedState)
    CheckBox arrivedStateCheckBox;
    @Bind(R.id.EngagedState)
    CheckBox engagedStateCheckBox;
    @Bind(R.id.ReleasedState)
    CheckBox releasedStateCheckBox;
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
                if (releasedStateCheckBox.isChecked()) {
                    ((IMean) element).setState(MeanState.RELEASED);
                } else if (engagedStateCheckBox.isChecked()) {
                    ((IMean) element).setState(MeanState.ENGAGED);
                } else if (arrivedStateCheckBox.isChecked()) {
                    ((IMean) element).setState(MeanState.ARRIVED);
                }
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

        droneCreatePathButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (createDronePathMode) {
                    ((Drone) element).setMission(mListener.getCurrentMission());
                    mListener.getCurrentMission();
                    mListener.setCreateDronePathMode(false);
                } else {
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
     * Set the element which can be modify by the drawer
     * @param element
     */
    public void setSelectedElement(final Element element) {
        this.element = element;
        ElementLabelEdit.setText(element.getName());
        roleSpinner.setSelection(Arrays.asList(Role.values()).indexOf(element.getRole()));
        roleArrayAdapter.notifyDataSetChanged();

        PictoFactory.ElementForm[] forms = PictoFactory.ElementForm.values();
        final MeanState[] states = MeanState.values();
        droneCreatePathButton.setVisibility(View.GONE);

        switch (element.getType()){
            case MEAN:
                if(((IMean) element).getState().equals(MeanState.ASKED)){
                    forms = new PictoFactory.ElementForm[]{
                            PictoFactory.ElementForm.MEAN_PLANNED};
                }else{
                    forms = new PictoFactory.ElementForm[]{
                            PictoFactory.ElementForm.MEAN,
                            PictoFactory.ElementForm.MEAN_GROUP,
                            PictoFactory.ElementForm.MEAN_COLUMN };
                }
                 break;
            case AIRMEAN:
                droneCreatePathButton.setVisibility(View.VISIBLE);
                if(((IMean) element).getState().equals(MeanState.ASKED)){
                    forms = new PictoFactory.ElementForm[]{
                            PictoFactory.ElementForm.AIRMEAN_PLANNED};
                }else{
                    forms = new PictoFactory.ElementForm[]{
                            PictoFactory.ElementForm.AIRMEAN};
                }
                break;
            case MEAN_OTHER:
                if(((IMean) element).getState().equals(MeanState.ASKED)){
                    forms = new PictoFactory.ElementForm[]{
                            PictoFactory.ElementForm.MEAN_OTHER_PLANNED};
                }else{
                    forms = new PictoFactory.ElementForm[]{
                            PictoFactory.ElementForm.MEAN_OTHER};
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
            elementDeleteButton.setVisibility(View.INVISIBLE);
            elementDeleteButton.setEnabled(false);
        }
    }


    private void initializeStateSelection() {
        switch(element.getType()){
            case MEAN_OTHER:
            case MEAN:
            case AIRMEAN:
                MeanState m = ((IMean) element).getState();
                stateTextView.setText(m.getMeanAsReadableText());
                arrivedStateCheckBox.setChecked(false);
                arrivedStateCheckBox.setEnabled(false);
                releasedStateCheckBox.setChecked(false);
                releasedStateCheckBox.setEnabled(false);
                engagedStateCheckBox.setChecked(false);
                engagedStateCheckBox.setEnabled(false);
                switch (m){
                    case ASKED:
                        arrivedStateCheckBox.setChecked(false);
                        arrivedStateCheckBox.setEnabled(false);
                        engagedStateCheckBox.setChecked(false);
                        engagedStateCheckBox.setEnabled(false);
                        releasedStateCheckBox.setChecked(false);
                        releasedStateCheckBox.setEnabled(false);
                        break;
                    case VALIDATED:
                        arrivedStateCheckBox.setChecked(false);
                        arrivedStateCheckBox.setEnabled(true);
                        engagedStateCheckBox.setChecked(false);
                        engagedStateCheckBox.setEnabled(true);
                        releasedStateCheckBox.setChecked(false);
                        releasedStateCheckBox.setEnabled(true);
                        break;
                    case ARRIVED:
                        arrivedStateCheckBox.setChecked(true);
                        arrivedStateCheckBox.setEnabled(false);
                        engagedStateCheckBox.setChecked(false);
                        engagedStateCheckBox.setEnabled(true);
                        releasedStateCheckBox.setChecked(false);
                        releasedStateCheckBox.setEnabled(true);
                        break;
                    case ENGAGED:
                        arrivedStateCheckBox.setChecked(true);
                        arrivedStateCheckBox.setEnabled(false);
                        engagedStateCheckBox.setChecked(true);
                        engagedStateCheckBox.setEnabled(true);
                        releasedStateCheckBox.setChecked(false);
                        releasedStateCheckBox.setEnabled(true);
                        break;
                    case RELEASED:
                        arrivedStateCheckBox.setChecked(true);
                        arrivedStateCheckBox.setEnabled(false);
                        engagedStateCheckBox.setChecked(true);
                        engagedStateCheckBox.setEnabled(false);
                        releasedStateCheckBox.setChecked(true);
                        releasedStateCheckBox.setEnabled(false);
                        break;
                }
                break;
            case POINT_OF_INTEREST:
            case WATERPOINT:

                break;
        }


    }

    public interface OnFragmentInteractionListener {
        void updateElement(Element element);
        Mission getCurrentMission();
        void setCreateDronePathMode(boolean isDronePathMode);
        void cancelUpdate();
        void deleteElement(Element element);
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
