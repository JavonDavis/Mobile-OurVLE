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
public class GetCourseParticipants extends RemoteWebServiceFunction {

    public GetCourseParticipants(final SessionKeyStore userSession,
	    final MoodleCourse course) {
	super("core_enrol_get_enrolled_users", userSession,
		"courseid", course.getId().toString());
    }

}
