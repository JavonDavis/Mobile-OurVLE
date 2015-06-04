/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Fragments.Course;

import org.sourceforge.ah.android.utilities.Communication.CommuncationModule;
import org.sourceforge.ah.android.utilities.Communication.Interfaces.OnCommunicationResponseListener;
import org.sourceforge.ah.android.utilities.Communication.Response.ResponseError;
import org.sourceforge.ah.android.utilities.Communication.Response.ResponseObject;
import org.sourceforge.ah.android.utilities.Plugins.DefaultCommunicationModulePlugin;
import org.sourceforge.ah.android.utilities.Plugins.DefaultCommunicationModulePlugin.OnReloadFragmentListener;
import org.sourceforge.ah.android.utilities.Widgets.Adapters.PinnedHeaderListAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import edu.uwi.mona.mobileourvle.app.R;
import edu.uwi.mona.mobileourvle.app.Activities.ViewUserProfileActivity;
import edu.uwi.mona.mobileourvle.app.Classes.SharedConstants.ParcelKeys;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.UserSession;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Courses.ExtendedMoodleCourse;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Courses.MoodleCourse;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Users.MoodleUser;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.MoodleCourseParcel;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.MoodleUserParcel;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.UserSessionParcel;
import edu.uwi.mona.mobileourvle.app.Fragments.Components.AuthenticatedListFragment;

/**
 * @author Aston Hamilton
 * 
 *         This fragment is incomplete but will be completed as progress on the
 *         other fragments continue since it will intelligently pull relevant
 *         information from the data maintained by the other fragments that is
 *         relevant to the current context,
 * 
 *         For now it serves as a placaeholder for a landing page of the course
 *         contents fragment pager
 */
public class CourseOverviewFragment extends AuthenticatedListFragment implements
	OnCommunicationResponseListener, OnReloadFragmentListener {

    private MoodleCourse mCourse;

    private PinnedHeaderListAdapter<String, ExtendedMoodleCourse> mListAdapter;

    private Activity mActivity;

    private TextView mInProgressIndicatorTextView;
    private TextView mMainInstructorTextView;
    private ImageButton mMainTinstructorProfileImageButton;

    private DefaultCommunicationModulePlugin mCommunicationModulePlugin;

    private MoodleUser mMainInstructor;

    public static CourseOverviewFragment newInstance(
	    final UserSession userSession, final MoodleCourse course) {
	final CourseOverviewFragment f = new CourseOverviewFragment();

	f.setUserSession(userSession);
	f.setMooleCourse(course);

	return f;
    }

    public void setMooleCourse(final MoodleCourse course) {
	getFragmentArguments().putParcelable(ParcelKeys.MOODLE_COURSE,
		new MoodleCourseParcel(course));
	mCourse = course;
    }

    @Override
    public void onAttach(final Activity activity) {
	super.onAttach(activity);
	mActivity = activity;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
	mCourse = ((MoodleCourseParcel) getFragmentArguments()
		.getParcelable(ParcelKeys.MOODLE_COURSE))
		.getWrappedObejct();

	mCommunicationModulePlugin = new DefaultCommunicationModulePlugin(this);

	registerPlugin(mCommunicationModulePlugin);

	setListAdapter(mListAdapter);

	super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
	    final ViewGroup container,
	    final Bundle savedInstanceState) {
	final View v = inflater
		.inflate(R.layout.fragment_course_overview, container, false);

	mInProgressIndicatorTextView = (TextView) v
		.findViewById(R.id.textview_in_progress);
	mMainInstructorTextView = (TextView) v
		.findViewById(R.id.textview_instructor);
	mMainTinstructorProfileImageButton = (ImageButton) v
		.findViewById(R.id.imagebutton_profile);

	mMainTinstructorProfileImageButton
		.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(final View v) {
			final Intent i = new Intent(mActivity,
				ViewUserProfileActivity.class);
			i.putExtra(ParcelKeys.USER_SESSION,
				new UserSessionParcel(getUserSession()));
			i.putExtra(ParcelKeys.MOODLE_USER,
				new MoodleUserParcel(mMainInstructor));

			startActivity(i);
		    }
		});
	return v;
    }

    @Override
    public void onStart() {
	super.onStart();
	loadCourseActivityOverview();
    }

    @Override
    public void onListItemClick(final ListView l, final View v,
	    final int position, final long id) {

	// Do nothing
    }

    @Override
    public void onCommunicationMenuItemTriggered() {
	loadCourseActivityOverview();
    }

    @Override
    public void onCommunicationError(final ResponseError response) {
	mCommunicationModulePlugin.defaultResponseError(response);

    }

    @Override
    public void onCommunicationResponse(final int requestId,
	    final ResponseObject response) {
	if (requestId == Requests.GET_COURSE_MANAGERS) {

	}

    }

    @Override
    public void onStop() {
	CommuncationModule.cancelAllRunningAsyncRequests(this);
	super.onStop();
    }

    private void loadCourseActivityOverview() {

	mMainInstructor = getUserSession().getContext().getCurretnUser();

	mMainInstructorTextView.setText(mMainInstructor.getFullName());
    }

    /* ========================== Interfaces ======================= */
    private interface Requests {
	int GET_COURSE_MANAGERS = 0;
    }

}
