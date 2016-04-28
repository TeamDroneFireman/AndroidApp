package edu.istic.tdf.dfclient.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.activity.MainMenuActivity;
import edu.istic.tdf.dfclient.dao.domain.InterventionDao;
import edu.istic.tdf.dfclient.dao.domain.element.DroneDao;
import edu.istic.tdf.dfclient.dao.domain.element.InterventionMeanDao;
import edu.istic.tdf.dfclient.dao.handler.IDaoWriteReturnHandler;
import edu.istic.tdf.dfclient.domain.element.IElement;
import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.domain.element.mean.IMean;
import edu.istic.tdf.dfclient.domain.element.mean.MeanState;
import edu.istic.tdf.dfclient.domain.element.mean.drone.Drone;
import edu.istic.tdf.dfclient.domain.element.mean.interventionMean.InterventionMean;
import edu.istic.tdf.dfclient.domain.geo.GeoPoint;
import edu.istic.tdf.dfclient.domain.geo.Location;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;
import edu.istic.tdf.dfclient.domain.intervention.SinisterCode;
import edu.istic.tdf.dfclient.drawable.PictoFactory;

/**
 * Created by btessiau on 25/04/16.
 */
public class InterventionCreateFormFragment extends Fragment {

    @Bind(R.id.address)
    EditText address;

    // UI
    @Bind(R.id.generate_lat_lng_from_address_button)
    Button generateLatLngFromAddressBt;

    @Bind(R.id.lat)
    EditText lat;

    @Bind(R.id.lng)
    EditText lng;

    @Bind(R.id.sinister_code)
    Spinner sinister_code;

    @Bind(R.id.means_list)
    Spinner means_list;

    // UI
    @Bind(R.id.interventionCreationValidationButton)
    Button interventionCreationValidationBt;

    private OnFragmentInteractionListener mListener;

    InterventionDao interventionDao;

    DroneDao droneDao;

    InterventionMeanDao interventionMeanDao;

    // for listView sinister_code
    private ArrayList<String> sinisters =new ArrayList<String>();
    private ArrayAdapter<String> sinistersAdapter;

    // for listView means_list
    private ArrayList<String> means =new ArrayList<String>();
    private ArrayAdapter<String> meansAdapter;

    // use to handle the geopoints from an address
    private Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Bundle bundle = msg.getData();
                    setAddLatLng(bundle.getString("add"), bundle.getDouble("lat"), bundle.getDouble("lng"));
                    break;
                default:
                    break;
            }
        }
    };

    public InterventionCreateFormFragment() {
        // Required empty public constructor
    }

    public static InterventionCreateFormFragment newInstance(InterventionDao interventionDao,
                                                             DroneDao droneDao,
                                                             InterventionMeanDao interventionMeanDao) {
        InterventionCreateFormFragment fragment = new InterventionCreateFormFragment();
        fragment.interventionDao = interventionDao;
        fragment.droneDao = droneDao;
        fragment.interventionMeanDao = interventionMeanDao;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_intervention_create_form, container, false);
        ButterKnife.bind(this, view);

        interventionCreationValidationBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsValideForm())
                {
                    computeIntervention();
                }
            }
        });

        generateLatLngFromAddressBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateLatLngFromAddress();
            }
        });

        meansAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                means);
        means_list.setAdapter(meansAdapter);

        sinistersAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, sinisters);
        sinistersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sinister_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadMeanFromSinisterCode(sinisters.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sinister_code.setAdapter(sinistersAdapter);

        loadSinisters();

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

    // TODO: 26/04/16
    private void loadSinisters()
    {
        sinisters.add("SAP");
        sinisters.add("INC");
        sinistersAdapter.notifyDataSetChanged();
    }

    public void loadMeanFromSinisterCode(String sinisterCode)
    {
        means.clear();

        switch (sinisterCode) {
            case "SAP":
                means.add("VSAV");
                break;
            case "INC":
                means.add("FTP");
                means.add("FTP");
                means.add("EPA");
                means.add("VLCG");
                break;
        }
        meansAdapter.notifyDataSetChanged();
    }

    public interface OnFragmentInteractionListener {

        /**
         * Called iff the form is complete
         *
         */
        void onCreateIntervention();

    }


    // JUST FOR TEST, Elements drone or mean examples
    public void makeElementsExample(Intervention intervention){

        Drone elemDrone1 = new Drone();
        Drone elemDrone2 = new Drone();

        InterventionMean elemInterventionMean1 = new InterventionMean();
        InterventionMean elemInterventionMean2 = new InterventionMean();
        InterventionMean elemInterventionMean3 = new InterventionMean();

        elemDrone1.setName("Drone1-Patrick");
        elemDrone1.setRole(Role.DEFAULT);
        elemDrone1.setLocation(intervention.getLocation());
        elemDrone1.setForm(PictoFactory.ElementForm.AIRMEAN);
        elemDrone1.setAction("IN_PROGRESS");
        elemDrone1.setState(MeanState.ASKED);

        elemDrone2.setName("Drone1-Michel");
        elemDrone2.setRole(Role.DEFAULT);
        elemDrone2.setLocation(intervention.getLocation());
        elemDrone2.setForm(PictoFactory.ElementForm.AIRMEAN);
        elemDrone2.setAction("IN_PROGRESS");
        elemDrone2.setState(MeanState.ASKED);

        elemInterventionMean1.setName("IntMean-Jéjé");
        elemInterventionMean1.setRole(Role.DEFAULT);
        elemInterventionMean1.setLocation(intervention.getLocation());
        elemInterventionMean1.setForm(PictoFactory.ElementForm.WATERPOINT);
        elemInterventionMean1.setState(MeanState.ENGAGED);
        elemInterventionMean1.setAction("IN_PROGRESS");

        elemInterventionMean2.setName("IntMean-Albert");
        elemInterventionMean2.setRole(Role.DEFAULT);
        elemInterventionMean2.setLocation(intervention.getLocation());
        elemInterventionMean2.setForm(PictoFactory.ElementForm.MEAN_GROUP);
        elemInterventionMean2.setState(MeanState.RELEASED);
        elemInterventionMean2.setAction("IN_PROGRESS");

        elemInterventionMean3.setName("InterventionMean-JosephJeanMie");
        elemInterventionMean3.setRole(Role.DEFAULT);
        elemInterventionMean3.setLocation(intervention.getLocation());
        elemInterventionMean3.setForm(PictoFactory.ElementForm.MEAN_PLANNED);
        elemInterventionMean3.setState(MeanState.VALIDATED);
        elemInterventionMean3.setAction("IN_PROGRESS");

        Collection<IElement> elements = new HashSet<IElement>();
        elements.add(elemDrone1);
        elements.add(elemDrone2);
        elements.add(elemInterventionMean1);
        elements.add(elemInterventionMean2);
        elements.add(elemInterventionMean3);

        intervention.setElements(elements);

    }

    private void computeIntervention() {
        final Intervention intervention = new Intervention();

        // TODO: 26/04/16 when model ok

        /*for(String mean:means) {
            intervention.addElement(compute(mean));
        }*/

        // address and geopoint inside location
        Location location = new Location();
        location.setAddress(address.getText().toString());
        GeoPoint geoPoint = new GeoPoint();
        geoPoint.setLatitude(Double.valueOf(lat.getText().toString()));
        geoPoint.setLongitude(Double.valueOf(lng.getText().toString()));
        location.setGeopoint(geoPoint);
        intervention.setLocation(location);

        //date creation
        Date now = new Date();
        intervention.setCreationDate(now);
        String strNow = new SimpleDateFormat("yyyy-MM-dd'-'HH:mm:ss", Locale.FRANCE).format(now);

        String str_sinister_code = sinister_code.getSelectedItem().toString();

        // TODO: 27/04/16 intervention code
        //intervention code
        switch (str_sinister_code) {
            case "SAP":
                intervention.setSinisterCode(SinisterCode.SAP);
                break;
            case "INC":
                intervention.setSinisterCode(SinisterCode.INC);
                break;
        }
        
        //name
        intervention.setName(str_sinister_code + "-" + strNow);

        //archived
        intervention.setArchived(false);

        ((MainMenuActivity) InterventionCreateFormFragment.this.getActivity()).showProgress();

        //makeElementsExample(intervention);
        interventionDao.persist(intervention, new IDaoWriteReturnHandler() {
            @Override
            public void onSuccess() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MainMenuActivity) InterventionCreateFormFragment.this.getActivity()).hideProgress();
                        cleanForm();
                        mListener.onCreateIntervention();
                    }
                });
            }

            @Override
            public void onRepositoryFailure(Throwable e) {
                Log.e("", "REPO FAILURE");
            }

            @Override
            public void onRestFailure(Throwable e) {
                Log.e("", "REST FAILURE");
            }
        });
    }

    private IMean compute(String mean) {
        IMean m = new InterventionMean();
        m.setState(MeanState.ASKED);
        m.setRole(Role.DEFAULT);

        return m;
    }

    private boolean IsValideForm() {
        boolean isValid = true;

        isValid = checkLat();
        isValid = checkLng() && isValid;
        isValid = checkSinisterCode() && isValid;

        return isValid;
    }

    private boolean checkLat() {
        try
        {
            Double lat = Double.parseDouble(this.lat.getText().toString());
            if(lat > 90 || lat < -90)
            {
                showErrorMsg("Latitude non valide");

                return false;
            }
            else
            {
                return true;
            }
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
            showErrorMsg("Latitude non valide");
        }

        return false;
    }

    private boolean checkLng() {
        try
        {
            Double lng = Double.parseDouble(this.lng.getText().toString());
            if(lng > 180 || lng < -180)
            {
                showErrorMsg("Longitude non valide");

                return false;
            }
            else
            {
                return true;
            }
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
            showErrorMsg("Longitude non valide");
        }

        return false;
    }

    private boolean checkSinisterCode(){
        if (this.sinisters.isEmpty())
        {
            showErrorMsg("Pas de code sinistre");
            return false;
        };

        return true;
    }

    private void generateLatLngFromAddress(){
        DataLongOperationAsynchTask dataLongOperationAsynchTask = new DataLongOperationAsynchTask();
        this.lat.setText("");
        this.lng.setText("");
        dataLongOperationAsynchTask.execute(this.address.getText().toString());
    }

    private void setAddLatLng(String add, Double lat, Double lng) {
        this.lat.setText(String.valueOf(lat));
        this.lng.setText(String.valueOf(lng));
        this.address.setText(add);
    }

    private class DataLongOperationAsynchTask extends AsyncTask<String, Void, String[]> {
        ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please wait...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String[] doInBackground(String... params) {
            String response;
            try {
                String url = "http://maps.google.com/maps/api/geocode/json?address=" + params[0] + "&sensor=false";
                url = url.replaceAll(" ", "%20");
                response = getLatLongByURL(url);
                Log.d("response", "" + response);
                return new String[]{response};
            } catch (Exception e) {
                return new String[]{"error"};
            }
        }

        @Override
        protected void onPostExecute(String... result) {
            try {
                JSONObject jsonObject = new JSONObject(result[0]);

                double lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lng");

                double lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lat");

                String formatted_address = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                .getString("formatted_address");

                                Log.d("latitude", "" + lat);
                Log.d("longitude", "" + lng);


                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putDouble("lat", lat);
                bundle.putDouble("lng",lng);
                bundle.putString("add",formatted_address);
                message.setData(bundle);
                message.what = 1;
                myHandler.sendMessage(message);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    private String getLatLongByURL(String requestURL) {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private void showErrorMsg(String msg){
        Toast t = Toast.makeText(getContext(), msg, Toast.LENGTH_LONG);
        t.getView().setBackgroundColor(Color.RED);
        t.show();
    }

    private void cleanForm(){
        this.address.setText("");
        this.lat.setText("");
        this.lng.setText("");
    }

}
