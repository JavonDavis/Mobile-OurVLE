/**
 *
 */
package com.mits.mobile.ourvle.Classes.TransportLayer.RemoteAPIRequests.WebServiceFunctions;

import com.mits.mobile.ourvle.Classes.DataLayer.Authentication.Session.SessionKeyStore;
import com.mits.mobile.ourvle.Classes.DataLayer.Authentication.Session.UserSession;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Courses.MoodleCourse;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.CourseModule;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.CourseForum;

/**
 * @author Aston Hamilton
 */
public class GetCourseDiscussions extends RemoteWebServiceFunction {

    public GetCourseDiscussions(final MoodleCourse course,
                                final SessionKeyStore userSession) {
        this(course.getId().toString(), userSession);
    }

    public GetCourseDiscussions(final String courseId,
                                final SessionKeyStore userSession) {
        super("mod_forum_get_forums_by_courses", userSession,
              "courseids[0]", courseId);
    }
}
