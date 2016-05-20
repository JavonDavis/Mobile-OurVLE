package edu.uwi.mona.mobileourvle.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import edu.uwi.mona.mobileourvle.R;
import edu.uwi.mona.mobileourvle.classes.tasks.LoginTask;

public class LoginFragment extends Fragment{

    private EditText mUsernameTextbox;
    private EditText mPasswordTextBox;
    private ProgressBar progressBar;
    private LinearLayout loginBox;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }


    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View fragmentView = inflater.inflate(R.layout.fragment_login_main, container, false);

        Button loginButton = (Button) fragmentView.findViewById(R.id.login_btn);

        mUsernameTextbox = (EditText) fragmentView.findViewById(R.id.id_number_field);
        mPasswordTextBox = (EditText) fragmentView.findViewById(R.id.password_field);


        TextView ourvleLong = (TextView) fragmentView.findViewById(R.id.ourvle_long);
        TextView ourvleShort = (TextView) fragmentView.findViewById(R.id.ourvle_short);

        progressBar = (ProgressBar) fragmentView.findViewById(R.id.progress);
        loginBox = (LinearLayout) fragmentView.findViewById(R.id.loginBox);

        Typeface main = Typeface.createFromAsset(getActivity().getAssets(),"roboto.ttf");
        Typeface main2 = Typeface.createFromAsset(getActivity().getAssets(),"jacks.ttf");

        mUsernameTextbox.setTypeface(main);
        mPasswordTextBox.setTypeface(main);

        loginButton.setTypeface(main);

        ourvleLong.setTypeface(main);
        ourvleShort.setTypeface(main2);

        // Attach Login button
        loginButton.setOnClickListener(new LoginButtonListener());

        return fragmentView;
    }


    /* ===================== Button Listeners ================ */
    private class LoginButtonListener implements OnClickListener {
        @Override
        public void onClick(final View v) {
            //loginButton.setEnabled(false);
            final String enteredUsername = mUsernameTextbox.getText().toString();
            final String enteredPassword = mPasswordTextBox.getText().toString();

            new LoginTask(getActivity(),enteredUsername,enteredPassword,progressBar,loginBox).execute();

        }
    }
}
