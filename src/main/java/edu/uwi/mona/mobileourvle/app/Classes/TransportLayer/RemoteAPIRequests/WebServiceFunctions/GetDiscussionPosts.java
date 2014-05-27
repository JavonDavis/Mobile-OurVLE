/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.RemoteAPIRequests.WebServiceFunctions;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.SessionKeyStore;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.ForumDiscussion;

/**
 * @author Aston Hamilton
 * 
 */
public class GetDiscussionPosts extends RemoteWebServiceFunction {

    public GetDiscussionPosts(final ForumDiscussion discussion,
	    final SessionKeyStore userSession) {
	super("mod_forum_get_forum_discussion_posts", userSession,
		"discussionid", discussion.getId().toString());
    }

}
