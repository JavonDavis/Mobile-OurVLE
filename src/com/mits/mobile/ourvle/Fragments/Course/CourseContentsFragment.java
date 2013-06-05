/**
 * 
 */
package com.mits.mobile.ourvle.Fragments.Course;

import java.util.List;

import org.sourceforge.ah.android.utilities.Communication.CommuncationModule;
import org.sourceforge.ah.android.utilities.Communication.Interfaces.OnCommunicationResponseListener;
import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONDecoder;
import org.sourceforge.ah.android.utilities.Communication.Response.ResponseError;
import org.sourceforge.ah.android.utilities.Communication.Response.ResponseObject;
import org.sourceforge.ah.android.utilities.Plugins.DefaultCommunicationModulePlugin;
import org.sourceforge.ah.android.utilities.Plugins.DefaultCommunicationModulePlugin.OnReloadFragmentListener;
import org.sourceforge.ah.android.utilities.Widgets.Adapters.PinnedHeaderListAdapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mits.mobile.ourvle.R;
import com.mits.mobile.ourvle.Classes.SharedConstants.ParcelKeys;
import com.mits.mobile.ourvle.Classes.DataLayer.Authentication.Session.UserSession;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Courses.CourseSection;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Courses.MoodleCourse;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.CourseModule;
import com.mits.mobile.ourvle.Classes.ParcableWrappers.MoodleCourseParcel;
import com.mits.mobile.ourvle.Classes.TransportLayer.JSONDescriptors.Moodle.Courses.CourseSectionDescriptor;
import com.mits.mobile.ourvle.Classes.TransportLayer.RemoteAPIRequests.WebServiceFunctions.GetCourseContents;
import com.mits.mobile.ourvle.Fragments.Components.AuthenticatedListFragment;
import com.mits.mobile.ourvle.Fragments.Components.CourseListFragment.CourseModuleFactory;

/**
 * @author Aston Hamilton
 * 
 */
public class CourseContentsFragment extends AuthenticatedListFragment implements
	OnCommunicationResponseListener, OnReloadFragmentListener {

    private MoodleCourse mCourse;

    private PinnedHeaderListAdapter<CourseSection, CourseModule> mCourseModuleListAdapter;

    private Listener mListener;

    private DefaultCommunicationModulePlugin mCommunicatioModulePlugin;

    private Activity mActivity;

    private String mEmptyListString;

    public static CourseContentsFragment newInstance(
	    final UserSession session,
	    final MoodleCourse course) {
	final CourseContentsFragment f = new CourseContentsFragment();

	f.setUserSession(session);
	f.setMoodleCourse(course);

	return f;
    }

    public void setMoodleCourse(final MoodleCourse course) {
	getFragmentArguments().putParcelable(ParcelKeys.MOODLE_COURSE,
		new MoodleCourseParcel(course));
	mCourse = course;
    }

    @Override
    public void onAttach(final Activity activity) {
	super.onAttach(activity);
	mActivity = activity;
	try {
	    mListener = (Listener) activity;
	} catch (final ClassCastException e) {
	    mListener = new Listener() {

		@Override
		public void onCourseModuleSelected(final CourseModule module) {
		    Toast.makeText(mActivity,
			    "Selected " + module.getName(), Toast.LENGTH_LONG)
			    .show();
		}
	    };
	}
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {

	mCourse = ((MoodleCourseParcel) getFragmentArguments()
		.getParcelable(ParcelKeys.MOODLE_COURSE))
		.getWrappedObejct();

	mCommunicatioModulePlugin = new DefaultCommunicationModulePlugin(this);

	registerPlugin(mCommunicatioModulePlugin);

	mCourseModuleListAdapter = new CourseModuleListAdapter(mActivity);

	setListAdapter(mCourseModuleListAdapter);

	super.onCreate(savedInstanceState);

	mEmptyListString = getString(R.string.no_course_contents);
    }

    @Override
    public void onStart() {
	super.onStart();

	loadCourseContents();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
	    final ViewGroup container,
	    final Bundle savedInstanceState) {
	return inflater.inflate(R.layout.fragment_course_contents,
		container, false);
    }

    @Override
    public void onListItemClick(final ListView l, final View v,
	    final int position, final long id) {
	mListener.onCourseModuleSelected(
		mCourseModuleListAdapter.getItem(position));
    }

    @Override
    public void onCommunicationError(final ResponseError response) {
	mCommunicatioModulePlugin.defaultResponseError(response);
    }

    @Override
    public void onCommunicationResponse(final int requestId,
	    final ResponseObject response) {
	setEmptyText(mEmptyListString);
	switch (requestId) {
	case Requests.GET_COURSE_CONTENTS:
	    mCommunicatioModulePlugin.turnOffLoadingIcon();
	    final List<CourseSection> courseSectionList = JSONDecoder
		    .getObjectList(new CourseSectionDescriptor(),
			    response.getResponseText());

	    mCourseModuleListAdapter.clearPartitions();

	    for (final CourseSection section : courseSectionList)
		mCourseModuleListAdapter
			.addPartition(section, section.getModuleList());

	    mCourseModuleListAdapter.notifyDataSetChanged();
	    break;
	}
    }

    private void loadCourseContents() {
	mCommunicatioModulePlugin.turnOnLoadingIcon();
	setEmptyText("Loading contents....");
	CommuncationModule.sendAsyncRequest(
		mActivity,
		new GetCourseContents(
			getUserSession(), mCourse),
		Requests.GET_COURSE_CONTENTS, this);
    }

    @Override
    public void onCommunicationMenuItemTriggered() {
	loadCourseContents();
    }

    @Override
    public void onStop() {
	CommuncationModule.cancelAllRunningAsyncRequests(this);
	super.onStop();
    }

    /* ======================== Private CLasses ====================== */
    private static class CourseModuleListAdapter extends
	    PinnedHeaderListAdapter<CourseSection, CourseModule> {

	public CourseModuleListAdapter(final Context context) {
	    super(context, R.layout.list_header_simple_pinned,
		    R.layout.list_item_course_module_list);
	}

	class RowViewHolder implements SimpleViewHolder {
	    ImageView icon;
	    TextView firstLine;
	}

	@Override
	protected SimpleViewHolder getRowViewHolder(
		final View rowView) {
	    final RowViewHolder h = new RowViewHolder();
	    h.icon = (ImageView) rowView
		    .findViewById(R.id.imageview_indicator);
	    h.firstLine = (TextView) rowView
		    .findViewById(R.id.textview_first_line);
	    return h;
	}

	@Override
	protected void bindRowView(
		final CourseModule rowData,
		final SimpleViewHolder rowViewHolder) {

	    final RowViewHolder h = (RowViewHolder) rowViewHolder;
	    h.icon.setImageResource(CourseModuleFactory
		    .getSystemIconResource(rowData));

	    h.firstLine.setText(rowData.getLabel());
	}
    }

    /* ========================== Interfaces ======================= */

    public static interface Listener {
	public void onCourseModuleSelected(CourseModule module);
    }

    private static interface Requests {
	int GET_COURSE_CONTENTS = 0;

    }
}
