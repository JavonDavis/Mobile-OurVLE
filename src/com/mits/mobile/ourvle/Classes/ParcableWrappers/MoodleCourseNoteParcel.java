/**
 * 
 */
package com.mits.mobile.ourvle.Classes.ParcableWrappers;

import org.joda.time.DateTime;
import org.sourceforge.ah.android.utilities.Formatters.DateFormatter;
import org.sourceforge.ah.android.utilities.Parcels.SimpleParcableWrapper;

import android.os.Parcel;
import android.os.Parcelable;

import com.mits.mobile.ourvle.Classes.DataLayer.CompanionEntities.CourseNote;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Courses.MoodleCourse;

/**
 * The Class MoodleCourseParcel.
 * 
 * @author Aston Hamilton
 */
public class MoodleCourseNoteParcel extends SimpleParcableWrapper<CourseNote> {

    public MoodleCourseNoteParcel(final CourseNote wrappedObject) {
	super(wrappedObject);
    }

    public MoodleCourseNoteParcel(final Parcel in) {
	super(in);
    }

    @Override
    protected CourseNote getObjectFromStream(final Parcel in) {
	final long noteId = in.readLong();
	final String noteText = in.readString();
	final long dateTimeUnixSeconds = in.readLong();
	final MoodleCourseParcel noteCourseParcel = in
		.readParcelable(MoodleCourseParcel.class.getClassLoader());

	final MoodleCourse noteCourse = noteCourseParcel.getWrappedObejct();
	final DateTime noteDateTaken = DateFormatter
		.getDateTimeFromUnixSeconds(dateTimeUnixSeconds);

	return new CourseNote(noteId, noteCourse, noteText, noteDateTaken);
    }

    @Override
    protected void writeObjectToParcel(
	    final CourseNote wrappedObject,
	    final Parcel parcel) {
	parcel.writeLong(wrappedObject.getNoteId().longValue());
	parcel.writeString(wrappedObject.getNote());
	parcel.writeLong(DateFormatter.getUnixSecondsFromDateTime(wrappedObject
		.getDateTaken()));
	parcel.writeParcelable(
		new MoodleCourseParcel(wrappedObject.getCourse()), 0);
    }

    /* The Constant CREATOR. */
    public static final Parcelable.Creator<MoodleCourseNoteParcel> CREATOR =
	    new SimpleParcableWrapperCreator<MoodleCourseNoteParcel>(
		    MoodleCourseNoteParcel.class);

}
