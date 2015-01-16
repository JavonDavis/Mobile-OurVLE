/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Fragments.Course.Companion.CourseClasses;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.Recur;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISOPeriodFormat;
import org.sourceforge.ah.android.utilities.Dialog.DialogCreator;
import org.sourceforge.ah.android.utilities.Dialog.DialogManager;
import org.sourceforge.ah.android.utilities.Dialog.DialogResponseReceiver;
import org.sourceforge.ah.android.utilities.Widgets.Fragments.DialogFragmentBase;
import org.sourceforge.ah.android.utilities.Widgets.Fragments.ListFragmentBase;
import org.sourceforge.ah.android.utilities.Widgets.Util.CalendarCompatabilityLayer.CompatabilityCalendarContract;
import org.sourceforge.ah.android.utilities.Widgets.Util.CalendarCompatabilityLayer.CompatabilityCalendars;
import org.sourceforge.ah.android.utilities.Widgets.Util.CalendarCompatabilityLayer.CompatabilityEventInstances;
import org.sourceforge.ah.android.utilities.Widgets.Util.CalendarCompatabilityLayer.CompatabilityEvents;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import edu.uwi.mona.mobileourvle.app.R;
import edu.uwi.mona.mobileourvle.app.R.id;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.ContentProviderContracts.CourseClassTimeContract;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Courses.MoodleCourse;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.MoodleCourseParcel;
import edu.uwi.mona.mobileourvle.app.Fragments.Course.Companion.CourseClasses.Dialogs.AddCourseClassDialogFragment;

/**
 * @author Aston Hamilton
 * 
 */
public class CourseClassTimesFragment extends ListFragmentBase
	implements LoaderCallbacks<Cursor>, DialogCreator {

    private MoodleCourse mCourse;
    private ClassTimeCursorAdatper mAdapter;

    private String mClassEventIdListString;
    private int mHasCalendarFlag = -1;
    private int itemID;
    private  Menu menu;

    private final static String ARG_MOODLE_COURSE = "moodle_course";

    public static CourseClassTimesFragment newInstance(
	    final MoodleCourse course) {
	final CourseClassTimesFragment f = new CourseClassTimesFragment();

	f.setMoodleCourse(course);
	return f;
    }

    public void setMoodleCourse(final MoodleCourse course) {
	getFragmentArguments()
		.putParcelable(CourseClassTimesFragment.ARG_MOODLE_COURSE,
			new MoodleCourseParcel(course));
	mCourse = course;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        mCourse = ((MoodleCourseParcel) getFragmentArguments()
            .getParcelable(CourseClassTimesFragment.ARG_MOODLE_COURSE))
            .getWrappedObejct();

        mAdapter = new ClassTimeCursorAdatper(
            getApplicationContext(),
            mCourse, null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        // Register dialog receivers
        DialogManager.registerResponseReceiver(getApplicationContext(),
            Dialogs.AddCourseClass,
            AddCourseClassDialogFragment.Response.onClassAdded,
            onClassAddedReceiver);

        getLoaderManager().initLoader(Loaders.LoadCalendars, null, this);
        getLoaderManager().initLoader(Loaders.LoadCourseClasses, null, this);
        setListAdapter(mAdapter);
        setHasOptionsMenu(true);

        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        addMenuOption();
    }

    private void addMenuOption() {
        menu= ((Toolbar) getActivity().findViewById(R.id.course_toolbar)).getMenu();
        //add search button to menu
        MenuItem item = menu.add("Add Class Time");
        itemID = item.getItemId();

        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setIcon(R.drawable.ic_new);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (mHasCalendarFlag < 0)
                    Toast.makeText(getApplicationContext(),
                            "Still loading. Please try again",
                            Toast.LENGTH_LONG).show();
                else if (mHasCalendarFlag == 0)
                    Toast.makeText(getApplicationContext(),
                            "Add atleast one calendar to your device.",
                            Toast.LENGTH_LONG).show();
                else
                    DialogManager.showDialog(Dialogs.AddCourseClass, CourseClassTimesFragment.this,
                            getFragmentManager());

                return true;
            }
        });
    }

    @Override
    public void onStop() {
        removeMenuItem();
        super.onStop();

    }

    private void removeMenuItem() {
        menu= ((Toolbar) getActivity().findViewById(R.id.course_toolbar)).getMenu();
        //add search button to menu
        menu.removeItem(itemID);
    }

    /*@Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {

	inflater.inflate(R.menu.add_item_menu, menu);

	super.onCreateOptionsMenu(menu, inflater);
    }*/

    @Override
    public View onCreateView(final LayoutInflater inflater,
	    final ViewGroup container,
	    final Bundle savedInstanceState) {
	final View v = inflater.inflate(R.layout.fragment_course_class_list,
		container, false);
	return v;
    }

   /* @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
	switch (item.getItemId()) {
	case id.menu_add:
	    if (mHasCalendarFlag < 0)
		Toast.makeText(getApplicationContext(),
			"Still loading. Please try again",
			Toast.LENGTH_LONG).show();
	    else if (mHasCalendarFlag == 0)
		Toast.makeText(getApplicationContext(),
			"Add atleast one calendar to your device.",
			Toast.LENGTH_LONG).show();
	    else
		DialogManager.showDialog(Dialogs.AddCourseClass, this,
			getFragmentManager());
	    break;
	}
	return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onListItemClick(final ListView l, final View v,
	    final int position, final long id) {
	final Cursor classTimeClicked = (Cursor) mAdapter.getItem(position);

	viewEvent(new EventCursorWrapper(classTimeClicked));
	super.onListItemClick(l, v, position, id);
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle bundle) {
	if (id == Loaders.LoadCalendars) {

	    final CompatabilityCalendars caenlarContractCalendars = CompatabilityCalendarContract
		    .getInstance().getCalendars();
	    return new CursorLoader(getActivity(),
		    caenlarContractCalendars.CONTENT_URI,
		    new String[] {
			    caenlarContractCalendars._ID,
			    caenlarContractCalendars.ACCOUNT_NAME,
			    caenlarContractCalendars.CALENDAR_DISPLAY_NAME
		    }, caenlarContractCalendars.SYNC_EVENTS + " = 1 AND "
			    + caenlarContractCalendars.VISIBLE
			    + " = 1", null,
		    caenlarContractCalendars.CALENDAR_DISPLAY_NAME
			    + " COLLATE LOCALIZED ASC");
	}

	if (id == Loaders.LoadCourseClasses)
	    return new CursorLoader(getParentActivity(),
		    CourseClassTimeContract.CONTENT_URI,
		    new String[] {
			    CourseClassTimeContract.COURSE_ID,
			    CourseClassTimeContract.EVENT_ID,
		    },
		    CourseClassTimeContract.COURSE_ID + " = ?",
		    new String[] { mCourse.getId().toString() },
		    null);

	if (id == Loaders.LoadCalendarEvents) {
	    final CompatabilityEvents caenlarContractEvents = CompatabilityCalendarContract
		    .getInstance().getEvents();

	    return new CursorLoader(getParentActivity(),
		    caenlarContractEvents.CONTENT_URI,
		    new String[] {
			    caenlarContractEvents._ID,
			    caenlarContractEvents.RRULE,
			    caenlarContractEvents.TITLE,
			    caenlarContractEvents.DESCRIPTION,
			    caenlarContractEvents.DTSTART,
			    caenlarContractEvents.DURATION
		    }, caenlarContractEvents._ID + " IN ( "
			    + mClassEventIdListString + ")",
		    null,
		    caenlarContractEvents.DTSTART + " ASC");
	}
	if (id == Loaders.LoadCalendarEventInstances) {
	    final CompatabilityEventInstances caenlarContractInstances = CompatabilityCalendarContract
		    .getInstance().getEventInstances();

	    return new CursorLoader(getParentActivity(),
		    caenlarContractInstances.CONTENT_URI,
		    new String[] {
			    caenlarContractInstances._ID,
			    caenlarContractInstances.EVENT_ID,
			    caenlarContractInstances.BEGIN,
			    caenlarContractInstances.END
		    }, caenlarContractInstances.EVENT_ID + " IN ( "
			    + mClassEventIdListString + ")",
		    null,
		    caenlarContractInstances.EVENT_ID + " ASC");
	}
	return null;
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
	if (loader.getId() == Loaders.LoadCalendars)
	    mHasCalendarFlag = data.getCount();

	if (loader.getId() == Loaders.LoadCourseClasses) {
	    final StringBuilder b = new StringBuilder();

	    if (data.moveToNext())
		b.append(data.getLong(1));
	    while (data.moveToNext()) {
		b.append(",");
		b.append(data.getLong(1));
	    }
	    mClassEventIdListString = b.toString();
	    getLoaderManager().restartLoader(Loaders.LoadCalendarEvents, null,
		    this);
	}

	if (loader.getId() == Loaders.LoadCalendarEvents)
	    mAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
	if (loader.getId() != Loaders.LoadCalendars)
	    mAdapter.swapCursor(null);

    }

    @Override
    public DialogFragmentBase createDialog(final int id) {
	if (id == Dialogs.AddCourseClass)
	    return AddCourseClassDialogFragment.newInstance(mCourse);

	return null;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void viewEvent(final EventCursorWrapper e) {
	final CompatabilityEvents caenlarContractEvents = CompatabilityCalendarContract
		.getInstance().getEvents();

	final Uri uri = ContentUris.withAppendedId(
		caenlarContractEvents.CONTENT_URI, e.getId());
	final Intent intent = new Intent(Intent.ACTION_VIEW)
		.setData(uri);

	final long currentTimestamp = new DateTime().getMillis();
	final long eventStartTimestamp = e.getStartTimestamp();

	try {
	    final DateTime nextClassDatetime;
	    if (eventStartTimestamp >= currentTimestamp)
		nextClassDatetime = new DateTime(eventStartTimestamp);
	    else {
		Recur recurrence = new Recur(e.getRepeatRule());

		final Date nextClassDate = recurrence
			.getNextDate(
				new Date(eventStartTimestamp),
				new Date(currentTimestamp));

		if (nextClassDate != null) {
		    final DateTime eventStartDateTime = new DateTime(
			    eventStartTimestamp);

		    // java util.date store time in GMT but i have the stamp in
		    // my
		    // time
		    // so I need to convert it to gmt first
		    final Calendar inst = Calendar.getInstance();
		    final int defaultMillisOffset = inst
			    .get(Calendar.ZONE_OFFSET)
			    + inst.get(Calendar.DST_OFFSET);
		    nextClassDatetime = new DateTime(
			    nextClassDate.getTime() - defaultMillisOffset)
			    .withTime(eventStartDateTime.getHourOfDay(),
				    eventStartDateTime.getMinuteOfHour(),
				    eventStartDateTime.getSecondOfMinute(),
				    eventStartDateTime.getMillisOfSecond());
		} else {
		    Toast.makeText(getApplicationContext(),
			    "This class is no longer held.", Toast.LENGTH_SHORT)
			    .show();
		    nextClassDatetime = new DateTime(eventStartTimestamp);
		}
	    }

	    final Long endTimestamp = e
		    .getEndTimestamp(nextClassDatetime.getMillis());

	    if (endTimestamp != null) {
		intent.putExtra(caenlarContractEvents.REVENT_START_TIME,
			nextClassDatetime.getMillis());
		intent.putExtra(caenlarContractEvents.REVENT_END_TIME,
			endTimestamp.longValue());
	    }
	} catch (final ParseException e1) {
	    // do nothing
	}
	startActivity(intent);
    }

    @Override
    public void onDestroy() {

	DialogManager.unregisterResponseReceiver(getApplicationContext(),
		Dialogs.AddCourseClass,
		onClassAddedReceiver);
	super.onDestroy();
    }

    /* =============================== Dialog Responses ================== */
    private final DialogResponseReceiver onClassAddedReceiver = new DialogResponseReceiver() {

	@Override
	public void onResponseReceived(final Bundle data) {
	    final Uri uri = data
		    .getParcelable(AddCourseClassDialogFragment.Response.IntertedUri);

	    Toast.makeText(getApplicationContext(),
		    "Class time added", Toast.LENGTH_SHORT)
		    .show();
	}
    };

    /* ================================ Private Classes =================== */
    private static class EventCursorWrapper {
	private final Cursor mCursor;

	private Long cId;
	private Long cStartTimestamp;
	private Long cEndTimestamp;
	private String cDuration;
	private String cDescription;
	private String cTitle;
	private String cRepeatRule;

	private static final int Id = 0;
	private static final int RepeatRule = 1;
	private static final int Title = 2;
	private static final int Description = 3;
	private static final int DateStart = 4;
	private static final int Duration = 5;

	/**
	 * @param cursor
	 */
	public EventCursorWrapper(final Cursor cursor) {
	    super();
	    mCursor = cursor;
	}

	public Long getId() {
	    if (cId == null)
		cId = mCursor.getLong(EventCursorWrapper.Id);

	    return cId;
	}

	public String getISOFormattedDuration() {
	    if (cDuration == null) {
		cDuration = mCursor
			.getString(EventCursorWrapper.Duration);

		if (cDuration != null) {
		    // fix for android malformed duration
		    cDuration = cDuration.toUpperCase(Locale.US);
		    if (cDuration.charAt(0) == 'P'
			    && !cDuration.contains("T")
			    && cDuration.charAt(1) >= '0'
			    && cDuration.charAt(1) <= '9')
			cDuration = "PT" + cDuration.substring(1);
		}
	    }
	    return cDuration;
	}

	public String getDescription() {
	    if (cDescription == null)
		cDescription = mCursor
			.getString(EventCursorWrapper.Description);

	    return cDescription;
	}

	public String getTitle() {
	    if (cTitle == null)
		cTitle = mCursor.getString(EventCursorWrapper.Title);

	    return cTitle;
	}

	public String getRepeatRule() {
	    if (cRepeatRule == null)
		cRepeatRule = mCursor.getString(EventCursorWrapper.RepeatRule);

	    return cRepeatRule;
	}

	private Long getEndTimestamp() {
	    if (cEndTimestamp == null) {
		final long startTime = getStartTimestamp();
		cEndTimestamp = getEndTimestamp(startTime);
	    }
	    return cEndTimestamp;
	}

	public Long getStartTimestamp() {
	    if (cStartTimestamp == null)
		cStartTimestamp = mCursor
			.getLong(EventCursorWrapper.DateStart);

	    return cStartTimestamp;
	}

	public Long getEndTimestamp(final long startTime) {
	    long endTimestamp;
	    final String durationString = getISOFormattedDuration();
	    try {

		if (durationString == null || durationString.length() == 0)
		    return null;

		final org.joda.time.Period durationPeriod = ISOPeriodFormat
			.standard()
			.parsePeriod(durationString);
		endTimestamp = new DateTime(startTime).plus(
			durationPeriod).getMillis();

	    } catch (final IllegalArgumentException e) {
		return null;
	    }

	    return endTimestamp;
	}
    }

    private static class ClassTimeCursorAdatper
	    extends CursorAdapter {

	private final MoodleCourse mCourse;

	private final DateTimeFormatter mStartDateFormat = DateTimeFormat
		.forPattern("EEEE hh:mm a");
	private final DateTimeFormatter mEndDateFormat = DateTimeFormat
		.forPattern("hh:mm a");

	public ClassTimeCursorAdatper(final Context context,
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

	    final EventCursorWrapper eventCursorWrapper = new EventCursorWrapper(
		    data);

	    String className = eventCursorWrapper.getDescription();
	    if (className.trim().length() == 0)
		className = eventCursorWrapper.getTitle();
	    if (className.trim().length() == 0)
		className = mCourse.getName();

	    h.className.setText(className);

	    final long startDateMilis = eventCursorWrapper
		    .getStartTimestamp();

	    final Long endDateMilis = eventCursorWrapper.getEndTimestamp();
	    if (endDateMilis == null) {
		Log.e(this.getClass().toString(),
			"Duration string not specified for the class: "
				+ className);

		h.classTime.setText("Date Error".toUpperCase(Locale.US));
	    } else {
		final String classTimeString = mStartDateFormat
			.print(startDateMilis)
			+ " - "
			+ mEndDateFormat.print(endDateMilis);
		h.classTime.setText(classTimeString.toUpperCase(Locale.US));
	    }
	}

	@Override
	public View newView(final Context context, final Cursor data,
		final ViewGroup parent) {

	    final LayoutInflater inflater = LayoutInflater.from(context);
	    final View v = inflater.inflate(
		    R.layout.list_item_course_class_time, parent, false);

	    final ViewHolder h = new ViewHolder();
	    h.className = (TextView) v.findViewById(R.id.textview_class_name);
	    h.classTime = (TextView) v
		    .findViewById(R.id.textview_class_time);

	    v.setTag(h);
	    return v;
	}
    }

    /* ========================= Interfaces ===================== */
    private static interface Dialogs {
	int AddCourseClass = 0;
    }

    private static interface Loaders {
	int LoadCalendarEventInstances = 3;
	int LoadCourseClasses = 0;
	int LoadCalendarEvents = 1;
	int LoadCalendars = 2;
    }

}
