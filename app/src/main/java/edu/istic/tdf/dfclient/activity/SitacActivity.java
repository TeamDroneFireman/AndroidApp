package edu.istic.tdf.dfclient.activity;

import android.app.ActionBar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.UI.Tool;
import edu.istic.tdf.dfclient.fragment.ContextualDrawerFragment;
import edu.istic.tdf.dfclient.fragment.LoginFragment;
import edu.istic.tdf.dfclient.fragment.SitacFragment;
import edu.istic.tdf.dfclient.fragment.ToolbarFragment;

public class SitacActivity extends AppCompatActivity implements SitacFragment.OnFragmentInteractionListener, ContextualDrawerFragment.OnFragmentInteractionListener, ToolbarFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitac);

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
        Toast.makeText(SitacActivity.this, tool.getTitle(), Toast.LENGTH_SHORT).show();
    }
}
