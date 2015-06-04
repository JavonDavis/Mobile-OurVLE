/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Fragments.Course.Companion.Notes;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Courses.MoodleCourse;

/**
 * @author Aston Hamilton
 * 
 */
public class AddCourseNoteFragment extends ViewCourseNoteFragment {

    public static AddCourseNoteFragment newInstance(
	    final MoodleCourse course) {
	final AddCourseNoteFragment f = new AddCourseNoteFragment();

	f.setMoodleCourse(course);
	// the ViewCourseNoteFragment was designed to be like add note when null
	// was passed
	f.setMoodleCourseNote(null);
	return f;
    }
}
