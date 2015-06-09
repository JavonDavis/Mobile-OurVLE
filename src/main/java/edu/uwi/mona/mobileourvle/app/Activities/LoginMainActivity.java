package edu.uwi.mona.mobileourvle.app.Activities;

import org.sourceforge.ah.android.utilities.Communication.EntitySyncroniser.EntitySyncronizer;
import org.sourceforge.ah.android.utilities.Communication.Response.ResponseObject;
import org.sourceforge.ah.android.utilities.Cryptography.AESUtil;
import org.sourceforge.ah.android.utilities.Cryptography.CryptographyUtil;
import org.sourceforge.ah.android.utilities.Widgets.Activities.ActivityBase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import edu.uwi.mona.mobileourvle.app.R;
import edu.uwi.mona.mobileourvle.app.Classes.SharedConstants.ParcelKeys;
import edu.uwi.mona.mobileourvle.app.Classes.SharedConstants.SharedContract;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.UserSession;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.UserSessionParcel;
import edu.uwi.mona.mobileourvle.app.Fragments.LoginFragment;
import edu.uwi.mona.mobileourvle.app.Fragments.LoginFragment.DefaultLoginResponse;

/**
 * The Class MainActivity.
 */
public class LoginMainActivity extends ActivityBase implements
        LoginFragment.Listener {

    public final static String SAVED_LOGIN_PREFERENCES_NAME = "lma-al";
    public final static String USERNAME_KEY = "lma-al-un";
    public final static String PASSWORD_KEY = "lma-al-sk";
    public final static String ENCRYPTION_KEY = "lma-al-ek";

    private DefaultLoginResponse mDefaultResponse;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        setTitle(R.string.title_login);

        EntitySyncronizer.setupEntitySyncronizer(
                getApplicationContext(),
                SharedContract.SYNCRONIZATION_PERMISSION,
                0, 5 * 60 * 1000);

        final LoginFragment fragment = LoginFragment.newInstance();

        final android.support.v4.app.FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this
        // fragment,
        transaction.replace(R.id.fragment, fragment);

        // Commit the transaction
        transaction.commit();

        mDefaultResponse = fragment.new DefaultLoginResponse();

    }

    @Override
    public void onLoginAuthenticationSuccess(final UserSession session,
                                             final ResponseObject response) {
        final Intent intent = new Intent(LoginMainActivity.this, HomeActivity.class);

        intent.putExtra(ParcelKeys.USER_SESSION, new UserSessionParcel(session));

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        final EditText usernameTextbox = (EditText) findViewById(R.id.id_number_field);

        if (usernameTextbox != null) { // MY flag to test if the auto login was being ran
            final EditText passwordTextBox = (EditText) findViewById(R.id.password_field);

            final SharedPreferences preferences = getSharedPreferences(
                    SAVED_LOGIN_PREFERENCES_NAME, MODE_PRIVATE);
            final String privateEncryptionKey;

            privateEncryptionKey = CryptographyUtil.generateSecureNonce();

            final String userNameHash = AESUtil.encryptAESString(
                    privateEncryptionKey, usernameTextbox.getText().toString());
            final String passwordHash = AESUtil.encryptAESString(
                    privateEncryptionKey, passwordTextBox.getText().toString());

            // If the encryption fails for any reason a log is printed and an empty
            // string is returned
            if (!userNameHash.isEmpty() && !passwordHash.isEmpty()) {
                preferences.edit()
                        .putString(ENCRYPTION_KEY, privateEncryptionKey)
                        .putString(USERNAME_KEY, userNameHash)
                        .putString(PASSWORD_KEY, passwordHash)
                        .commit();

            } else {
                Toast.makeText(getApplicationContext(),
                        "Could not save credentials", Toast.LENGTH_SHORT)
                        .show();
            }

        }


        startActivity(intent);
    }

    @Override
    public void onLoginAuthenticationFailed() {
        mDefaultResponse.onLoginAuthenticationFailed();
    }

}