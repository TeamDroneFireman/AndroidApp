package edu.istic.tdf.dfclient.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.activity.MainMenuActivity;
import edu.istic.tdf.dfclient.auth.AuthHelper;
import edu.istic.tdf.dfclient.auth.Credentials;
import edu.istic.tdf.dfclient.dao.domain.InterventionDao;
import edu.istic.tdf.dfclient.http.exception.HttpException;
import edu.istic.tdf.dfclient.rest.handler.IRestReturnHandler;
import edu.istic.tdf.dfclient.rest.service.login.LoginRestService;
import edu.istic.tdf.dfclient.rest.service.login.response.LoginResponse;

public class LoginFragment extends Fragment {

    // Parameters
    private final static int BUTTON_ERROR_DISPLAY_TIME_IN_MS = 2500;

    // UI
    @Bind(R.id.username) EditText usernameTxt;
    @Bind(R.id.password) EditText passwordTxt;
    @Bind(R.id.loginButton) ActionProcessButton loginBt;

    private OnFragmentInteractionListener mListener;

    LoginRestService loginRestService;

    // TODO: Rename and change types and number of parameters
    // loginRestService is instanciated from Dagger FragmentModule
    public static LoginFragment newInstance(LoginRestService loginRestService) {
        LoginFragment fragment = new LoginFragment();
        fragment.loginRestService = loginRestService;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container,savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);

        // Set button to endless mode
        loginBt.setMode(ActionProcessButton.Mode.ENDLESS);

        // Listeners
        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment.this.login(usernameTxt.getText().toString(), passwordTxt.getText().toString());
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

    public void login(String user, String password) {
        loginBt.setProgress(1);
        loginBt.setEnabled(false);
        loginRestService.login(user, password, new IRestReturnHandler<LoginResponse>() {
            // This will be called, no matter what the result is
            public void onEnd() {
                LoginFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loginBt.setEnabled(true);
                    }
                });

            }

            @Override
            public void onSuccess(LoginResponse r) {

                // Store credentials
                Credentials credentials = new Credentials(r.getUserId(), r.getToken());
                AuthHelper.storeCredentials(credentials);

                // Go to the next activity
                Intent intent = new Intent(LoginFragment.this.getActivity(), MainMenuActivity.class);
                LoginFragment.this.getActivity().startActivity(intent);

                onEnd();
            }

            @Override
            public void onError(Throwable error) {

                // TODO : Remove this when auth service will work
                /*LoginResponse loginResponse = new LoginResponse();
                loginResponse.setUserId("571a4902ddd1850100ce8691");
                loginResponse.setToken("CS5JXqF9wQ7CVfdvpTx3oJxGPCGiKUfYDWZw2Hk0A29BTYCESuurYxYQLHQaSemB");
                onSuccess(loginResponse);*/

                LoginFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Animation shake = AnimationUtils.loadAnimation(LoginFragment.this.getContext(), R.anim.shake);
                        loginBt.startAnimation(shake);
                        loginBt.setProgress(-1);

                        // After a few seconds, set button back to normal position
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        loginBt.setProgress(0);
                                    }
                                }, BUTTON_ERROR_DISPLAY_TIME_IN_MS);
                    }
                });



                // TODO: Activate this back when auth service works
                /*
                if(error instanceof HttpException
                        && ((HttpException) error).getResponse().code() == 401) { // If unauthorized
                    LoginFragment.this.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(LoginFragment.this.getActivity(), "Identifiants incorrects", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    LoginFragment.this.getActivity().runOnUiThread(new Runnable() { // If other error
                        public void run() {
                            Toast.makeText(LoginFragment.this.getActivity(), "An error occured", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                */

                onEnd();
            }
        });
    }

    public interface OnFragmentInteractionListener {
    }
}
