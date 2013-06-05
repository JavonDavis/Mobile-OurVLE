package com.mits.mobile.ourvle.Activities;

import org.sourceforge.ah.android.utilities.Communication.EntitySyncroniser.EntitySyncronizer;
import org.sourceforge.ah.android.utilities.Communication.Response.ResponseObject;
import org.sourceforge.ah.android.utilities.Widgets.Activities.ActivityBase;

import android.content.Intent;
import android.os.Bundle;

import com.mits.mobile.ourvle.R;
import com.mits.mobile.ourvle.Classes.SharedConstants.ParcelKeys;
import com.mits.mobile.ourvle.Classes.SharedConstants.SharedContract;
import com.mits.mobile.ourvle.Classes.DataLayer.Authentication.Session.UserSession;
import com.mits.mobile.ourvle.Classes.ParcableWrappers.UserSessionParcel;
import com.mits.mobile.ourvle.Fragments.LoginFragment;
import com.mits.mobile.ourvle.Fragments.LoginFragment.DefaultLoginResponse;

/**
 * The Class MainActivity.
 */
public class LoginMainActivity extends ActivityBase implements
	LoginFragment.Listener {
    /**
     * Called when the activity is first created.
     * 
     * @param savedInstanceState
     *            the saved instance state
     */

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
	final Intent intent = new Intent(LoginMainActivity.this,
		CourseListActivity.class);

	intent.putExtra(ParcelKeys.USER_SESSION,
		new UserSessionParcel(session));

	startActivity(intent);
    }

    @Override
    public void onLoginAuthenticationFailed() {
	mDefaultResponse.onLoginAuthenticationFailed();
    }

}