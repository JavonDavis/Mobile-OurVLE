/**
 * 
 */
package com.mits.mobile.ourvle.Classes.TransportLayer.RemoteAPIRequests.WebServiceFunctions;

import com.mits.mobile.ourvle.Classes.DataLayer.Authentication.Session.SessionKeyStore;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Courses.MoodleCourse;

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
