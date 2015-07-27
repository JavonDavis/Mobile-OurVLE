package edu.uwi.mona.mobileourvle.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpStatus;

import edu.uwi.mona.mobileourvle.R;
import edu.uwi.mona.mobileourvle.classes.tasks.LoginTask;

public class LoginFragment extends Fragment{

    private EditText mUsernameTextbox;
    private EditText mPasswordTextBox;
    private TextView ourvleShort,ourvleLong;
    private Button loginButton;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }


    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View fragmentView = inflater.inflate(R.layout.fragment_login_main, container, false);

        loginButton = (Button) fragmentView.findViewById(R.id.login_btn);

        mUsernameTextbox = (EditText) fragmentView.findViewById(R.id.id_number_field);
        mPasswordTextBox = (EditText) fragmentView.findViewById(R.id.password_field);

        ourvleLong = (TextView) fragmentView.findViewById(R.id.ourvle_long);
        ourvleShort = (TextView) fragmentView.findViewById(R.id.ourvle_short);

        Typeface main = Typeface.createFromAsset(getActivity().getAssets(),"jacks.ttf");

        ourvleLong.setTypeface(main);
        ourvleShort.setTypeface(main);

        // Attach Login button
        loginButton.setOnClickListener(new LoginButtonListener());

        return fragmentView;
    }


    /* ===================== Button Listeners ================ */
    private class LoginButtonListener implements OnClickListener {
        @Override
        public void onClick(final View v) {
            loginButton.setEnabled(false);
            final String enteredUsername = mUsernameTextbox.getText().toString();
            final String enteredPassword = mPasswordTextBox.getText().toString();

            new LoginTask(getActivity(),enteredUsername,enteredPassword).execute();

        }
    }
}
