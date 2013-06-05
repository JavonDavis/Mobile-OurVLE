/**
 * 
 */
package com.mits.mobile.ourvle.Fragments.Components;

import org.sourceforge.ah.android.utilities.Plugins.BaseClass.PluggableListFragment;

import android.os.Bundle;

import com.mits.mobile.ourvle.Classes.DataLayer.Authentication.Session.UserSession;
import com.mits.mobile.ourvle.Classes.ParcableWrappers.UserSessionParcel;

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
}
