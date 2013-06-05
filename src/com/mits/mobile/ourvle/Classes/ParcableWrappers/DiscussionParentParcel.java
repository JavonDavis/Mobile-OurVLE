/**
 * 
 */
package com.mits.mobile.ourvle.Classes.ParcableWrappers;

import org.sourceforge.ah.android.utilities.Parcels.SimpleParcableWrapper;

import android.os.Parcel;
import android.os.Parcelable;

import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.DiscussionParent;

// TODO: Auto-generated Javadoc
/**
 * The Class MoodleCourseParcel.
 * 
 * @author Aston Hamilton
 */
public class DiscussionParentParcel extends
	SimpleParcableWrapper<DiscussionParent> {

    public DiscussionParentParcel(final DiscussionParent parent) {
	super(parent);
    }

    public DiscussionParentParcel(final Parcel in) {
	super(in);
    }

    @Override
    protected DiscussionParent getObjectFromStream(final Parcel in) {

	final int type = in.readInt();

	if (type == 1)
	    return new DiscussionParent(
		    ((CourseForumParcel) in
			    .readParcelable(
			    CourseForumParcel.class.getClassLoader()))
			    .getWrappedObejct());
	else if (type == 2)
	    return new DiscussionParent(
		    ((CourseModuleParcel) in
			    .readParcelable(
			    CourseModuleParcel.class.getClassLoader()))
			    .getWrappedObejct());
	else
	    throw new IllegalArgumentException(
		    "Discussion parent not fourm or module,");
    }

    @Override
    protected void writeObjectToParcel(
	    final DiscussionParent parent,
	    final Parcel parcel) {
	if (parent.isForum()) {
	    parcel.writeInt(1);
	    parcel.writeParcelable(new CourseForumParcel(parent.getForum()), 0);
	} else if (parent.isModule()) {
	    parcel.writeInt(2);
	    parcel.writeParcelable(new CourseModuleParcel(parent.getModule()),
		    0);
	} else
	    throw new IllegalArgumentException(
		    "Discussion parent not fourm or module,");
    }

    /** The Constant CREATOR. */
    public static final Parcelable.Creator<DiscussionParentParcel> CREATOR =
	    new SimpleParcableWrapperCreator<DiscussionParentParcel>(
		    DiscussionParentParcel.class);

}
