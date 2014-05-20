/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers;

import org.sourceforge.ah.android.utilities.Parcels.SimpleParcableWrapper;

import android.os.Parcel;
import android.os.Parcelable;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Courses.MoodleCourse;

/**
 * The Class MoodleCourseParcel.
 * 
 * @author Aston Hamilton
 */
public class MoodleCourseParcel extends SimpleParcableWrapper<MoodleCourse> {

    public MoodleCourseParcel(final MoodleCourse wrappedObject) {
	super(wrappedObject);
    }

    public MoodleCourseParcel(final Parcel in) {
	super(in);
    }

    @Override
    protected MoodleCourse getObjectFromStream(final Parcel in) {
	final long courseId = in.readLong();
	final String fullName = in.readString();

	return new MoodleCourse(courseId, fullName);
    }

    @Override
    protected void writeObjectToParcel(
	    final MoodleCourse wrappedObject,
	    final Parcel parcel) {
	parcel.writeLong(wrappedObject.getId().longValue());
	parcel.writeString(wrappedObject.getName());
    }

    /** The Constant CREATOR. */
    public static final Parcelable.Creator<MoodleCourseParcel> CREATOR =
	    new SimpleParcableWrapperCreator<MoodleCourseParcel>(
		    MoodleCourseParcel.class);

}
