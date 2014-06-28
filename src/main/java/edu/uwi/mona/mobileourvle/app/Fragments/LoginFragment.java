package edu.uwi.mona.mobileourvle.app.Fragments;

import org.apache.http.HttpStatus;
import org.sourceforge.ah.android.utilities.Communication.CommuncationModule;
import org.sourceforge.ah.android.utilities.Communication.Interfaces.OnCommunicationResponseListener;
import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONDecoder;
import org.sourceforge.ah.android.utilities.Communication.Response.ResponseError;
import org.sourceforge.ah.android.utilities.Communication.Response.ResponseObject;
import org.sourceforge.ah.android.utilities.Cryptography.AESUtil;
import org.sourceforge.ah.android.utilities.Dialog.DialogCreator;
import org.sourceforge.ah.android.utilities.Dialog.DialogManager;
import org.sourceforge.ah.android.utilities.Plugins.DefaultCommunicationModulePlugin;
import org.sourceforge.ah.android.utilities.Plugins.BaseClass.PluggableFragment;
import org.sourceforge.ah.android.utilities.Widgets.Fragments.DialogFragmentBase;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import edu.uwi.mona.mobileourvle.app.Activities.LoginMainActivity;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.CourseForum;
import edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.JSONDescriptors.Moodle.Modules.Forum.CourseForumDescriptior;
import edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.RemoteAPIRequests.WebServiceFunctions.GetCourseDiscussions;
import edu.uwi.mona.mobileourvle.app.R;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.SessionContext;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.UserSession;
import edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.JSONDescriptors.Authentication.SessionContextDescriptor;
import edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.JSONDescriptors.Authentication.UserSessionDescriptior;
import edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.RemoteAPIRequests.LoginRemoteFunction;
import edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.RemoteAPIRequests.WebServiceFunctions.GetSessionContext;
import edu.uwi.mona.mobileourvle.app.Classes.Util.ApplicationDataManager;
import edu.uwi.mona.mobileourvle.app.Fragments.Shared.Dialogs.ProgressDialogFragment;

import java.util.List;

public class LoginFragment extends PluggableFragment implements
        OnCommunicationResponseListener, DialogCreator {

    private EditText mUsernameTextbox;
    private EditText mPasswordTextBox;

    private Listener mListener;

    private UserSession mUserSession;

    private DefaultCommunicationModulePlugin mCommunicationModulePlugin;

    private SessionContext mSessionContext;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (Listener) activity;
        } catch (final ClassCastException e) {
            mListener = new DefaultLoginResponse();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        mCommunicationModulePlugin = new DefaultCommunicationModulePlugin();
        registerPlugin(mCommunicationModulePlugin);

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {

        final SharedPreferences preferences = getApplicationContext().getSharedPreferences(
                LoginMainActivity.SAVED_LOGIN_PREFERENCES_NAME, Context.MODE_PRIVATE);

        final String savedEncryptionKey = preferences.getString(LoginMainActivity.ENCRYPTION_KEY, "");
        final String savedUsername = preferences.getString(LoginMainActivity.USERNAME_KEY, "");
        final String savedPassword = preferences.getString(LoginMainActivity.PASSWORD_KEY, "");

        if (!savedEncryptionKey.isEmpty() && !savedUsername.isEmpty() && !savedPassword.isEmpty()) {
            final String username = AESUtil.decryptAESString(savedEncryptionKey, savedUsername);

            final View v = inflater.inflate(R.layout.fragment_auto_login_main, container, false);

            final TextView idNumberView = (TextView) v.findViewById(R.id.id_number);

            idNumberView.setText(username);
            return v;
        }

        // Inflate the layout for this fragment
        final View fragmentView = inflater.inflate(R.layout.fragment_login_main, container, false);

        final Button loginButton = (Button) fragmentView.findViewById(R.id.login_btn);

        mUsernameTextbox = (EditText) fragmentView.findViewById(R.id.id_number_field);
        mPasswordTextBox = (EditText) fragmentView.findViewById(R.id.password_field);

        // Attach Login button
        loginButton.setOnClickListener(new LoginButtonListener());

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();

        final SharedPreferences preferences = getApplicationContext().getSharedPreferences(
                LoginMainActivity.SAVED_LOGIN_PREFERENCES_NAME, Context.MODE_PRIVATE);

        final String savedEncryptionKey = preferences.getString(LoginMainActivity.ENCRYPTION_KEY, "");
        final String savedUsername = preferences.getString(LoginMainActivity.USERNAME_KEY, "");
        final String savedPassword = preferences.getString(LoginMainActivity.PASSWORD_KEY, "");


        if (!savedEncryptionKey.isEmpty() && !savedUsername.isEmpty() && !savedPassword.isEmpty()) {
            final String username = AESUtil.decryptAESString(savedEncryptionKey, savedUsername);
            final String password = AESUtil.decryptAESString(savedEncryptionKey, savedPassword);

            mCommunicationModulePlugin.turnOnLoadingIcon();
            CommuncationModule.sendAsyncRequest(
                    getApplicationContext(), new LoginRemoteFunction(username, password),
                    Requests.LOGIN, LoginFragment.this);
        }
    }

    @Override
    public void onCommunicationError(final ResponseError response) {
        dismissDialog(Dialogs.LOGIN_PROGRESS);
        if (response.getStatus() == HttpStatus.SC_UNAUTHORIZED)
            mListener.onLoginAuthenticationFailed();
        else
            mCommunicationModulePlugin.defaultResponseError(response);
    }

    @Override
    public void onCommunicationResponse(final int requestId,
                                        final ResponseObject response) {
        switch (requestId) {
            case Requests.LOGIN:
                mUserSession = JSONDecoder
                        .getObject(new UserSessionDescriptior(),
                                   response.getResponseText());

                CommuncationModule.sendAsyncRequest(
                        getApplicationContext(),
                        new GetSessionContext(
                                mUserSession),
                        Requests.GET_CONTEXT, this
                                                   );
                break;
            case Requests.GET_CONTEXT:
                mSessionContext = JSONDecoder
                        .getObject(new SessionContextDescriptor(),
                                   response.getResponseText());

                CommuncationModule.sendAsyncRequest(
                        getApplicationContext(),
                        new GetCourseDiscussions(
                                String.valueOf(mSessionContext.getSiteInfo().getFrontPageId()),
                                mUserSession),
                        Requests.GET_SITE_NEWS_FORUM, this
                                                   );
                break;
            case Requests.GET_SITE_NEWS_FORUM:
                dismissDialog(Dialogs.LOGIN_PROGRESS);
                mCommunicationModulePlugin.turnOffLoadingIcon();
                final List<CourseForum> newsForumForumList = JSONDecoder
                        .getObjectList(new CourseForumDescriptior(),
                                       response.getResponseText());

                if (newsForumForumList.size() > 0) {
                    // I'm expecting there to be only on Forum called 'Site News'
                    mSessionContext.getSiteInfo().setNewsForum(newsForumForumList.get(0));
                }

                mUserSession.setContext(mSessionContext);

                if (!ApplicationDataManager.saveLastUserSession(
                        getApplicationContext(),
                        mUserSession))
                    Log.i(getTag(), "Last Session Not Saved.");
                else
                    Log.i(getTag(), "Last session saved.");

                mListener.onLoginAuthenticationSuccess(mUserSession, response);
                break;
        }
    }

    @Override
    public DialogFragmentBase createDialog(final int id) {
        switch (id) {
            case Dialogs.LOGIN_PROGRESS:
                return new ProgressDialogFragment();
            default:
                return null;
        }
    }

    @Override
    public void onDestroy() {
        CommuncationModule.cancelAllRunningAsyncRequests(this);
        super.onDestroy();
    }

    private void dismissDialog(final int dialogId) {
        DialogManager.dismissDialog(dialogId, getFragmentManager());
    }

    private void showDialog(final int dialogId) {
        DialogManager.showDialog(dialogId, this, getFragmentManager());
    }

    /* ===================== Button Listeners ================ */
    private class LoginButtonListener implements OnClickListener {
        @Override
        public void onClick(final View v) {
            final String enteredUsername = mUsernameTextbox.getText().toString();
            final String enteredPassword = mPasswordTextBox.getText().toString();

            showDialog(Dialogs.LOGIN_PROGRESS);
            mCommunicationModulePlugin.turnOnLoadingIcon();

            CommuncationModule.sendAsyncRequest(
                    getApplicationContext(),
                    new LoginRemoteFunction(
                            enteredUsername, enteredPassword),
                    Requests.LOGIN, LoginFragment.this
                                               );
        }
    }

    /* ====================== Private classes ============== */

    public class DefaultLoginResponse implements Listener {
        @Override
        public void onLoginAuthenticationSuccess(final UserSession session,
                                                 final ResponseObject response) {
            Toast.makeText(getApplicationContext(),
                           "Login Successful", Toast.LENGTH_SHORT)
                 .show();

        }

        @Override
        public void onLoginAuthenticationFailed() {
            Toast.makeText(getApplicationContext(),
                           "Invalid Login. Please try again", Toast.LENGTH_SHORT)
                 .show();
        }

    }

    /* ===================== Interfaces ====================== */

    public interface Listener {

        public void onLoginAuthenticationSuccess(UserSession session,
                                                 ResponseObject response);

        public void onLoginAuthenticationFailed();
    }

    private interface Dialogs {
        int LOGIN_PROGRESS = 0;
    }

    private interface Requests {
        int LOGIN = 0;
        int GET_CONTEXT = 1;
        int GET_SITE_NEWS_FORUM = 2;
    }

}
