/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Courses;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Users.CourseManager;

/**
 * @author Aston Hamilton
 * 
 */
public class ExtendedMoodleCourse extends MoodleCourse {
    private final CourseManager[] managers;

    /**
     * @param id
     * @param name
     * @param shortName
     * @param managers
     */
    public ExtendedMoodleCourse(final Long id, final String name,String shortName,
	    final CourseManager... managers) {
	super(id, name, shortName);
	this.managers = managers;
    }

    /**
     * @return the managers
     */
    public CourseManager[] getManagers() {
	return managers;
    }

}
