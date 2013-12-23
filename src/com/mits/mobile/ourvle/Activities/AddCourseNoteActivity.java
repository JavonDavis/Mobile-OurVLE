/**
 *
 */
package com.mits.mobile.ourvle.Activities;

import org.joda.time.DateTime;
import org.sourceforge.ah.android.utilities.Formatters.DateFormatter;
import org.sourceforge.ah.android.utilities.Widgets.Activities.ActivityBase;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.mits.mobile.ourvle.R;
import com.mits.mobile.ourvle.Classes.SharedConstants.ParcelKeys;
import com.mits.mobile.ourvle.Classes.DataLayer.CompanionEntities.CourseNote;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Courses.MoodleCourse;
import com.mits.mobile.ourvle.Classes.Dialogs.ConfirmDeleteDialog;
import com.mits.mobile.ourvle.Classes.ParcableWrappers.MoodleCourseParcel;
import com.mits.mobile.ourvle.Fragments.Course.Companion.Notes.AddCourseNoteFragment;
import com.mits.mobile.ourvle.Fragments.Course.Companion.Notes.ViewCourseNoteFragment;

/**
 * @author Aston Hamilton
 */
public class AddCourseNoteActivity extends ActivityBase
        implements ConfirmDeleteDialog.Listener,
        ViewCourseNoteFragment.Listener {
    private MoodleCourse mCourse;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_fragment);

        final Bundle extras = getIntent().getExtras();

        mCourse = ((MoodleCourseParcel) extras
                .get(ParcelKeys.MOODLE_COURSE)).getWrappedObejct();

        setTitle(DateFormatter.getLongDateTime(new DateTime()));

        final AddCourseNoteFragment fragment = AddCourseNoteFragment
                .newInstance(mCourse);

        final FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        // Replace whatever is in the fragment_container view with this
        // fragment,
        transaction.replace(R.id.fragment, fragment);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void onNoteSaved(final CourseNote courseNote) {
        Toast.makeText(getApplicationContext(), "Note saved", Toast.LENGTH_LONG)
             .show();

        finish();
    }

    /*
     * I have to define all these callbacks because I'm too lazy to do the "add
     * note fragment" properly. I'll clean it up in a later release :)
     */
    @Override
    public void onNoteDeleted(final CourseNote courseNote) {
        throw new IllegalStateException("Cannot delete new note.");
    }

    @Override
    public void onPositiveClicked() {
        throw new IllegalStateException("Cannot delete new note.");
    }

    @Override
    public void onNegativeClicked() {
        throw new IllegalStateException("Cannot delete new note.");
    }
}
