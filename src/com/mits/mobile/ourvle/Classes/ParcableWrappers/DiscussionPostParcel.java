/**
 * 
 */
package com.mits.mobile.ourvle.Classes.ParcableWrappers;

import org.joda.time.DateTime;
import org.sourceforge.ah.android.utilities.Formatters.DateFormatter;
import org.sourceforge.ah.android.utilities.Parcels.SimpleParcableWrapper;

import android.os.Parcel;
import android.os.Parcelable;

import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.DiscussionPost;

/**
 * The Class MoodleCourseParcel.
 * 
 * @author Aston Hamilton
 */
public class DiscussionPostParcel extends SimpleParcableWrapper<DiscussionPost> {

    public DiscussionPostParcel(final DiscussionPost wrappedObject) {
	super(wrappedObject);
    }

    public DiscussionPostParcel(final Parcel in) {
	super(in);
    }

    @Override
    protected DiscussionPost getObjectFromStream(final Parcel in) {
	final long id = in.readLong();
	final String pDiscussionId = in.readString();
	final String subject = in.readString();
	final String message = in.readString();
	final long parentId = in.readLong();
	final String createdDateTiemString = in.readString();
	final String lastModifiedDateTimeString = in.readString();
	final boolean hasAttachment = in.readInt() == 1;
	final MoodleUserParcel posterParcel = in
		.readParcelable(MoodleUserParcel.class.getClassLoader());

	final DateTime createdDateTime = DateFormatter
		.getDateTimeFromISOString(createdDateTiemString);
	final DateTime lastModifiedDateTime = DateFormatter
		.getDateTimeFromISOString(lastModifiedDateTimeString);

	return new DiscussionPost(id, subject, message, pDiscussionId, parentId,
		createdDateTime, lastModifiedDateTime,
		hasAttachment, posterParcel.getWrappedObejct());
    }

    @Override
    protected void writeObjectToParcel(
	    final DiscussionPost wrappedObject,
	    final Parcel parcel) {
	// Safer to explicitly cast to long and extract the primitive since
	// parcel don't play nice with autowrapped long's
	parcel.writeLong(((Long) wrappedObject.getId()).longValue());
	parcel.writeString(wrappedObject.getDiscussionId());
	parcel.writeString(wrappedObject.getSubject());
	parcel.writeString(wrappedObject.getMessage());
	parcel.writeLong(((Long) wrappedObject.getParentId()).longValue());
	parcel.writeString(DateFormatter.getISODateString(wrappedObject
		.getDateCreaded()));
	parcel.writeString(DateFormatter.getISODateString(wrappedObject
		.getDateLastModified()));
	parcel.writeInt(wrappedObject.isHasAttachment() ? 1 : 0);
	parcel.writeParcelable(
		new MoodleUserParcel(wrappedObject.getPoster()), 0);
    }

    /** The Constant CREATOR. */
    public static final Parcelable.Creator<DiscussionPostParcel> CREATOR =
	    new SimpleParcableWrapperCreator<DiscussionPostParcel>(
		    DiscussionPostParcel.class);

}
