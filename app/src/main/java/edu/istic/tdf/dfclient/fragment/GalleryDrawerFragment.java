package edu.istic.tdf.dfclient.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.UI.adapter.GalleryListAdapter;
import edu.istic.tdf.dfclient.domain.image.ImageDrone;

public class GalleryDrawerFragment extends Fragment implements Observer {
    private OnFragmentInteractionListener mListener;

    private View view;

    private GalleryListAdapter galleryListAdapter;

    private ArrayList<String> drones = new ArrayList<>();
    private ArrayList<String> dates = new ArrayList<>();
    private ArrayList<String> imgUrl = new ArrayList<>();

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

        galleryListAdapter = new GalleryListAdapter(this.getActivity(), drones, imgUrl, dates);

        listViewGallery.setAdapter(galleryListAdapter);

        listViewGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ImageView imageView = new ImageView(getContext());
                Picasso.with(getContext()).load(imgUrl.get(position)).into(imageView);

                //create a dialog in order to show in full screnn the image
                AlertDialog.Builder imgDialog = new AlertDialog.Builder(
                        getActivity());

                Dialog dialog = imgDialog.create();

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                dialog.show();
                dialog.getWindow().setAttributes(lp);

                dialog.show();

                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                LinearLayout layout = (LinearLayout) layoutInflater.inflate(R.layout.img_full_screen_popup, null);

                ((ImageView) layout.findViewById(R.id.imgFullScrenn)).setImageDrawable(imageView.getDrawable());
                dialog.setContentView(layout);
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

    /**
     * methode that will fill the listview
     * @param imageDrones
     */
    public void updateList(Collection<ImageDrone> imageDrones)
    {
        drones.clear();
        imgUrl.clear();
        dates.clear();

        for (ImageDrone imageDrone : imageDrones) {
            drones.add(imageDrone.getDrone());
            imgUrl.add(imageDrone.getLink());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE);
            dates.add(sdf.format(imageDrone.getTakenAt()));
        }

        galleryListAdapter.notifyDataSetChanged();
    }

    @Override
    public void update(Observable observable, Object data) {

    }

    public interface OnFragmentInteractionListener {
    }
}
