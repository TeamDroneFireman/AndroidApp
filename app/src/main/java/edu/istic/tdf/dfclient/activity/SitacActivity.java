package edu.istic.tdf.dfclient.activity;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.istic.tdf.dfclient.dao.domain.InterventionDao;
import edu.istic.tdf.dfclient.dao.handler.IDaoSelectReturnHandler;
import edu.istic.tdf.dfclient.domain.intervention.Intervention;
import edu.istic.tdf.dfclient.fragment.ContextualDrawerFragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Element;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.inject.Inject;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.UI.Tool;
import edu.istic.tdf.dfclient.fragment.SitacFragment;
import edu.istic.tdf.dfclient.fragment.ToolbarFragment;
import eu.inloop.easygcm.EasyGcm;

public class SitacActivity extends BaseActivity implements
        SitacFragment.OnFragmentInteractionListener,
        ContextualDrawerFragment.OnFragmentInteractionListener,
        ToolbarFragment.OnFragmentInteractionListener {

    private Tool selectedTool;
    @Bind(R.id.contextual_drawer_container) View contextualDrawer;
    @Bind(R.id.sitac_container) View sitacContainer;

    private List<Element> elements;
    private Element selectedElement;

    @Inject
    InterventionDao interventionDao;

    private Intervention intervention;

    private ArrayList<Observer> observers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitac);

        ButterKnife.bind(this);

        interventionDao.find("1", new IDaoSelectReturnHandler<Intervention>() {
            @Override
            public void onRepositoryResult(Intervention r) {}

            @Override
            public void onRestResult(Intervention r) {
                intervention = r;
            }

            @Override
            public void onRepositoryFailure(Throwable e) {
                Log.e("MAXIME", "REPO FAILURE");
            }

            @Override
            public void onRestFailure(Throwable e) {
                Log.e("MAXIME", "REST FAILURE");
            }
        });

        SitacFragment sitacFragment = SitacFragment.newInstance();
        ToolbarFragment toolbarFragment = ToolbarFragment.newInstance();
        ContextualDrawerFragment contextualDrawerFragment = ContextualDrawerFragment.newInstance();

        observers.add(sitacFragment);
        observers.add(toolbarFragment);
        observers.add(contextualDrawerFragment);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.sitac_container, sitacFragment)
                .replace(R.id.toolbar_container, toolbarFragment)
                .replace(R.id.contextual_drawer_container, contextualDrawerFragment)
                .commit();

    }

    @Override
    public void handleSelectedTool(Tool tool) {
        this.selectedTool = tool;
        Toast.makeText(SitacActivity.this, tool.getTitle(), Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(this, MeansTableActivity.class);
                this.startActivity(intent);
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


}
