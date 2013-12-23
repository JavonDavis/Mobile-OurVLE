/**
 *
 */
package com.mits.mobile.ourvle.Classes.ParcableWrappers;

import org.sourceforge.ah.android.utilities.Parcels.SimpleParcableWrapper;

import android.os.Parcel;
import android.os.Parcelable;

import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.CourseModule;

/**
 * The Class MoodleCourseParcel.
 *
 * @author Aston Hamilton
 */
public class CourseModuleParcel extends SimpleParcableWrapper<CourseModule> {

    public CourseModuleParcel(final CourseModule wrappedObject) {
        super(wrappedObject);
    }

    public CourseModuleParcel(final Parcel in) {
        super(in);
    }

    @Override
    protected CourseModule getObjectFromStream(final Parcel in) {
        final long id = in.readLong();
        final String pCourseId = in.readString();
        final String label = in.readString();
        final String name = in.readString();

        return new CourseModule(id, pCourseId, label, name);
    }

    @Override
    protected void writeObjectToParcel(
            final CourseModule wrappedObject,
            final Parcel parcel) {
        parcel.writeLong(wrappedObject.getId().longValue());
        parcel.writeString(wrappedObject.getCourseId());
        parcel.writeString(wrappedObject.getLabel());
        parcel.writeString(wrappedObject.getName());
    }

    /**
     * The Constant CREATOR.
     */
    public static final Parcelable.Creator<CourseModuleParcel> CREATOR =
            new SimpleParcableWrapperCreator<CourseModuleParcel>(
                    CourseModuleParcel.class);

}
