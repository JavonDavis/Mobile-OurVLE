/**
 * 
 */
package com.mits.mobile.ourvle.Classes.TransportLayer.RemoteAPIRequests.WebServiceFunctions;

import com.mits.mobile.ourvle.Classes.DataLayer.Authentication.Session.SessionKeyStore;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.ForumDiscussion;

/**
 * @author Aston Hamilton
 * 
 */
public class GetDiscussionPosts extends RemoteWebServiceFunction {

    public GetDiscussionPosts(final ForumDiscussion discussion,
	    final SessionKeyStore userSession) {
	super("mobile_mdl_get_discussion_posts", userSession,
		"discussionid", discussion.getId().toString());
    }

}
