package edu.istic.tdf.dfclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.TdfApplication;
import edu.istic.tdf.dfclient.dagger.component.ApplicationComponent;
import edu.istic.tdf.dfclient.http.exception.HttpException;
import edu.istic.tdf.dfclient.rest.handler.IRestReturnHandler;
import edu.istic.tdf.dfclient.rest.service.logout.LogoutRestService;
import edu.istic.tdf.dfclient.rest.service.logout.response.LogoutResponse;
import edu.istic.tdf.dfclient.util.AndroidUtils;
import eu.inloop.easygcm.EasyGcm;

/**
 * Created by maxime on 22/04/2016.
 *
 * Base activity that manages injection
 */
public class BaseActivity extends AppCompatActivity {

    private final static String TAG = "BaseActivity";

    View progressOverlay;

    @Inject
    LogoutRestService logoutRestService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Push
        EasyGcm.setLoggingLevel(EasyGcm.Logger.LEVEL_WARNING);
        EasyGcm.init(this);

        getApplicationComponent().inject(this);

        // UI Binding
        ButterKnife.bind(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return ((TdfApplication) getApplication()).getApplicationComponent();
    }

    public void logout() {
        logoutRestService.logout(new IRestReturnHandler<LogoutResponse>() {
            // This will be called, no matter what the result is
            public void onEnd() {

            }

            public void onNetworkError() {
                BaseActivity.this.runOnUiThread(new Runnable() { // If other error
                    public void run() {
                        Toast.makeText(BaseActivity.this, "Network error. Please check your internet connection.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onSuccess(LogoutResponse r) {

                ((TdfApplication) BaseActivity.this.getApplication()).deleteCredentials();
                //AuthHelper.storeCredentials(credentials);

                // Go to the next activity with transition
                BaseActivity.this.overridePendingTransition(R.anim.shake, R.anim.shake);

                Bundle intentBundle = new Bundle();
                final Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                ActivityCompat.startActivity(BaseActivity.this, intent, intentBundle);
                onEnd();
            }

            @Override
            public void onError(Throwable error) {

                if (error instanceof HttpException
                        && ((HttpException) error).getResponse() != null
                        && ((HttpException) error).getResponse().code() == 401) { // If unauthorized
                    BaseActivity.this.runOnUiThread(new Runnable() { // If other error
                        public void run() {
                            Toast.makeText(BaseActivity.this, "Authorization error.", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else if (error instanceof HttpException
                        && ((HttpException) error).getResponse() != null
                        && ((HttpException) error).getResponse().code() == 500) { // If no credentials
                    // Go back to login activity with transition
                    BaseActivity.this.overridePendingTransition(R.anim.shake, R.anim.shake);

                    Bundle intentBundle = new Bundle();
                    final Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                    ActivityCompat.startActivity(BaseActivity.this, intent, intentBundle);
                }

                onEnd();
            }
        });
    }

    public void showProgress(){
        if(progressOverlay != null){
            AndroidUtils.animateView(progressOverlay, View.VISIBLE, 0.4f, 200);
        }
    }

    public void hideProgress() {
        if(progressOverlay != null) {
            AndroidUtils.animateView(progressOverlay, View.GONE, 0, 200);
        }
    }
}
