/**
 *
 */
package edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.RemoteAPIRequests.WebServiceFunctions;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.SessionKeyStore;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.UserSession;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.CourseModule;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.CourseForum;

/**
 * @author Aston Hamilton
 */
public class GetForumDiscussions extends RemoteWebServiceFunction {

    public GetForumDiscussions(final CourseForum forum,
                               final SessionKeyStore userSession) {
        this(forum.getForumid().toString(), userSession);
    }

    public GetForumDiscussions(final String forumId,
                               final SessionKeyStore userSession) {
        super("mod_forum_get_forum_discussions", userSession,
              "forumids[0]", forumId);
    }

}
