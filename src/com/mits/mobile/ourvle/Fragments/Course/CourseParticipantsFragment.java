/**
 * 
 */
package com.mits.mobile.ourvle.Fragments.Course;

import java.util.List;

import org.sourceforge.ah.android.utilities.AndroidUtil.AsyncManager;
import org.sourceforge.ah.android.utilities.AndroidUtil.ManagedAsyncTask;
import org.sourceforge.ah.android.utilities.Communication.CommuncationModule;
import org.sourceforge.ah.android.utilities.Communication.Interfaces.OnCommunicationResponseListener;
import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONDecoder;
import org.sourceforge.ah.android.utilities.Communication.Request.RequestObject;
import org.sourceforge.ah.android.utilities.Communication.Response.ResponseError;
import org.sourceforge.ah.android.utilities.Communication.Response.ResponseObject;
import org.sourceforge.ah.android.utilities.Databases.SQLiteDatabaseWrapper;
import org.sourceforge.ah.android.utilities.Plugins.DefaultCommunicationModulePlugin;
import org.sourceforge.ah.android.utilities.Plugins.DefaultCommunicationModulePlugin.OnReloadFragmentListener;
import org.sourceforge.ah.android.utilities.Widgets.Adapters.DynamicViewAdapter;
import org.sourceforge.ah.android.utilities.Widgets.Adapters.DynamicViewAdapter.SimpleViewHolder;
import org.sourceforge.ah.android.utilities.Widgets.Util.RemoteImageViewLoader;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import com.mits.mobile.ourvle.R;
import com.mits.mobile.ourvle.Classes.SharedConstants.ParcelKeys;
import com.mits.mobile.ourvle.Classes.DataLayer.Android.PhoneContact;
import com.mits.mobile.ourvle.Classes.DataLayer.Authentication.Session.UserSession;
import com.mits.mobile.ourvle.Classes.DataLayer.Databases.Wrappers.MoodleUserContactDbWrapper;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Courses.MoodleCourse;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Users.MoodleUser;
import com.mits.mobile.ourvle.Classes.ParcableWrappers.MoodleCourseParcel;
import com.mits.mobile.ourvle.Classes.TransportLayer.JSONDescriptors.Moodle.Users.DefaultMoodleUserDescriptor;
import com.mits.mobile.ourvle.Classes.TransportLayer.RemoteAPIRequests.WebServiceFunctions.GetCourseParticipants;
import com.mits.mobile.ourvle.Fragments.Components.AuthenticatedListFragment;

/**
 * @author Aston Hamilton
 * 
 */
public class CourseParticipantsFragment extends AuthenticatedListFragment
	implements
	OnCommunicationResponseListener, OnReloadFragmentListener {

    private MoodleCourse mCourse;

    private CourseParticipantViewPopulator mViewPopulator;
    private DynamicViewAdapter<MoodleUser> mListAdapter;

    private Listener mListener;

    private DefaultCommunicationModulePlugin mCommunicationModulePlugin;

    private MoodleUserContactDbWrapper mDb;

    private Activity mActivity;
    private CharSequence mEmptyListString;

    private final String sComponentUri;
    protected boolean fLoadingDb;

    /**
     * 
     */
    public CourseParticipantsFragment() {
	super();
	sComponentUri = this.getClass().getName();
    }

    public static CourseParticipantsFragment newInstance(
	    final UserSession session,
	    final MoodleCourse course) {
	final CourseParticipantsFragment f = new CourseParticipantsFragment();

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
	mActivity = activity;
	super.onAttach(activity);
	try {
	    mListener = (Listener) activity;
	} catch (final ClassCastException e) {
	    mListener = new Listener() {

		@Override
		public void onParticipantSelected(final MoodleUser user) {
		    Toast.makeText(mActivity,
			    "Selected " + user.getFullName(), Toast.LENGTH_LONG)
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

	mDb = new MoodleUserContactDbWrapper(mActivity);

	mViewPopulator = new CourseParticipantViewPopulator(mDb, mActivity);
	mListAdapter =
		new DynamicViewAdapter<MoodleUser>(
			mActivity,
			R.layout.list_item_course_participant,
			mViewPopulator);

	setListAdapter(mListAdapter);

	mCommunicationModulePlugin = new DefaultCommunicationModulePlugin(this);
	registerPlugin(mCommunicationModulePlugin);
	super.onCreate(savedInstanceState);

	mEmptyListString = getString(R.string.no_participants);
    }

    @Override
    public void onStart() {
	super.onStart();
	loadCourseParticipants();
    }

    @Override
    public void onResume() {
	super.onResume();
	new ManagedAsyncTask<Object, Void, Void>(sComponentUri) {

	    @Override
	    protected void onPreExecute() {
		fLoadingDb = true;

		super.onPreExecute();
	    };

	    @Override
	    protected Void doInBackground(final Object... params) {
		((SQLiteDatabaseWrapper) params[0]).loadDatabase();
		return null;
	    }

	    @Override
	    protected void onPostExecute(final Void result) {
		fLoadingDb = false;

		mListAdapter.notifyDataSetChanged();
		super.onPostExecute(result);
	    };
	}.execute(mDb);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
	    final ViewGroup container,
	    final Bundle savedInstanceState) {
	return inflater.inflate(R.layout.fragment_course_participants_list,
		container,
		false);
    }

    @Override
    public void onListItemClick(final ListView l, final View v,
	    final int position, final long id) {
	mListener.onParticipantSelected(
		mListAdapter.getItem(position));
    }

    @Override
    public void onCommunicationError(final ResponseError response) {
	mCommunicationModulePlugin.defaultResponseError(response);
    }

    @Override
    public void onCommunicationResponse(final int requestId,
	    final ResponseObject response) {
	setEmptyText(mEmptyListString);
	switch (requestId) {
	case Requests.GET_PARTICIPANTS:
	    mCommunicationModulePlugin.turnOffLoadingIcon();
	    final List<MoodleUser> courseParticipantList = JSONDecoder
		    .getObjectList(new DefaultMoodleUserDescriptor(),
			    response.getResponseText());

	    mListAdapter.clear();
	    // mListAdapter.addAll(courseParticipantList);
	    for (final MoodleUser mu : courseParticipantList)
		mListAdapter.add(mu);
	    mListAdapter.notifyDataSetChanged();
	    break;
	}
    }

    @Override
    public void onCommunicationMenuItemTriggered() {
	loadCourseParticipants();
    }

    @Override
    public void onStop() {
	mDb.close();

	CommuncationModule.cancelAllRunningAsyncRequests(this);
	AsyncManager.cancelRunningTasks(sComponentUri, true);
	super.onStop();
    }

    private void loadCourseParticipants() {
	mCommunicationModulePlugin.turnOnLoadingIcon();
	setEmptyText("Loading participants....");
	new ManagedAsyncTask<Object, Void, Void>(sComponentUri) {

	    @Override
	    protected Void doInBackground(final Object... params) {
		((SQLiteDatabaseWrapper) params[0]).loadDatabase();

		CommuncationModule.sendAsyncRequest(
			mActivity,
			(RequestObject) params[1],
			CourseParticipantsFragment.Requests.GET_PARTICIPANTS,
			CourseParticipantsFragment.this);
		return null;
	    }

	}.execute(mDb, new GetCourseParticipants(getUserSession(), mCourse));
    }

    /* ======================== Private CLasses ====================== */
    public class CourseParticipantViewPopulator implements
	    DynamicViewAdapter.DynamicViewPopulator<MoodleUser> {
	class ViewHolder implements SimpleViewHolder {
	    TextView participatName;
	    QuickContactBadge participantBadge;
	}

	private final MoodleUserContactDbWrapper mDb;
	private final Context mContext;

	/**
	 * @param context
	 * @param mUserContactCache
	 */
	public CourseParticipantViewPopulator(
		final MoodleUserContactDbWrapper db, final Context context) {
	    super();
	    mDb = db;
	    mContext = context;
	}

	@Override
	public SimpleViewHolder getViewHolder(final View view) {
	    final ViewHolder h = new ViewHolder();
	    h.participatName = (TextView) view.findViewById(R.id.textview_name);
	    h.participantBadge = (QuickContactBadge) view
		    .findViewById(R.id.quickcontact_profile);
	    return h;
	}

	@Override
	public void bindView(final SimpleViewHolder viewHolder,
		final MoodleUser data) {
	    final ViewHolder h = (ViewHolder) viewHolder;
	    h.participatName.setText(data.getFullName());

	    RemoteImageViewLoader
		    .cancelRemotePictureLoad(h.participantBadge);

	    final PhoneContact contact;
	    if (!fLoadingDb)
		contact = mDb.getMoodleUserContact(data);
	    else
		contact = null;

	    if (contact == null) {
		RemoteImageViewLoader
			.loadRemotePicture(
				R.drawable.ic_contact_picture,
				sComponentUri,
				data.getPictureUrl(),
				h.participantBadge);
		h.participantBadge
			.setOnClickListener(new OnClickListener() {

			    @Override
			    public void onClick(final View v) {
				if (fLoadingDb)
				    Toast.makeText(
					    mContext,
					    "Loading contacts. Try again later",
					    Toast.LENGTH_SHORT).show();
				else
				    Toast.makeText(mContext,
					    "Attach the user to a contact",
					    Toast.LENGTH_SHORT).show();
			    }
			});
	    } else {
		h.participantBadge.setImageBitmap(contact
			.getProfileAvatarBitmap());
		h.participantBadge.assignContactUri(contact.getUri());
		h.participantBadge.setOnClickListener(h.participantBadge);
	    }
	}

    }

    /* ========================== Interfaces ======================= */

    public interface Listener {
	public void onParticipantSelected(MoodleUser user);
    }

    private interface Requests {
	int GET_PARTICIPANTS = 0;
    }

}
