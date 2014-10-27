/**
 *
 */
package edu.uwi.mona.mobileourvle.app.Fragments.Course.Companion.Notes;

import org.joda.time.DateTime;
import org.sourceforge.ah.android.utilities.Formatters.DateFormatter;
import org.sourceforge.ah.android.utilities.Plugins.BaseClass.PluggableListFragment;
import org.sourceforge.ah.android.utilities.Widgets.Adapters.DynamicViewAdapter.SimpleViewHolder;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import edu.uwi.mona.mobileourvle.app.Activities.CourseContentsActivity;
import edu.uwi.mona.mobileourvle.app.R;
import edu.uwi.mona.mobileourvle.app.R.id;
import edu.uwi.mona.mobileourvle.app.Activities.AddCourseNoteActivity;
import edu.uwi.mona.mobileourvle.app.Activities.ViewCourseNoteActivity;
import edu.uwi.mona.mobileourvle.app.Classes.SharedConstants.ParcelKeys;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.CompanionEntities.CourseNote;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.ContentProviderContracts.CourseClassTimeContract;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.ContentProviderContracts.CourseNotesContract;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Courses.MoodleCourse;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.MoodleCourseNoteParcel;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.MoodleCourseParcel;

/**
 * @author Aston Hamilton
 *
 */
public class CourseNotesFragment extends PluggableListFragment implements
        LoaderCallbacks<Cursor> {
    private MoodleCourse mCourse;
    private CourseNotesAdapter mAdapter;
    private static boolean isLargeScreen = false;
    private CourseContentsActivity activity;
    private ViewCourseNoteFragment mFragment;

    public static CourseNotesFragment newInstance(
            final MoodleCourse course, boolean large) {
        final CourseNotesFragment f = new CourseNotesFragment();
        isLargeScreen = large;
        f.setMoodleCourse(course);
        return f;
    }

    public void setMoodleCourse(final MoodleCourse course) {
        getFragmentArguments().putParcelable(ParcelKeys.MOODLE_COURSE,
                new MoodleCourseParcel(course));
        mCourse = course;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        mCourse = ((MoodleCourseParcel) getFragmentArguments()
                .getParcelable(ParcelKeys.MOODLE_COURSE))
                .getWrappedObejct();

        mAdapter = new CourseNotesAdapter(mCourse,
                getParentActivity(), null,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        try
        {
            activity = (CourseContentsActivity) getActivity();
        }
        catch(ClassCastException e)
        {
            Log.e("cast exception in notes fragment",e.toString());
        }

        getLoaderManager().initLoader(Loaders.LoadCourseNotes, null, this);

        setListAdapter(mAdapter);

        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {

        inflater.inflate(R.menu.add_item_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_course_notes_list,
                container, false);
        return v;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case id.menu_add:

                startNewNoteIntent();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(final ListView l, final View v,
                                final int position, final long id) {
        startViewNoteIntent(position);

        super.onListItemClick(l, v, position, id);
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int arg0, final Bundle arg1) {
        return new CursorLoader(getParentActivity(),
                CourseNotesContract.CONTENT_URI,
                new String[] {
                        CourseNotesContract.COURSE_ID,
                        CourseNotesContract._ID,
                        CourseNotesContract.TEXT,
                        CourseNotesContract.TIMESTAMP
                },
                CourseClassTimeContract.COURSE_ID + " = ?",
                new String[] { mCourse.getId().toString() },
                null);
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> arg0, final Cursor arg1) {
        mAdapter.swapCursor(arg1);
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> arg0) {
        mAdapter.swapCursor(null);
    }

    private void startNewNoteIntent() {
        final Intent i = new Intent(getParentActivity(),
                AddCourseNoteActivity.class);

        i.putExtra(ParcelKeys.MOODLE_COURSE, new MoodleCourseParcel(mCourse));
        startActivity(i);
    }

    private void startViewNoteIntent(final int position) {

        final Cursor cursor = (Cursor) mAdapter.getItem(position);
        final NoteCursorWrapper noteWrapper = new NoteCursorWrapper(cursor,
                mCourse);

        final CourseNote note = noteWrapper.getNote();
        if(isLargeScreen)
        {
            mFragment = ViewCourseNoteFragment
                    .newInstance(mCourse, note);

            final FragmentTransaction transaction = activity.getSupportFragmentManager()
                    .beginTransaction();

            // Replace whatever is in the fragment_container view with this
            // fragment,
            transaction.replace(id.noteContainer, mFragment);

            // Commit the transaction
            transaction.commit();
        }
        else
        {
            final Intent i = new Intent(getParentActivity(),
                    ViewCourseNoteActivity.class);

            i.putExtra(ParcelKeys.MOODLE_COURSE_NOTE,
                    new MoodleCourseNoteParcel(note));
            startActivity(i);
        }

    }

    public void deleteNote()
    {
        mFragment.deleteNote();
    }

    /* ====================== Private Classes =================== */
    private static class NoteCursorWrapper {
        private final Cursor mCoursor;
        private final MoodleCourse mCourse;

        private Long cId;
        private Long cCourseId;
        private String cNotes;
        private Long cTimestamp;

        private CourseNote cNote;
        private DateTime cDateTime;

        private static final int COURSE_ID = 0;
        private static final int NOTE_ID = 1;
        private static final int NOTES_TEXT = 2;
        private static final int TIMESTAMP = 3;

        /**
         * @param coursor
         * @param course
         */
        public NoteCursorWrapper(final Cursor cursor,
                                 final MoodleCourse course) {
            super();
            mCoursor = cursor;
            mCourse = course;
        }

        /**
         * @return the Course Id
         */
        @SuppressWarnings("unused")
        public Long getCourseId() {
            if (cCourseId == null)
                cCourseId = mCoursor
                        .getLong(NoteCursorWrapper.COURSE_ID);
            return cCourseId;
        }

        /**
         * @return the Id
         */
        public Long getId() {
            if (cId == null)
                cId = mCoursor.getLong(NoteCursorWrapper.NOTE_ID);
            return cId;
        }

        /**
         * @return the Notes
         */
        public String getNoteText() {
            if (cNotes == null)
                cNotes = mCoursor
                        .getString(NoteCursorWrapper.NOTES_TEXT);
            return cNotes;
        }

        /**
         * @return the Timestamp
         */
        public Long getTimestamp() {
            if (cTimestamp == null)
                cTimestamp = mCoursor
                        .getLong(NoteCursorWrapper.TIMESTAMP);
            return cTimestamp;
        }

        public CourseNote getNote() {
            if (cNote == null)
                cNote = new CourseNote(getId(), mCourse, getNoteText(),
                        new DateTime(
                                getTimestamp()));

            return cNote;
        }

        public DateTime getDateTaken() {
            if (cDateTime == null)
                cDateTime = new DateTime(
                        getTimestamp());

            return cDateTime;
        }

    }

    private static class CourseNotesAdapter extends CursorAdapter {

        private final MoodleCourse mCourse;

        class ViewHolder implements SimpleViewHolder {
            TextView text;
            TextView date;
        }

        public CourseNotesAdapter(final MoodleCourse course,
                                  final Context context, final Cursor c,
                                  final int flags) {
            super(context, c, flags);

            mCourse = course;
        }

        @Override
        public View newView(final Context context, final Cursor c,
                            final ViewGroup root) {

            final LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View view = inflater.inflate(R.layout.list_item_course_note,
                    root,
                    false);

            final ViewHolder h = new ViewHolder();
            h.text = (TextView) view.findViewById(R.id.textview_note);
            h.date = (TextView) view.findViewById(R.id.textview_note_date);

            view.setTag(h);

            return view;
        }

        @Override
        public void bindView(final View v, final Context context,
                             final Cursor c) {
            final NoteCursorWrapper data = new NoteCursorWrapper(
                    c, mCourse);

            final ViewHolder h = (ViewHolder) v.getTag();

            h.text.setText(data.getNoteText());
            h.date.setText(DateFormatter.getShortDateTime(data.getDateTaken()));

        }
    }

    private static interface Loaders {
        int LoadCourseNotes = 0;
    }
}
