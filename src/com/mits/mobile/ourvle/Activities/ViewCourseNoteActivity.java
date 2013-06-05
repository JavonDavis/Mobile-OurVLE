/**
 * 
 */
package com.mits.mobile.ourvle.Activities;

import org.sourceforge.ah.android.utilities.Formatters.DateFormatter;
import org.sourceforge.ah.android.utilities.Widgets.Activities.ActivityBase;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.mits.mobile.ourvle.R;
import com.mits.mobile.ourvle.Classes.SharedConstants.ParcelKeys;
import com.mits.mobile.ourvle.Classes.DataLayer.CompanionEntities.CourseNote;
import com.mits.mobile.ourvle.Classes.Dialogs.ConfirmDeleteDialog;
import com.mits.mobile.ourvle.Classes.ParcableWrappers.MoodleCourseNoteParcel;
import com.mits.mobile.ourvle.Fragments.Course.Companion.Notes.ViewCourseNoteFragment;

/**
 * @author Aston Hamilton
 * 
 */
public class ViewCourseNoteActivity extends ActivityBase
	implements ConfirmDeleteDialog.Listener,
	ViewCourseNoteFragment.Listener {
    private CourseNote mCourseNote;

    private ViewCourseNoteFragment mFragment;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_simple_fragment);

	final Bundle extras = getIntent().getExtras();

	mCourseNote = ((MoodleCourseNoteParcel) extras
		.get(ParcelKeys.MOODLE_COURSE_NOTE)).getWrappedObejct();

	setTitle(DateFormatter.getLongDateTime(mCourseNote.getDateTaken()));

	/*
	 * Stupid design after trying to cut shortcuts forces me to pass the
	 * course
	 * 
	 * Will be fixed in next version cuz I'm tired of looking at this code
	 * right now :(
	 */
	mFragment = ViewCourseNoteFragment
		.newInstance(mCourseNote.getCourse(), mCourseNote);

	final FragmentTransaction transaction = getSupportFragmentManager()
		.beginTransaction();

	// Replace whatever is in the fragment_container view with this
	// fragment,
	transaction.replace(R.id.fragment, mFragment);

	// Commit the transaction
	transaction.commit();
    }

    @Override
    public void onNoteSaved(final CourseNote courseNote) {
	Toast.makeText(getApplicationContext(), "Note saved", Toast.LENGTH_LONG)
		.show();

	finish();
    }

    @Override
    public void onNoteDeleted(final CourseNote courseNote) {
	Toast.makeText(getApplicationContext(), "Note deleted",
		Toast.LENGTH_LONG)
		.show();

	finish();
    }

    @Override
    public void onPositiveClicked() {
	mFragment.deleteNote();
    }

    @Override
    public void onNegativeClicked() {
	// do nothing
    }
}
