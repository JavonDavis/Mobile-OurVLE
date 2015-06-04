/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers;

import org.joda.time.DateTime;
import org.sourceforge.ah.android.utilities.Formatters.DateFormatter;
import org.sourceforge.ah.android.utilities.Parcels.SimpleParcableWrapper;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.DiscussionParent;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.ForumDiscussion;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Users.MoodleUser;

/**
 * @author Aston Hamilton
 * 
 */
public class ForumDiscussionParcel extends
	SimpleParcableWrapper<ForumDiscussion> {

    public ForumDiscussionParcel(final ForumDiscussion discussion) {
	super(discussion);
    }

    public ForumDiscussionParcel(final Parcel in) {
	super(in);

    }

    @Override
    protected ForumDiscussion getObjectFromStream(final Parcel in) {
	final long discussionId = in.readLong();
	final String name = in.readString();

	final MoodleUser creator = ((MoodleUserParcel) in.readParcelable(
		MoodleUserParcel.class.getClassLoader()))
		.getWrappedObejct();
	final String lastModifiedString = in.readString();

	final DateTime lastModified = DateFormatter
		.getDateTimeFromISOString(lastModifiedString);

	final DiscussionParent parent = ((DiscussionParentParcel) in
		.readParcelable(
		DiscussionParentParcel.class.getClassLoader()))
		.getWrappedObejct();

        return new ForumDiscussion(discussionId, name, creator,
		lastModified, parent);
    }

    @Override
    protected void writeObjectToParcel(final ForumDiscussion wrappedObject,
	    final Parcel parcel) {
	parcel.writeLong(wrappedObject.getId().longValue());
	parcel.writeString(wrappedObject.getName());
	parcel.writeParcelable(
		new MoodleUserParcel(wrappedObject.getCreator()), 0);
	parcel.writeString(DateFormatter.getISODateString(wrappedObject
		.getLastModified()));
	parcel.writeParcelable(
		new DiscussionParentParcel(wrappedObject.getParent()), 0);

    }

    /** The Constant CREATOR. */
    public static final Parcelable.Creator<ForumDiscussionParcel> CREATOR =
	    new SimpleParcableWrapperCreator<ForumDiscussionParcel>(
		    ForumDiscussionParcel.class);

}
