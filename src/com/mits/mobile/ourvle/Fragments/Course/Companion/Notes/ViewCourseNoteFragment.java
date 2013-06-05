/**
 * 
 */
package com.mits.mobile.ourvle.Fragments.Course.Companion.Notes;

import org.joda.time.DateTime;
import org.sourceforge.ah.android.utilities.Dialog.DialogCreator;
import org.sourceforge.ah.android.utilities.Dialog.DialogManager;
import org.sourceforge.ah.android.utilities.Plugins.BaseClass.PluggableFragment;
import org.sourceforge.ah.android.utilities.Widgets.Fragments.DialogFragmentBase;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.mits.mobile.ourvle.R;
import com.mits.mobile.ourvle.Classes.SharedConstants.ParcelKeys;
import com.mits.mobile.ourvle.Classes.DataLayer.CompanionEntities.CourseNote;
import com.mits.mobile.ourvle.Classes.DataLayer.Databases.ContentProviderContracts.CourseNotesContract;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Courses.MoodleCourse;
import com.mits.mobile.ourvle.Classes.Dialogs.ConfirmDeleteDialog;
import com.mits.mobile.ourvle.Classes.ParcableWrappers.MoodleCourseNoteParcel;
import com.mits.mobile.ourvle.Classes.ParcableWrappers.MoodleCourseParcel;

/**
 * @author Aston Hamilton
 * 
 */
public class ViewCourseNoteFragment extends PluggableFragment
	implements DialogCreator {
    private Listener mListener;

    private MoodleCourse mCourse;
    private CourseNote mCourseNote;

    private EditText mNotesTextEditText;

    public static ViewCourseNoteFragment newInstance(
	    final MoodleCourse course,
	    final CourseNote note) {
	final ViewCourseNoteFragment f = new ViewCourseNoteFragment();

	f.setMoodleCourse(course);
	f.setMoodleCourseNote(note);
	return f;
    }

    public void setMoodleCourse(final MoodleCourse course) {
	getFragmentArguments().putParcelable(ParcelKeys.MOODLE_COURSE,
		new MoodleCourseParcel(course));
	mCourse = course;
    }

    public void setMoodleCourseNote(final CourseNote note) {
	getFragmentArguments()
		.putParcelable(ParcelKeys.MOODLE_COURSE_NOTE,
			new MoodleCourseNoteParcel(note));

	mCourseNote = note;
    }

    @Override
    public void onAttach(final Activity activity) {
	super.onAttach(activity);
	mListener = (Listener) activity;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
	mCourseNote = ((MoodleCourseNoteParcel) getFragmentArguments()
		.getParcelable(ParcelKeys.MOODLE_COURSE_NOTE))
		.getWrappedObejct();

	mCourse = ((MoodleCourseParcel) getFragmentArguments()
		.getParcelable(ParcelKeys.MOODLE_COURSE))
		.getWrappedObejct();

	setHasOptionsMenu(true);
	super.onCreate(savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {

	if (mCourseNote == null)
	    inflater.inflate(R.menu.course_view_notes_fragment_menu_no_delete,
		    menu);
	else
	    inflater.inflate(R.menu.course_view_notes_fragment_menu, menu);

	super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
	switch (item.getItemId()) {
	case R.id.menu_save_note:
	    final String noteText = mNotesTextEditText.getText().toString()
		    .trim();

	    if (noteText.length() == 0) {
		Toast.makeText(getApplicationContext(), "Enter a note to save",
			Toast.LENGTH_LONG).show();
		return false;
	    }

	    final ContentResolver cr = getApplicationContext()
		    .getContentResolver();
	    if (mCourseNote == null) {
		mCourseNote = new CourseNote(-1, mCourse, noteText,
			new DateTime());

		final ContentValues values = new ContentValues();
		values.put(CourseNotesContract.COURSE_ID, mCourse.getId()
			.longValue());
		values.put(CourseNotesContract.TEXT, mCourseNote.getNote());
		values.put(CourseNotesContract.TIMESTAMP, mCourseNote
			.getDateTaken().getMillis());
		new AsyncQueryHandler(cr) {

		}.startInsert(0, null, CourseNotesContract.CONTENT_URI, values);

	    } else {
		mCourseNote.setNote(noteText);
		mCourseNote.setDateTime(new DateTime());

		final ContentValues values = new ContentValues();
		values.put(CourseNotesContract.TEXT, mCourseNote.getNote());
		values.put(CourseNotesContract.TIMESTAMP, mCourseNote
			.getDateTaken().getMillis());
		new AsyncQueryHandler(cr) {

		}.startUpdate(0, null, CourseNotesContract.CONTENT_URI, values,
			CourseNotesContract._ID + " = ?",
			new String[] { mCourseNote
				.getNoteId().toString() });

	    }
	    mListener.onNoteSaved(mCourseNote);
	    break;

	case R.id.menu_delete_note:
	    if (mCourseNote == null)
		throw new IllegalStateException(
			"Note cannot be null to delete.");

	    DialogManager.showDialog(Dialog.DELETE_DIALOG, this,
		    getFragmentManager());
	}

	return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
	    final ViewGroup container,
	    final Bundle savedInstanceState) {
	final View v = inflater.inflate(R.layout.fragment_view_course_note,
		container, false);

	mNotesTextEditText = (EditText) v.findViewById(R.id.edittext_note);

	if (mCourseNote != null)
	    mNotesTextEditText.setText(mCourseNote.getNote());
	return v;
    }

    @Override
    public DialogFragmentBase createDialog(final int id) {
	if (id == Dialog.DELETE_DIALOG)
	    return ConfirmDeleteDialog
		    .newInstance("Are you sure you want to delete this note?");

	return null;
    }

    public void deleteNote() {
	final ContentResolver cr = getApplicationContext()
		.getContentResolver();
	new AsyncQueryHandler(cr) {

	}.startDelete(0, null, CourseNotesContract.CONTENT_URI,
		CourseNotesContract._ID + " = ?", new String[] { mCourseNote
			.getNoteId().toString() });

	mListener.onNoteDeleted(mCourseNote);

    }

    /* =========================== Interfaces ======================= */
    public static interface Dialog {
	int DELETE_DIALOG = 0;
    }

    public static interface Listener {
	void onNoteSaved(CourseNote courseNote);

	void onNoteDeleted(CourseNote courseNote);
    }

}
