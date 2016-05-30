package edu.istic.tdf.dfclient.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.UI.adapter.GalleryListAdapter;

public class GalleryDrawerFragment extends Fragment implements Observer {
    private OnFragmentInteractionListener mListener;

    private View view;

    private GalleryListAdapter galleryListAdapter;

    private ArrayList<String> itemname = new ArrayList<>();
    private ArrayList<Integer> imgid = new ArrayList<>();

    @Bind(R.id.list_view_gallery)
    ListView listViewGallery;

    public GalleryDrawerFragment() {
        // Required empty public constructor
    }

    public static GalleryDrawerFragment newInstance() {
        GalleryDrawerFragment fragment = new GalleryDrawerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gallery_drawer, container, false);
        ButterKnife.bind(this, view);

        galleryListAdapter = new GalleryListAdapter(this.getActivity(), itemname, imgid);

        listViewGallery.setAdapter(galleryListAdapter);

        listViewGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO: 30/05/16 what happen when click on an image
            }
        });

        updateList();

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

    public void updateList()
    {
        itemname.add("Safari");
        itemname.add("Camera");
        itemname.add("Global");
        itemname.add("FireFox");
        
        imgid.add(R.drawable.action_zone1);
        imgid.add(R.drawable.airmean_planned);
        imgid.add(R.drawable.common_google_signin_btn_icon_dark);
        imgid.add(R.drawable.common_google_signin_btn_icon_dark_focused);
        // TODO: 30/05/16

        galleryListAdapter.notifyDataSetChanged();
    }

    @Override
    public void update(Observable observable, Object data) {

    }

    public interface OnFragmentInteractionListener {
    }
}
