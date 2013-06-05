/**
 * 
 */
package com.mits.mobile.ourvle.Classes.ParcableWrappers;

import org.sourceforge.ah.android.utilities.Parcels.SimpleParcableWrapper;

import android.os.Parcel;
import android.os.Parcelable;

import com.mits.mobile.ourvle.Classes.DataLayer.Authentication.Session.UserSession;

/**
 * The Class UserSessionParcel.
 * 
 * @author Aston Hamilton
 */
public class UserSessionParcel extends
	SimpleParcableWrapper<UserSession> {

    public UserSessionParcel(final UserSession session) {
	super(session);
    }

    public UserSessionParcel(final Parcel in) {
	super(in);
    }

    @Override
    protected UserSession getObjectFromStream(final Parcel in) {
	final String sessionToken = in.readString();

	final SessionContextParcel sessionContextParcel = in
		.readParcelable(SessionContextParcel.class.getClassLoader());

	return new UserSession(sessionToken,
		sessionContextParcel.getWrappedObejct());
    }

    @Override
    protected void writeObjectToParcel(final UserSession wrappedObject,
	    final Parcel parcel) {
	parcel.writeString(wrappedObject.getSessionKey());
	parcel.writeParcelable(
		new SessionContextParcel(wrappedObject.getContext()), 0);
    }

    /** The Constant CREATOR. */
    public static final Parcelable.Creator<UserSessionParcel> CREATOR =
	    new SimpleParcableWrapperCreator<UserSessionParcel>(
		    UserSessionParcel.class);
}
