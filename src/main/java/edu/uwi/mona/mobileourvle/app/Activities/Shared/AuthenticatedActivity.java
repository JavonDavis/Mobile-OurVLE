/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Activities.Shared;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import edu.uwi.mona.mobileourvle.app.Classes.SharedConstants.ParcelKeys;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.UserSession;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.UserSessionParcel;

/**
 * The Class AuthenticatedActivity.
 * 
 * @author Aston Hamilton
 */
public class AuthenticatedActivity extends Activity {

    /** The session. */
    private UserSession session;

    /** The Constant LOGIN_REQUEST. */
    public static final int LOGIN_REQUEST = -1;

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onStart()
     */
    @Override
    public void onStart() {
	super.onStart();
	final Intent intent = new Intent(this, LoginPromptActivity.class);

	startActivityForResult(intent, AuthenticatedActivity.LOGIN_REQUEST);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onActivityResult(int, int,
     * android.content.Intent)
     */
    @Override
    protected void onActivityResult(final int requestCode,
	    final int resultCode, final Intent data) {
	switch (requestCode) {
	case LOGIN_REQUEST:
	    if (resultCode == Activity.RESULT_OK) {
		final Bundle extras = data.getExtras();

		session = ((UserSessionParcel) extras
			.get(ParcelKeys.USER_SESSION)).getWrappedObejct();
	    }
	}
    }

    /**
     * Gets the user session.
     * 
     * @return the user session
     */
    protected UserSession getUserSession() {
	return session;
    }
}
