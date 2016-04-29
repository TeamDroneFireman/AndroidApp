package edu.istic.tdf.dfclient.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.UI.adapter.RoleArrayAdapter;
import edu.istic.tdf.dfclient.UI.adapter.ShapeArrayAdapter;
import edu.istic.tdf.dfclient.domain.element.Element;
import edu.istic.tdf.dfclient.domain.element.ElementType;
import edu.istic.tdf.dfclient.domain.element.IElement;
import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.drawable.PictoFactory;

import static edu.istic.tdf.dfclient.domain.element.Role.values;

public class ContextualDrawerFragment extends Fragment implements Observer {

    private OnFragmentInteractionListener mListener;

    @Bind(R.id.ElementLabelEdit)
    EditText ElementLabelEdit;

    @Bind(R.id.ElementSubmitButton)
    Button ElementSubmitButton;

    @Bind(R.id.ElementCancelButton)
    Button elementCancelButton;

    @Bind(R.id.RoleSpinner)
    Spinner roleSpinner;

    @Bind(R.id.FormSpinner)
    Spinner formSpinner;

    private Element element;

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

        View view = inflater.inflate(R.layout.fragment_contextual_drawer, container, false);

        ButterKnife.bind(this, view);

        ElementSubmitButton.setOnClickListener(new View.OnClickListener() {
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
                mListener.cancelUpdate();
            }
        });


        roleSpinner.setAdapter(new RoleArrayAdapter(getContext(), Role.values()));
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

    public void setSelectedElement(Element element) {
        this.element = element;
        ElementLabelEdit.setText(element.getName());
        roleSpinner.setSelection(Arrays.asList(Role.values()).indexOf(element.getRole()));

        PictoFactory.ElementForm[] forms = PictoFactory.ElementForm.values();

        switch (element.getType()){
            case MEAN:
                forms = new PictoFactory.ElementForm[]{PictoFactory.ElementForm.MEAN, PictoFactory.ElementForm.MEAN_PLANNED,  PictoFactory.ElementForm.MEAN_GROUP,  PictoFactory.ElementForm.MEAN_COLUMN };
                break;
            case AIRMEAN:
                forms = new PictoFactory.ElementForm[]{PictoFactory.ElementForm.AIRMEAN, PictoFactory.ElementForm.AIRMEAN_PLANNED};
                break;
            default:
                forms = new PictoFactory.ElementForm[]{PictoFactory.ElementForm.MEAN_OTHER, PictoFactory.ElementForm.MEAN_OTHER_PLANNED,PictoFactory.ElementForm.SOURCE, PictoFactory.ElementForm.TARGET,PictoFactory.ElementForm.WATERPOINT, PictoFactory.ElementForm.WATERPOINT_SUPPLY, PictoFactory.ElementForm.WATERPOINT_SUSTAINABLE};
                break;
        }

        formSpinner.setAdapter(new ShapeArrayAdapter(getContext(), forms));
        formSpinner.setSelection(Arrays.asList(forms).indexOf(element.getForm()));
    }

    public interface OnFragmentInteractionListener {
        void updateElement(Element element);
        void cancelUpdate();
    }
}
