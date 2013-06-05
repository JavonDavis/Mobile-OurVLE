/**
 * 
 */
package com.mits.mobile.ourvle.Classes.TransportLayer.RemoteAPIRequests.WebServiceFunctions;

import com.mits.mobile.ourvle.Classes.DataLayer.Authentication.Session.SessionKeyStore;
import com.mits.mobile.ourvle.Classes.DataLayer.Authentication.Session.UserSession;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Courses.MoodleCourse;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Users.MoodleUser;

/**
 * @author Aston Hamilton
 * 
 */
public class GetUserProfile extends RemoteWebServiceFunction {

    public GetUserProfile(final SessionKeyStore userSession,
	    final MoodleUser user) {
	super("core_user_get_users_by_id", userSession,
		"userids[0]", user.getId().toString());
    }

    public GetUserProfile(final UserSession userSession,
	    final MoodleUser user, final MoodleCourse course) {
	super("core_user_get_course_user_profiles", userSession,
		"userlist[0][userid]", user.getId().toString(),
		"userlist[0][courseid]", course.getId().toString());
    }

}
