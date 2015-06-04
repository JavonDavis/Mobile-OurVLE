/**
 *
 */
package edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.RemoteAPIRequests.WebServiceFunctions;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.SessionKeyStore;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.UserSession;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Courses.MoodleCourse;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.CourseModule;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.CourseForum;

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
