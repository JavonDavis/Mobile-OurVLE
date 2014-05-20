/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Classes.DataLayer.CompanionEntities;

import java.io.File;

import org.joda.time.DateTime;

import android.net.Uri;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Android.PhonePhoto;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Courses.MoodleCourse;

/**
 * @author Aston Hamilton
 * 
 */
public class CoursePhoto extends PhonePhoto {
    private final String notes;
    private final DateTime dateTaken;
    private final MoodleCourse course;

    /**
     * @param mContext
     * @param uri
     * @param notes
     * @param dateTaken
     * @param course
     */
    public CoursePhoto(final Uri uri,
	    final DateTime dateTaken, final MoodleCourse course,
	    final String notes) {
	super(uri);

	this.notes = notes;
	this.dateTaken = dateTaken;
	this.course = course;
    }

    public CoursePhoto(final File photoFile,
	    final DateTime dateTaken, final MoodleCourse course,
	    final String notes) {
	this(Uri.fromFile(photoFile), dateTaken, course, notes);
    }

    /**
     * @return the notes
     */
    public String getNotes() {
	return notes;
    }

    /**
     * @return the dateTaken
     */
    public DateTime getDateTaken() {
	return dateTaken;
    }

    /**
     * @return the course
     */
    public MoodleCourse getCourse() {
	return course;
    }

}
