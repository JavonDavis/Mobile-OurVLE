package edu.uwi.mona.mobileourvle.app.Fragments.Course.Companion.CourseClasses.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.ContentProviderContracts.CourseClassTimeContract;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Courses.MoodleCourse;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.MoodleCourseParcel;
import edu.uwi.mona.mobileourvle.app.R;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.MutableDateTime;
import org.joda.time.Period;
import org.joda.time.format.ISOPeriodFormat;
import org.sourceforge.ah.android.utilities.Dialog.DialogCreator;
import org.sourceforge.ah.android.utilities.Dialog.DialogManager;
import org.sourceforge.ah.android.utilities.Dialog.DialogResponseReceiver;
import org.sourceforge.ah.android.utilities.Widgets.Dialogs.DatePickerDialogFragment;
import org.sourceforge.ah.android.utilities.Widgets.Dialogs.TimePickerDialogFragment;
import org.sourceforge.ah.android.utilities.Widgets.Fragments.DialogFragmentBase;
import org.sourceforge.ah.android.utilities.Widgets.Util.CalendarCompatabilityLayer.CompatabilityCalendarContract;
import org.sourceforge.ah.android.utilities.Widgets.Util.CalendarCompatabilityLayer.CompatabilityCalendars;
import org.sourceforge.ah.android.utilities.Widgets.Util.CalendarCompatabilityLayer.CompatabilityEvents;
import org.sourceforge.ah.android.utilities.Widgets.Util.CalendarCompatabilityLayer.CompatabilityReminders;

import java.util.TimeZone;

public class AddCourseClassDialogFragment extends DialogFragmentBase
        implements LoaderCallbacks<Cursor>, DialogCreator {
    private MoodleCourse mCourse;

    private final static String ARG_COURSE = "course";

    private final static String STATE_START_DATE_TIME = "startdatetime";
    private final static String STATE_END_DATE_TIME = "enddatetime";

    private final static int LOADER_CALENDARS = 0;

    private final static int DIALOG_DATE_PICKER = 1;
    private final static int DIALOG_TIME_PICKER = 2;

    private SimpleCursorAdapter mCalendarSpinnerAdapter;

    private Spinner mCalendarSpinner;
    private Spinner mWeekdaysSpinner;
    private TextView mCourseTextView;
    private Button mStartTimeButton;
    private Button mEndTimeButton;
    private EditText mClassTypeEditText;
    private EditText mClassLocationEditText;

    private boolean mStartDateSetFlag;
    private boolean mStartTimeSetFlag;

    private MutableDateTime mStartDateTime;
    private MutableDateTime mEndDateTime;

    public static AddCourseClassDialogFragment newInstance(
            final MoodleCourse course) {
        final AddCourseClassDialogFragment f = new AddCourseClassDialogFragment();

        f.setMoodleCourse(course);

        return f;
    }

    private void setMoodleCourse(final MoodleCourse course) {
        getFragmentArguments().putParcelable(
                AddCourseClassDialogFragment.ARG_COURSE,
                new MoodleCourseParcel(course));
        mCourse = course;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        mCourse = ((MoodleCourseParcel)
                getFragmentArguments().
                        getParcelable(AddCourseClassDialogFragment.ARG_COURSE))
                .getWrappedObejct();

        // Register Dialogs
        DialogManager.registerResponseReceiver(getApplicationContext(),
                                               AddCourseClassDialogFragment.DIALOG_DATE_PICKER,
                                               DatePickerDialogFragment.Response.onDateChanged,
                                               onDateChangedReceiver);
        DialogManager.registerResponseReceiver(getApplicationContext(),
                                               AddCourseClassDialogFragment.DIALOG_TIME_PICKER,
                                               TimePickerDialogFragment.Response.onTimeChanged,
                                               onTimeChangedReceiver);

        // Restore states
        if (savedInstanceState != null)
            restoreSavedInstanceState(savedInstanceState);

        // Create an empty adapter we will use to display the loaded data.

        final CompatabilityCalendars caenlarContractCalendars = CompatabilityCalendarContract
                .getInstance().getCalendars();
        mCalendarSpinnerAdapter = new SimpleCursorAdapter(
                getActivity(),
                android.R.layout.simple_list_item_1,
                null,
                new String[]{caenlarContractCalendars.CALENDAR_DISPLAY_NAME},
                new int[]{android.R.id.text1}, 0);

        if (!getShowsDialog())
            setHasOptionsMenu(true);

        getLoaderManager()
                .initLoader(AddCourseClassDialogFragment.LOADER_CALENDARS,
                            null, this);

        mStartDateTime = new MutableDateTime();

        if (mStartDateTime.getMinuteOfHour() > 30) {
            mStartDateTime.set(DateTimeFieldType.minuteOfHour(), 0);
            mStartDateTime.addHours(1);
        } else
            mStartDateTime.set(DateTimeFieldType.minuteOfHour(), 30);

        mEndDateTime = mStartDateTime.copy();
        mEndDateTime.add(DurationFieldType.hours(), 1);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        inflater.inflate(R.menu.confirm_create_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {

        if (getShowsDialog())
            return super.onCreateView(inflater, container, savedInstanceState);

        final View v = getDialogViewHeirachy(inflater, container,
                                             savedInstanceState);

        return v;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(
                getParentActivity());

        final LayoutInflater inflater = getParentActivity().getLayoutInflater();
        builder.setView(getDialogViewHeirachy(inflater, null,
                                              savedInstanceState));
        builder.setTitle("Add Class");
        builder.setPositiveButton("Save",
                                  new DialogInterface.OnClickListener() {

                                      @Override
                                      public void onClick(final DialogInterface dialog,
                                                          final int which) {
                                          addClassTime();
                                      }

                                  });

        builder.setNegativeButton("Cancel",
                                  new DialogInterface.OnClickListener() {

                                      @Override
                                      public void onClick(final DialogInterface dialog,
                                                          final int which) {
                                          sendResponse(Response.onClassAdditionCancelled,
                                                       new Bundle());
                                      }

                                  });

        return builder.create();
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int arg0, final Bundle arg1) {
        return getCalendarLoader();
    }

    private Loader<Cursor> getCalendarLoader() {

        final CompatabilityCalendars caenlarContractCalendars = CompatabilityCalendarContract
                .getInstance().getCalendars();
        return new CursorLoader(getActivity(),
                                caenlarContractCalendars.CONTENT_URI,
                                new String[]{
                                        caenlarContractCalendars._ID,
                                        caenlarContractCalendars.ACCOUNT_NAME,
                                        caenlarContractCalendars.CALENDAR_DISPLAY_NAME
                                }, caenlarContractCalendars.SYNC_EVENTS + " = 1 AND "
                                   + caenlarContractCalendars.VISIBLE
                                   + " = 1", null,
                                caenlarContractCalendars.CALENDAR_DISPLAY_NAME
                                + " COLLATE LOCALIZED ASC");
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> arg0, final Cursor data) {

        mCalendarSpinnerAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> arg0) {
        mCalendarSpinnerAdapter.swapCursor(null);
    }

    @Override
    public DialogFragmentBase createDialog(final int id) {
        if (id == AddCourseClassDialogFragment.DIALOG_DATE_PICKER)
            if (mStartDateSetFlag)
                return DatePickerDialogFragment.newInstance(mStartDateTime
                                                                    .toDateTime());
            else
                return DatePickerDialogFragment.newInstance(mEndDateTime
                                                                    .toDateTime());

        if (id == AddCourseClassDialogFragment.DIALOG_TIME_PICKER)
            if (mStartTimeSetFlag)
                return TimePickerDialogFragment.newInstance(mStartDateTime
                                                                    .toDateTime());
            else
                return TimePickerDialogFragment.newInstance(mEndDateTime
                                                                    .toDateTime());

        return null;
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        outState.putSerializable(
                AddCourseClassDialogFragment.STATE_START_DATE_TIME,
                mStartDateTime);
        outState.putSerializable(
                AddCourseClassDialogFragment.STATE_END_DATE_TIME,
                mEndDateTime);
        super.onSaveInstanceState(outState);
    }

    private void restoreSavedInstanceState(final Bundle inState) {
        mStartDateTime = (MutableDateTime) inState
                .getSerializable(AddCourseClassDialogFragment.STATE_START_DATE_TIME);
        mEndDateTime = (MutableDateTime) inState
                .getSerializable(AddCourseClassDialogFragment.STATE_END_DATE_TIME);
    }

    private void addClassTime() {

        if (mEndDateTime.isBefore(mStartDateTime)) {
            Toast.makeText(getApplicationContext(),
                           "End time cannot be before start time.", Toast.LENGTH_SHORT)
                 .show();
            return;
        }
        final CompatabilityEvents calendarContractEvents = CompatabilityCalendarContract
                .getInstance().getEvents();

        final String[] iCalDays = {"MO", "TU", "WE", "TH", "FR", "SA", "SU"};
        final String iCalRecurrenceDay = iCalDays[mStartDateTime.getDayOfWeek() - 1];

        final long calendarId = mCalendarSpinner.getSelectedItemId();
        final String courseType = mClassTypeEditText.getText().toString()
                                                    .trim();
        final String classLocation = mClassLocationEditText.getText().toString().trim();

        final String timezone = TimeZone.getDefault().getID();

        final Period classDurationPeriod = new Period(mStartDateTime,
                                                      mEndDateTime);

        final ContentResolver cr = getApplicationContext().getContentResolver();
        final ContentValues values = new ContentValues();
        values.put(calendarContractEvents.DTSTART,
                   mStartDateTime.getMillis());
        values.put(calendarContractEvents.DURATION,
                   classDurationPeriod.toString(ISOPeriodFormat.standard()));
        values.put(calendarContractEvents.TITLE, mCourse.getName());
        values.put(calendarContractEvents.DESCRIPTION, courseType);
        values.put(calendarContractEvents.CALENDAR_ID, calendarId);
        values.put(calendarContractEvents.EVENT_TIMEZONE, timezone);
        values.put("eventLocation",classLocation);
        values.put(calendarContractEvents.HAS_ALARM, 1);
        values.put(calendarContractEvents.ACCESS_LEVEL,
                   calendarContractEvents.ACCESS_PRIVATE);
        values.put(calendarContractEvents.AVAILABILITY,
                   calendarContractEvents.AVAILABILITY_BUSY);
        values.put(calendarContractEvents.RRULE, "FREQ=WEEKLY;BYDAY="
                                                + iCalRecurrenceDay);

        new AsyncQueryHandler(cr) {
            @Override
            protected void onInsertComplete(final int token,
                                            final Object cookie, final Uri uri) {
                final long eventID = Long.parseLong(uri.getLastPathSegment());

                final CompatabilityReminders caenlarContractReminders = CompatabilityCalendarContract
                        .getInstance().getReminders();

                final ContentValues values = new ContentValues();
                values.put(caenlarContractReminders.EVENT_ID, eventID);
                values.put(caenlarContractReminders.METHOD,
                           caenlarContractReminders.METHOD_ALERT);
                values.put(caenlarContractReminders.MINUTES, 15);

                new AsyncQueryHandler(cr) {
                    @Override
                    protected void onInsertComplete(final int token,
                                                    final Object cookie, final Uri uri) {

                        final ContentValues values = new ContentValues();
                        values.put(CourseClassTimeContract.COURSE_ID,
                                   mCourse.getId());
                        values.put(CourseClassTimeContract.EVENT_ID, eventID);

                        new AsyncQueryHandler(cr) {
                            @Override
                            protected void onInsertComplete(final int token,
                                                            final Object cookie, final Uri uri) {

                                final Bundle b = new Bundle();
                                b.putParcelable(Response.IntertedUri, uri);

                                // explicitly specify we want app config in
                                // async
                                // context
                                sendResponse(getApplicationContext(),
                                             Response.onClassAdded, b);

                                super.onInsertComplete(token, cookie, uri);

                            }

                            ;
                        }.startInsert(0, null,
                                      CourseClassTimeContract.CONTENT_URI,
                                      values);
                        super.onInsertComplete(token, cookie, uri);
                    }

                    ;
                }.startInsert(0, null, caenlarContractReminders.CONTENT_URI,
                              values);
            }
        }.startInsert(0, null, calendarContractEvents.CONTENT_URI, values);
    }

    private View getDialogViewHeirachy(final LayoutInflater inflater,
                                       final ViewGroup container, final Bundle savedInstanceState) {

        final View v = inflater.inflate(
                R.layout.fragment_course_class_add_class, container, false);
        mCalendarSpinner = (Spinner) v.findViewById(R.id.spinner_calendars);
        mWeekdaysSpinner = (Spinner) v.findViewById(R.id.spinner_weekdays);
        mStartTimeButton = (Button) v.findViewById(R.id.button_start_time);
        mEndTimeButton = (Button) v.findViewById(R.id.button_end_time);
        mCourseTextView = (TextView) v.findViewById(R.id.textview_course);
        mClassTypeEditText = (EditText) v
                .findViewById(R.id.edittext_class_type);

        mClassLocationEditText = (EditText) v.findViewById(R.id.editText_class_location);

        mWeekdaysSpinner
                .setAdapter(
                        new ArrayAdapter<String>(getApplicationContext(),
                                                 R.layout.list_item_simple_item,
                                                 android.R.id.text1,
                                                 new String[]{
                                                         "Monday", "Tuesday",
                                                         "Wednesday", "Thursday", "Friday",
                                                         "Saturday","Sunday"
                                                 }));

        mWeekdaysSpinner
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(final AdapterView<?> arg0,
                                               final View arg1,
                                               int position, final long id) {
                        position+=1;

                        mStartDateTime.setDayOfWeek(position);
                        mEndDateTime.setDayOfWeek(position);

                        adjustRolloverMinuites();
                    }

                    @Override
                    public void onNothingSelected(final AdapterView<?> arg0) {

                    }
                });

        mCalendarSpinner.setAdapter(mCalendarSpinnerAdapter);

        mCourseTextView.setText(mCourse.getName());

        updateDateTimeFields();

        mClassTypeEditText.setText("Lecture");

        mStartTimeButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {
                mStartTimeSetFlag = true;
                DialogManager.showDialog(
                        AddCourseClassDialogFragment.DIALOG_TIME_PICKER,
                        AddCourseClassDialogFragment.this,
                        getFragmentManager());

            }
        });

        mEndTimeButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {
                mStartTimeSetFlag = false;
                DialogManager.showDialog(
                        AddCourseClassDialogFragment.DIALOG_TIME_PICKER,
                        AddCourseClassDialogFragment.this,
                        getFragmentManager());

            }
        });

        return v;
    }

    private void updateDateTimeFields() {

        setTime(mStartTimeButton, mStartDateTime.getMillis());

        setTime(mEndTimeButton, mEndDateTime.getMillis());
    }

    private void adjustRolloverMinuites() {
        // sync dates
        mEndDateTime.setYear(mStartDateTime.getYear());
        mEndDateTime.setDayOfYear(mStartDateTime.getDayOfYear());

        if (mEndDateTime.getMinuteOfDay() < mStartDateTime.getMinuteOfDay())
            mEndDateTime.addDays(1);
    }

    private void setTime(final TextView v, final long milis) {
        final String formattedTime = DateUtils
                .formatDateTime(getApplicationContext(),
                                milis,
                                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_12HOUR);

        v.setText(formattedTime);
    }

    // No longer setting day so I dont neew this function anymore
    @SuppressWarnings("unused")
    private void setDate(final TextView v, final long milis) {
        final String formattedTime = DateUtils
                .formatDateTime(getApplicationContext(),
                                milis,
                                DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_SHOW_YEAR
                                | DateUtils.FORMAT_ABBREV_ALL);

        v.setText(formattedTime);
    }

    @Override
    public void onDestroy() {
        // Register Dialogs
        DialogManager.unregisterResponseReceiver(getApplicationContext(),
                                                 AddCourseClassDialogFragment.DIALOG_DATE_PICKER,
                                                 onDateChangedReceiver);
        DialogManager.unregisterResponseReceiver(getApplicationContext(),
                                                 AddCourseClassDialogFragment.DIALOG_TIME_PICKER,
                                                 onTimeChangedReceiver);

        super.onDestroy();
    }

    /* =================== Dialog Resposnses ============================ */
    // no longer setting dates so I dont need this response anymore
    private final DialogResponseReceiver onDateChangedReceiver = new DialogResponseReceiver() {

        @Override
        public void onResponseReceived(final Bundle data) {
            final int year = data
                    .getInt(DatePickerDialogFragment.Response.Year);
            final int month = data
                    .getInt(DatePickerDialogFragment.Response.MonthOfYear);
            final int day = data
                    .getInt(DatePickerDialogFragment.Response.DayofMonth);

            final MutableDateTime dateTime;
            if (mStartDateSetFlag == true)
                dateTime = mStartDateTime;
            else
                dateTime = mEndDateTime;

            dateTime.set(DateTimeFieldType.year(), year);
            dateTime.set(DateTimeFieldType.monthOfYear(), month + 1);
            dateTime.set(DateTimeFieldType.dayOfMonth(), day);

            updateDateTimeFields();
        }
    };

    private final DialogResponseReceiver onTimeChangedReceiver = new DialogResponseReceiver() {

        @Override
        public void onResponseReceived(final Bundle data) {
            final int hour = data
                    .getInt(TimePickerDialogFragment.Response.HourOfDay);
            final int minute = data
                    .getInt(TimePickerDialogFragment.Response.Minute);

            // maintain the time difference if start date is updated
            final long timeDelta = mEndDateTime.getMillis()
                                   - mStartDateTime.getMillis();
            final MutableDateTime dateTime;
            if (mStartTimeSetFlag == true)
                dateTime = mStartDateTime;
            else
                dateTime = mEndDateTime;

            dateTime.set(DateTimeFieldType.hourOfDay(), hour);
            dateTime.set(DateTimeFieldType.minuteOfHour(), minute);

            if (mStartTimeSetFlag == true) {
                mEndDateTime.set(DateTimeFieldType.hourOfDay(), hour);
                mEndDateTime.set(DateTimeFieldType.minuteOfHour(), minute);
                mEndDateTime.add(timeDelta);
            }

            adjustRolloverMinuites();
            updateDateTimeFields();
        }
    };

    /* =================== Interfaces ===================== */
    public static interface Response {
        String onClassAdditionCancelled = "edu.uwi.mona.mobileourvle.app.Fragments.Course.Companion.CourseClasses.AddCourseClassDialogFragment.onClassAdditionCancelled";

        String onClassAdded = "edu.uwi.mona.mobileourvle.app.Fragments.Course.Companion.CourseClasses.AddCourseClassDialogFragment.onClassAdded";
        String IntertedUri = "uri";

    }
}
