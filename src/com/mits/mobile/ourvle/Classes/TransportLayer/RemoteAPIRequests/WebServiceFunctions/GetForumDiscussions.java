/**
 * 
 */
package com.mits.mobile.ourvle.Classes.TransportLayer.RemoteAPIRequests.WebServiceFunctions;

import com.mits.mobile.ourvle.Classes.DataLayer.Authentication.Session.SessionKeyStore;
import com.mits.mobile.ourvle.Classes.DataLayer.Authentication.Session.UserSession;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.CourseModule;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.CourseForum;

/**
 * @author Aston Hamilton
 * 
 */
public class GetForumDiscussions extends RemoteWebServiceFunction {

    public GetForumDiscussions(final CourseForum forum,
	    final SessionKeyStore userSession) {
	super("mod_forum_get_forum_discussions", userSession,
		"forumids[0]", forum.getForumid().toString());
    }

    public GetForumDiscussions(final CourseModule module,
	    final UserSession userSession) {
	super("mod_forum_get_forum_discussions",
		userSession,
		"forumids[0]", module.getId().toString());
    }

}
