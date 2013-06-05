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
	super("mobile_mdl_get_forum_discussions_with_last_post", userSession,
		"forumid", forum.getForumid().toString());
    }

    public GetForumDiscussions(final CourseModule module,
	    final UserSession userSession) {
	super("mobile_mdl_get_forum_module_discussions_with_last_post",
		userSession,
		"moduleid", module.getId().toString());
    }

}
