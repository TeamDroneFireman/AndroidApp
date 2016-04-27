package edu.istic.tdf.dfclient.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Observable;
import java.util.Observer;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.domain.element.IElement;

public class ContextualDrawerFragment extends Fragment implements Observer {

    private OnFragmentInteractionListener mListener;

    @Bind(R.id.ElementLabelEdit)
    EditText ElementLabelEdit;

    @Bind(R.id.ElementSubmitButton)
    Button ElementSubmitButton;

    private IElement element;

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
                mListener.updateElement(element);
            }
        });
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

    public void setSelectedElement(IElement element) {
        this.element = element;
    }

    public interface OnFragmentInteractionListener {
        void updateElement(IElement element);
    }
}
