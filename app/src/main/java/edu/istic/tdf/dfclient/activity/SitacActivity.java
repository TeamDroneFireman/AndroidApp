package edu.istic.tdf.dfclient.activity;

import android.os.Bundle;
import android.widget.Toast;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.UI.Tool;
import edu.istic.tdf.dfclient.fragment.ContextualDrawerFragment;
import edu.istic.tdf.dfclient.fragment.SitacFragment;
import edu.istic.tdf.dfclient.fragment.ToolbarFragment;
import edu.istic.tdf.dfclient.push.PushListener;
import eu.inloop.easygcm.EasyGcm;

public class SitacActivity extends BaseActivity implements SitacFragment.OnFragmentInteractionListener, ContextualDrawerFragment.OnFragmentInteractionListener, ToolbarFragment.OnFragmentInteractionListener {

    private Tool selectedTool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitac);

        //String registrationPush = EasyGcm.getRegistrationId(this);

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

    }
}
