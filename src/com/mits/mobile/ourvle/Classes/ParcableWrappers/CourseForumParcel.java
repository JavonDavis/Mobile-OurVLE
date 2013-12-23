/**
 *
 */
package com.mits.mobile.ourvle.Classes.ParcableWrappers;

import org.joda.time.DateTime;
import org.sourceforge.ah.android.utilities.Formatters.DateFormatter;
import org.sourceforge.ah.android.utilities.Parcels.SimpleParcableWrapper;

import android.os.Parcel;
import android.os.Parcelable;

import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.CourseForum;

/**
 * @author Aston Hamilton
 */
public class CourseForumParcel extends SimpleParcableWrapper<CourseForum> {

    public CourseForumParcel(final CourseForum forum) {
        super(forum);
    }

    public CourseForumParcel(final Parcel in) {
        super(in);
    }

    @Override
    protected CourseForum getObjectFromStream(final Parcel in) {
        final Long forumId = in.readLong();
        final String pModuleId = in.readString();
        final String name = in.readString();
        final String intro = in.readString();
        final String lastModifiedString = in.readString();

        final DateTime lastModified = DateFormatter
                .getDateTimeFromISOString(lastModifiedString);
        return new CourseForum(forumId, pModuleId, name, intro, lastModified);
    }

    @Override
    protected void writeObjectToParcel(final CourseForum wrappedObject,
                                       final Parcel parcel) {
        parcel.writeLong(wrappedObject.getForumid().longValue());
        parcel.writeString(wrappedObject.getModuleId());
        parcel.writeString(wrappedObject.getName());
        parcel.writeString(wrappedObject.getIntro());
        parcel.writeString(DateFormatter.getISODateString(wrappedObject
                                                                  .getLastModified()));
    }

    /**
     * The Constant CREATOR.
     */
    public static final Parcelable.Creator<CourseForumParcel> CREATOR =
            new SimpleParcableWrapperCreator<CourseForumParcel>(
                    CourseForumParcel.class);
}
