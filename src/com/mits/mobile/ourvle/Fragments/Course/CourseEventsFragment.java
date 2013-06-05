/**
 * 
 */
package com.mits.mobile.ourvle.Fragments.Course;

import java.util.List;
import java.util.TimeZone;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import com.mits.mobile.ourvle.R;
import org.sourceforge.ah.android.utilities.Communication.Interfaces.OnCommunicationResponseListener;
import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONDecoder;
import org.sourceforge.ah.android.utilities.Communication.Response.ResponseError;
import org.sourceforge.ah.android.utilities.Communication.Response.ResponseObject;
import org.sourceforge.ah.android.utilities.Plugins.DefaultCommunicationModulePlugin;
import org.sourceforge.ah.android.utilities.Plugins.DefaultCommunicationModulePlugin.OnReloadFragmentListener;
import org.sourceforge.ah.android.utilities.Plugins.BaseClass.PluggableListFragment;

import android.annotation.TargetApi;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mits.mobile.ourvle.Classes.DataLayer.Databases.ContentProviderContracts.CourseEventsContract;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Courses.CourseEvent;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Courses.MoodleCourse;
import com.mits.mobile.ourvle.Classes.ParcableWrappers.MoodleCourseParcel;
import com.mits.mobile.ourvle.Classes.TransportLayer.JSONDescriptors.Moodle.Courses.CourseEventDescriptor;

/**
 * @author Aston Hamilton
 * 
 */
public class CourseEventsFragment extends PluggableListFragment
	implements LoaderCallbacks<Cursor>, OnReloadFragmentListener,
	OnCommunicationResponseListener {
    private static interface ColumnIndicies {
	int Id = 0;
	int Title = 1;
	int Description = 2;
	int DateStart = 3;
	int Duration = 4;
    }

    private MoodleCourse mCourse;
    private CourseEventCursorAdatper mAdapter;

    private String mCourseEventIdListString;

    private final static String ARG_MOODLE_COURSE = "moodle_course";

    private DefaultCommunicationModulePlugin mReloadFragmentPlugin;
    private long mDefaultCalendarId;

    public static CourseEventsFragment newInstance(
	    final MoodleCourse course) {
	final CourseEventsFragment f = new CourseEventsFragment();

	f.setMoodleCourse(course);
	return f;
    }

    public void setMoodleCourse(final MoodleCourse course) {
	getFragmentArguments()
		.putParcelable(CourseEventsFragment.ARG_MOODLE_COURSE,
			new MoodleCourseParcel(course));
	mCourse = course;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
	mCourse = ((MoodleCourseParcel) getFragmentArguments()
		.getParcelable(CourseEventsFragment.ARG_MOODLE_COURSE))
		.getWrappedObejct();

	mAdapter = new CourseEventCursorAdatper(
		getApplicationContext(),
		mCourse, null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

	mReloadFragmentPlugin = new DefaultCommunicationModulePlugin(this);
	registerPlugin(mReloadFragmentPlugin);

	getLoaderManager().initLoader(Loaders.LoadCalendar, null, this);
	getLoaderManager().initLoader(Loaders.LoadCourseEvents, null, this);
	setListAdapter(mAdapter);
	super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
	    final ViewGroup container,
	    final Bundle savedInstanceState) {
	final View v = inflater.inflate(R.layout.fragment_course_event_list,
		container, false);
	return v;
    }

    @Override
    public void onListItemClick(final ListView l, final View v,
	    final int position, final long id) {
	final Cursor classTimeClicked = (Cursor) mAdapter.getItem(position);

	final long eventId = classTimeClicked.getLong(ColumnIndicies.Id);
	viewEvent(eventId);
	super.onListItemClick(l, v, position, id);
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle bundle) {
	if (id == Loaders.LoadCourseEvents)
	    return new CursorLoader(getParentActivity(),
		    CourseEventsContract.CONTENT_URI,
		    new String[] {
			    CourseEventsContract.COURSE_ID,
			    CourseEventsContract.EVENT_ID,
		    },
		    CourseEventsContract.COURSE_ID + " = ?",
		    new String[] { mCourse.getId().toString() },
		    null);

	if (id == Loaders.LoadCalendarEvents)
	    return getCalendarEventsLoader();

	if (id == Loaders.LoadCalendar)
	    return getCalendarLoader();

	return null;
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
	if (loader.getId() == Loaders.LoadCourseEvents) {
	    final StringBuilder b = new StringBuilder();

	    if (data.moveToNext())
		b.append(data.getLong(1));
	    while (data.moveToNext()) {
		b.append(",");
		b.append(data.getLong(1));
	    }
	    mCourseEventIdListString = b.toString();
	    getLoaderManager().restartLoader(Loaders.LoadCalendarEvents, null,
		    this);
	}

	if (loader.getId() == Loaders.LoadCalendarEvents) {
	    if (data.getCount() == 0)
		loadCourseEvents();
	    mAdapter.swapCursor(data);
	}

	if (loader.getId() == Loaders.LoadCalendar)
	    if (data.moveToFirst())
		mDefaultCalendarId = data.getLong(0);
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
	if (loader.getId() != Loaders.LoadCalendar)
	    mAdapter.swapCursor(null);
    }

    @Override
    public void onCommunicationError(final ResponseError arg0) {
	mReloadFragmentPlugin.defaultResponseError(arg0);
    }

    @Override
    public void onCommunicationResponse(final int id,
	    final ResponseObject response) {
	if (id == Requests.GetCourseEvents) {
	    final List<CourseEvent> courseEvents = JSONDecoder
		    .getObjectList(new CourseEventDescriptor(mCourse),
			    response.getResponseText());

	    final ContentResolver cr = getApplicationContext()
		    .getContentResolver();

	    final long calendarId = mDefaultCalendarId;

	    final String timezone = TimeZone.getDefault().getID();

	    // Empty the table and insert all new events
	    new AsyncQueryHandler(cr) {
		@Override
		protected void onDeleteComplete(final int token,
			final Object cookie, final int result) {
		    new AsyncQueryHandler(cr) {
			@Override
			protected void onDeleteComplete(final int token,
				final Object cookie, final int result) {
			    for (final CourseEvent event : courseEvents)
				saveEvent(event, cr, calendarId, timezone);
			}
		    }.startDelete(0, null, Events.CONTENT_URI, Events._ID
			    + " IN ( " + mCourseEventIdListString + ")",
			    null);
		};
	    }.startDelete(0, null, CourseEventsContract.CONTENT_URI, null, null);

	}

    }

    @Override
    public void onCommunicationMenuItemTriggered() {
	loadCourseEvents();
    }

    private void loadCourseEvents() {
	if (mCourseEventIdListString == null) {
	    Toast.makeText(getApplicationContext(),
		    "Try again when loading completes.",
		    Toast.LENGTH_LONG).show();
	    return;
	}
	Toast.makeText(getApplicationContext(), "Loading events stub.",
		Toast.LENGTH_LONG).show();

    }

    private void saveEvent(final CourseEvent event, final ContentResolver cr,
	    final long calendarId, final String timezone) {
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	    saveEvent_API14(event, cr, calendarId, timezone);
	else
	    throw new RuntimeException(
		    "Adding class times is not supported pre Honeycomb.");
    }

    @TargetApi(14)
    private void saveEvent_API14(final CourseEvent event,
	    final ContentResolver cr, final long calendarId,
	    final String timezone) {
	final ContentValues values = new ContentValues();
	values.put(Events.DTSTART,
		event.getEventStartDateTime().getMillis());
	values.put(Events.DTEND,
		event.getEventEndDateTime().getMillis());
	values.put(Events.TITLE, event.getEventName());
	values.put(Events.DESCRIPTION, event.getEventDesc());
	values.put(Events.CALENDAR_ID, calendarId);
	values.put(Events.EVENT_TIMEZONE, timezone);
	values.put(Events.ACCESS_LEVEL, Events.ACCESS_DEFAULT);

	new AsyncQueryHandler(cr) {
	    @Override
	    protected void onInsertComplete(final int token,
		    final Object cookie, final Uri uri) {
		final long eventID = Long.parseLong(uri
			.getLastPathSegment());

		final ContentValues values = new ContentValues();
		values.put(CourseEventsContract.COURSE_ID,
			mCourse.getId());
		values.put(CourseEventsContract.EVENT_ID, eventID);

		new AsyncQueryHandler(cr) {
		}.startInsert(0, null,
			CourseEventsContract.CONTENT_URI,
			values);

		super.onInsertComplete(token, cookie, uri);
	    }
	}.startInsert(0, null, Events.CONTENT_URI, values);
    }

    private Loader<Cursor> getCalendarEventsLoader() {
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	    return getCalendarEventsLoader_API14();
	else
	    throw new RuntimeException(
		    "Using this fragment is not supported pre Honeycomb.");
    }

    @TargetApi(14)
    public Loader<Cursor> getCalendarEventsLoader_API14() {
	return new CursorLoader(getParentActivity(), Events.CONTENT_URI,
		new String[] {
			Events._ID,
			Events.TITLE,
			Events.DESCRIPTION,
			Events.DTSTART,
			Events.DTEND
		}, Events._ID + " IN ( " + mCourseEventIdListString + ")",
		null,
		Events.DTSTART + " ASC");
    }

    private Loader<Cursor> getCalendarLoader() {
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	    return getCalendarEventsLoader_API14();
	else
	    Toast.makeText(getApplicationContext(),
		    "Not supported on this device", Toast.LENGTH_SHORT).show();
	return null;
    }

    @TargetApi(14)
    public Loader<Cursor> getCalendarCursorLoader_API14() {
	return new CursorLoader(getActivity(), Calendars.CONTENT_URI,
		new String[] {
			Calendars._ID,
			Calendars.ACCOUNT_NAME,
			Calendars.CALENDAR_DISPLAY_NAME
		}, Calendars.SYNC_EVENTS + " = 1 AND " + Calendars.VISIBLE
			+ " = 1", null,
		Calendars.CALENDAR_DISPLAY_NAME + " COLLATE LOCALIZED ASC");
    }

    private void viewEvent(final long eventId) {
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	    startEditEventIntent_API14(eventId);
	else
	    Toast.makeText(getApplicationContext(),
		    "Not supported on this device", Toast.LENGTH_SHORT).show();
    }

    @TargetApi(14)
    private void startEditEventIntent_API14(final long eventId) {
	final Uri uri = ContentUris.withAppendedId(Events.CONTENT_URI, eventId);
	final Intent intent = new Intent(Intent.ACTION_VIEW)
		.setData(uri);
	startActivity(intent);
    }

    /* ================================ Private Classes =================== */
    private static class CourseEventCursorAdatper
	    extends CursorAdapter {

	private final MoodleCourse mCourse;

	private final DateTimeFormatter mStartDateFormat = DateTimeFormat
		.forPattern("EEEE KK:mm a");
	private final DateTimeFormatter mEndDateFormat = DateTimeFormat
		.forPattern("KK:mm a");

	public CourseEventCursorAdatper(final Context context,
		final MoodleCourse course,
		final Cursor c, final int flags) {
	    super(context, c, flags);
	    mCourse = course;
	}

	class ViewHolder {
	    TextView className;
	    TextView classTime;
	}

	@Override
	public void bindView(final View v, final Context context,
		final Cursor data) {
	    final ViewHolder h = (ViewHolder) v.getTag();

	    String className = data.getString(ColumnIndicies.Description);
	    if (className.trim().length() == 0)
		className = data.getString(ColumnIndicies.Title);
	    if (className.trim().length() == 0)
		className = mCourse.getName();

	    h.className.setText(className);

	    final long startDateMilis = data.getLong(ColumnIndicies.DateStart);
	    final long endDateMilis = data.getLong(ColumnIndicies.Duration);

	    final String eventTimeString;

	    if (endDateMilis > 0)
		eventTimeString =
			mStartDateFormat
				.print(startDateMilis)
				+ " - "
				+ mEndDateFormat.print(endDateMilis);
	    else
		eventTimeString = "All Day";
	    h.classTime.setText(eventTimeString.toUpperCase());

	}

	@Override
	public View newView(final Context context, final Cursor data,
		final ViewGroup parent) {

	    final LayoutInflater inflater = LayoutInflater.from(context);
	    final View v = inflater.inflate(
		    R.layout.list_item_course_event, parent, false);

	    final ViewHolder h = new ViewHolder();
	    h.className = (TextView) v.findViewById(R.id.textview_event_name);
	    h.classTime = (TextView) v
		    .findViewById(R.id.textview_event_time);

	    v.setTag(h);
	    return v;
	}
    }

    /* ========================= Interfaces ===================== */
    private static interface Loaders {
	int LoadCourseEvents = 0;
	int LoadCalendarEvents = 1;
	int LoadCalendar = 2;
    }

    private static interface Requests {
	int GetCourseEvents = 0;
    }

}
