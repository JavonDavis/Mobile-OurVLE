/**
 * 
 */
package com.mits.mobile.ourvle.Classes.ParcableWrappers;

import org.sourceforge.ah.android.utilities.Parcels.SimpleParcableWrapper;

import android.os.Parcel;
import android.os.Parcelable;

import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Users.MoodleUser;

/**
 * @author Aston Hamilton
 * 
 */
public class MoodleUserParcel extends SimpleParcableWrapper<MoodleUser> {

    public MoodleUserParcel(final MoodleUser user) {
	super(user);
    }

    public MoodleUserParcel(final Parcel in) {
	super(in);
    }

    @Override
    protected MoodleUser getObjectFromStream(final Parcel in) {
	final String userId = in.readString();
	final String firstName = in.readString();
	final String lastName = in.readString();
	final String picUrl = in.readString();

	return new MoodleUser(userId, firstName, lastName, picUrl);
    }

    @Override
    protected void writeObjectToParcel(final MoodleUser wrappedObject,
	    final Parcel parcel) {
	parcel.writeString(wrappedObject.getId());
	parcel.writeString(wrappedObject.getFirstName());
	parcel.writeString(wrappedObject.getLastName());
	parcel.writeString(wrappedObject.getPictureUrl());
    }

    /** The Constant CREATOR. */
    public static final Parcelable.Creator<MoodleUserParcel> CREATOR =
	    new SimpleParcableWrapperCreator<MoodleUserParcel>(
		    MoodleUserParcel.class);

}
