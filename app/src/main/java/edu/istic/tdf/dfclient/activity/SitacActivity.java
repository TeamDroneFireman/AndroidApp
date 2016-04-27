package edu.istic.tdf.dfclient.activity;

import edu.istic.tdf.dfclient.domain.element.IElement;
import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.domain.element.mean.drone.Drone;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;
import edu.istic.tdf.dfclient.drawable.PictoFactory;
import edu.istic.tdf.dfclient.fragment.ContextualDrawerFragment;

import android.location.Address;
import android.location.Location;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Element;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import javax.annotation.Resource;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.UI.Tool;
import edu.istic.tdf.dfclient.fragment.MeansTableFragment;
import edu.istic.tdf.dfclient.fragment.SitacFragment;
import edu.istic.tdf.dfclient.fragment.ToolbarFragment;
import eu.inloop.easygcm.EasyGcm;

public class SitacActivity extends BaseActivity implements
        SitacFragment.OnFragmentInteractionListener,
        ContextualDrawerFragment.OnFragmentInteractionListener,
        ToolbarFragment.OnFragmentInteractionListener,
        MeansTableFragment.OnFragmentInteractionListener{

    private Tool selectedTool;
    private View contextualDrawer;
    private View sitacContainer;

    private List<Element> elements;
    private Element selectedElement;

    private SitacFragment sitacFragment;
    private ToolbarFragment toolbarFragment;
    private ContextualDrawerFragment contextualDrawerFragment;
    private MeansTableFragment meansTableFragment;

    private android.support.v4.app.Fragment currentFragment;

    private Intervention intervention;

    private ArrayList<Observer> observers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        intervention = createInterventionBouton();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitac);
        contextualDrawer = findViewById(R.id.contextual_drawer_container);
        sitacContainer = findViewById(R.id.sitac_container);

        String registrationPush = EasyGcm.getRegistrationId(this);
        Log.i("MAXIME", "Registration push : " + registrationPush);

        sitacFragment = SitacFragment.newInstance();
        toolbarFragment = ToolbarFragment.newInstance();
        contextualDrawerFragment = ContextualDrawerFragment.newInstance();
        meansTableFragment = MeansTableFragment.newInstance();

        observers.add(sitacFragment);
        observers.add(toolbarFragment);
        observers.add(contextualDrawerFragment);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.sitac_container, meansTableFragment, meansTableFragment.getTag())
                .hide(meansTableFragment)
                .add(R.id.sitac_container, sitacFragment, sitacFragment.getTag())
                .add(R.id.toolbar_container, toolbarFragment)
                .add(R.id.contextual_drawer_container, contextualDrawerFragment)
                .commit();

        currentFragment = sitacFragment;

    }

    @Override
    public void handleSelectedTool(Tool tool) {
        this.selectedTool = tool;
        Toast.makeText(SitacActivity.this, tool.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public Intervention getIntervention() {
        return intervention;
    }

    @Override
    public Tool getSelectedTool() {
        return selectedTool;
    }

    @Override
    public void handleElementAdded() {
        this.selectedTool = null;
        showContextualDrawer();
    }

    @Override
    public void handleCancelSelection() {
        this.selectedTool = null;
        hideContextualDrawer();
    }

    private void showContextualDrawer(){
        contextualDrawer.animate().translationX(0);

    }
    private void hideContextualDrawer(){
        contextualDrawer.animate().translationX(contextualDrawer.getWidth());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sitac_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.switch_to_means_table:
                switchTo(meansTableFragment);
                break;
            case R.id.switch_to_sitac:
                switchTo(sitacFragment);
                break;
            default:
                break;
        }

        return true;
    }

    private void notifyObservers(){
        for(Observer observer : observers){
            observer.notify();
        }
    }

    void switchTo (Fragment fragment)
    {
        if (fragment.isVisible())
            return;
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.setCustomAnimations(R.anim.frag_slide_in, R.anim.frag_slide_out);

        // Make sure the next view is below the current one
        fragment.getView().bringToFront();

        // Hide the current fragment
        t.hide(currentFragment);
        t.show(fragment);
        currentFragment = fragment;

        // You probably want to add the transaction to the backstack
        // so that user can use the back button
        t.addToBackStack(null);
        t.commit();
    }

    private Intervention createInterventionBouton(){

        Intervention intervention = new Intervention();

        Address address = new Address(Locale.FRANCE);
        address.setLatitude(1);
        address.setLongitude(1);
        intervention.setAddress(address);

        IElement drone1 = new Drone();
        drone1.setForm(PictoFactory.ElementForm.AIRMEAN);
        drone1.setRole(Role.FIRE);
        drone1.setName("DRONE");

        Location targetLocation = new Location("");//provider name is unecessary
        targetLocation.setLatitude(48.1154336);//your coords of course
        targetLocation.setLongitude(-1.638722);
        drone1.setLocation(targetLocation);
        intervention.addElement(drone1);

        return intervention;

    }

}
