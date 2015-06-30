/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers;

import org.sourceforge.ah.android.utilities.Parcels.SimpleParcableWrapper;

import android.os.Parcel;
import android.os.Parcelable;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Courses.ExtendedMoodleCourse;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Users.CourseManager;

// TODO: Auto-generated Javadoc
/**
 * The Class MoodleCourseParcel.
 * 
 * @author Aston Hamilton
 */
public class ExtendedMoodleCourseParcel extends
	SimpleParcableWrapper<ExtendedMoodleCourse> {

    public ExtendedMoodleCourseParcel(final ExtendedMoodleCourse course) {
	super(course);
    }

    public ExtendedMoodleCourseParcel(final Parcel in) {
	super(in);
    }

    @Override
    protected ExtendedMoodleCourse getObjectFromStream(final Parcel in) {

	final long courseId = in.readLong();
	final String fullName = in.readString();
		final String shortName = in.readString();

	final int numManagers = in.readInt();
	final CourseManager[] managerList = new CourseManager[numManagers];

	for (int i = 0; i < numManagers; i++) {
	    final String managerUserId = in.readString();
	    final String managerFirstName = in.readString();
	    final String managerLastName = in.readString();
	    final String managerPictureUrl = in.readString();

	    final String managerRoleId = in.readString();
	    final String managerRoleName = in.readString();

	    final CourseManager manager = new CourseManager(managerUserId,
		    managerFirstName, managerLastName, managerPictureUrl,
		    managerRoleId, managerRoleName);

	    managerList[i] = manager;
	}

	return new ExtendedMoodleCourse(courseId, fullName,shortName, managerList);
    }

    @Override
    protected void writeObjectToParcel(
	    final ExtendedMoodleCourse wrappedObject,
	    final Parcel parcel) {
	parcel.writeLong(wrappedObject.getId().longValue());
	parcel.writeString(wrappedObject.getName());

	parcel.writeInt(wrappedObject.getManagers().length);

	for (final CourseManager manager : wrappedObject.getManagers()) {
	    parcel.writeString(manager.getId());
	    parcel.writeString(manager.getFirstName());
	    parcel.writeString(manager.getLastName());
	    parcel.writeString(manager.getPictureUrl());

	    parcel.writeString(manager.getRoleId());
	    parcel.writeString(manager.getRoleName());

	}
    }

    /** The Constant CREATOR. */
    public static final Parcelable.Creator<ExtendedMoodleCourseParcel> CREATOR =
	    new SimpleParcableWrapperCreator<ExtendedMoodleCourseParcel>(
		    ExtendedMoodleCourseParcel.class);

}
