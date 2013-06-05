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
public class GetCourseContents extends RemoteWebServiceFunction {

    public GetCourseContents(final SessionKeyStore userSession,
	    final MoodleCourse course) {
	super("core_course_get_contents", userSession,
		"courseid", course.getId().toString());
    }

}
