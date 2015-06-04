/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.RemoteAPIRequests.WebServiceFunctions;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.SessionKeyStore;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Courses.MoodleCourse;

/**
 * @author Aston Hamilton
 * 
 */
public class GetCourseContents extends RemoteWebServiceFunction {

    public GetCourseContents(final SessionKeyStore userSession,
	    final MoodleCourse course) {
	super("core_course_get_contents", userSession,
		"courseid", course.getId().toString());
    }

}
