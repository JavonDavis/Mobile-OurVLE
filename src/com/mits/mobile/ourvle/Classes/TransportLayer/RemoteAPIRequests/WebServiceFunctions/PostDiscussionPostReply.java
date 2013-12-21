/**
 * 
 */
package com.mits.mobile.ourvle.Classes.TransportLayer.RemoteAPIRequests.WebServiceFunctions;

import com.mits.mobile.ourvle.Classes.DataLayer.Authentication.Session.UserSession;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.DiscussionPostReply;

/**
 * @author Aston Hamilton
 * 
 */
public class PostDiscussionPostReply extends RemoteWebServiceFunction {
    public PostDiscussionPostReply(final UserSession session,
	    final DiscussionPostReply reply) {

	super("core_enrol_get_users_courses", session,
		"userid", session.getContext()
			.getCurretnUser().getId());
    }
}
