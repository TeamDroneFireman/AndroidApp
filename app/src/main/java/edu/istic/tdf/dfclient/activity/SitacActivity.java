package edu.istic.tdf.dfclient.activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pushbots.push.Pushbots;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.UI.Tool;
import edu.istic.tdf.dfclient.fragment.ContextualDrawerFragment;
import edu.istic.tdf.dfclient.fragment.LoginFragment;
import edu.istic.tdf.dfclient.fragment.SitacFragment;
import edu.istic.tdf.dfclient.fragment.ToolbarFragment;

public class SitacActivity extends BaseActivity implements SitacFragment.OnFragmentInteractionListener, ContextualDrawerFragment.OnFragmentInteractionListener, ToolbarFragment.OnFragmentInteractionListener {

    private Tool selectedTool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitac);

        // Pushbot
        Pushbots.sharedInstance().init(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.sitac_container, SitacFragment.newInstance())
                .commit();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.toolbar_container, ToolbarFragment.newInstance())
                .commit();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contextual_drawer_container, ContextualDrawerFragment.newInstance())
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
        ContextualDrawerFragment contextualDrawerFragment = (ContextualDrawerFragment) (getSupportFragmentManager()
                .findFragmentById(R.id.contextual_drawer_container));
        ViewGroup.LayoutParams params = contextualDrawerFragment.getView().getLayoutParams();
        params.width = 400;
        contextualDrawerFragment.getView().setLayoutParams(params);
        contextualDrawerFragment.getView().requestLayout();
/*
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_left)
                .replace(R.id.contextual_drawer_container, ContextualDrawerFragment.newInstance())
                .commit();*/

        this.selectedTool = null;
    }

    @Override
    public void handleCancelSelection() {
        this.selectedTool = null;
        ContextualDrawerFragment contextualDrawerFragment = (ContextualDrawerFragment) (getSupportFragmentManager()
                .findFragmentById(R.id.contextual_drawer_container));
        ViewGroup.LayoutParams params = contextualDrawerFragment.getView().getLayoutParams();
        params.width = 0;
        contextualDrawerFragment.getView().setLayoutParams(params);
        contextualDrawerFragment.getView().requestLayout();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sitac_menu, menu);
        return true;
    }
}
