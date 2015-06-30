/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Fragments.Components;

import org.sourceforge.ah.android.utilities.Plugins.BaseClass.PluggableListFragment;

import android.os.Bundle;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.UserSession;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.UserSessionParcel;

/**
 * @author Aston Hamilton
 * 
 */
public class AuthenticatedListFragment extends PluggableListFragment {
    private UserSession mSession;

    private static final String ARG_USER_SESSION = "session";

    public UserSession getUserSession() {
	return mSession;
    }

    protected void setUserSession(final UserSession session) {
	getFragmentArguments().putParcelable(
		AuthenticatedListFragment.ARG_USER_SESSION,
		new UserSessionParcel(session));
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
	mSession = ((UserSessionParcel) getFragmentArguments()
		.getParcelable(AuthenticatedListFragment.ARG_USER_SESSION))
		.getWrappedObejct();
	super.onCreate(savedInstanceState);
    }

    public Bundle getFragmentArguments() {
        if(super.getArguments() == null) {
            this.setArguments(new Bundle());
        }

        return super.getArguments();
    }
}
