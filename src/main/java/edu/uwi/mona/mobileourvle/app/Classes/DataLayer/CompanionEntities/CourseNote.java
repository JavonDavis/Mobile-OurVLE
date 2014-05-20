/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Classes.DataLayer.CompanionEntities;

import org.joda.time.DateTime;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Courses.MoodleCourse;

/**
 * @author Aston Hamilton
 * 
 */
public class CourseNote {
    private final long noteId;
    private final MoodleCourse course;
    private String note;
    private DateTime dateTaken;

    /**
     * @param noteId
     * @param course
     * @param note
     * @param dateTaken
     */
    public CourseNote(final long noteId, final MoodleCourse course,
	    final String note,
	    final DateTime dateTaken) {
	super();
	this.noteId = noteId;
	this.course = course;
	this.note = note;
	this.dateTaken = dateTaken;
    }

    /**
     * @return the noteId
     */
    public Long getNoteId() {
	return noteId;
    }

    /**
     * @return the course
     */
    public MoodleCourse getCourse() {
	return course;
    }

    /**
     * @return the note
     */
    public String getNote() {
	return note;
    }

    public void setNote(final String noteText) {
	note = noteText;
    }

    /**
     * @return the dateTaken
     */
    public DateTime getDateTaken() {
	return dateTaken;
    }

    public void setDateTime(final DateTime dateTime) {
	dateTaken = dateTime;
    }

}
