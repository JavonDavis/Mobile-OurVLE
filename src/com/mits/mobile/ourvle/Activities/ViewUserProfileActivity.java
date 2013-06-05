/**
 * 
 */
package com.mits.mobile.ourvle.Activities;

import org.sourceforge.ah.android.utilities.Widgets.Activities.ActivityBase;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.mits.mobile.ourvle.R;
import com.mits.mobile.ourvle.Classes.SharedConstants.ParcelKeys;
import com.mits.mobile.ourvle.Classes.DataLayer.Authentication.Session.UserSession;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Courses.MoodleCourse;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Users.MoodleUser;
import com.mits.mobile.ourvle.Classes.ParcableWrappers.MoodleCourseParcel;
import com.mits.mobile.ourvle.Classes.ParcableWrappers.MoodleUserParcel;
import com.mits.mobile.ourvle.Classes.ParcableWrappers.UserSessionParcel;
import com.mits.mobile.ourvle.Fragments.MoodleUser.ViewProfileFragment;

/**
 * @author Aston Hamilton
 * 
 */
public class ViewUserProfileActivity extends ActivityBase {

    private UserSession mUserSession;
    private MoodleUser mUser;
    private MoodleCourse mCourse;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_view_user_profile);

	final Bundle extras = getIntent().getExtras();

	mUserSession = ((UserSessionParcel) extras
		.get(ParcelKeys.USER_SESSION)).getWrappedObejct();

	mUser = ((MoodleUserParcel) extras
		.get(ParcelKeys.MOODLE_USER)).getWrappedObejct();
	// set the action bar title

	if (extras.containsKey(ParcelKeys.MOODLE_COURSE))
	    mCourse = ((MoodleCourseParcel) extras
		    .get(ParcelKeys.MOODLE_COURSE)).getWrappedObejct();
	else
	    mCourse = null;

	setTitle(mUser.getFullName());

	final ViewProfileFragment fragment = ViewProfileFragment
		.newInstance(mUserSession, mUser, mCourse);

	final FragmentTransaction transaction = getSupportFragmentManager()
		.beginTransaction();

	// Replace whatever is in the fragment_container view with this
	// fragment,
	transaction.replace(R.id.fragment, fragment);

	// Commit the transaction
	transaction.commit();

    }

}
