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
	super("mbl_moodle_get_forum_discussions", userSession,
		"discussionid", discussion.getId().toString());
    }

}
