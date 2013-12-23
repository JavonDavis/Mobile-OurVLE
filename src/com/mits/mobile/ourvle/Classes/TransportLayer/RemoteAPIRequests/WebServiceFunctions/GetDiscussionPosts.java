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
	super("mbl_moodle_get_forum_discussions", userSession,
		"discussionid", discussion.getId().toString());
    }

}
