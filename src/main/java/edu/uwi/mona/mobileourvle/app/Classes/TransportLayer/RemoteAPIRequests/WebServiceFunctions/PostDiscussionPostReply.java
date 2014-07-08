/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.RemoteAPIRequests.WebServiceFunctions;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.UserSession;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.DiscussionPostReply;

/**
 * @author Aston Hamilton
 *
 * Abandaoned until natively supported by moodle
 */
public class PostDiscussionPostReply extends RemoteWebServiceFunction {
    public PostDiscussionPostReply(final UserSession session,
	    final DiscussionPostReply reply) {

	super("core_enrol_get_users_courses", session,
		"userid", session.getContext()
			.getCurretnUser().getId());

    }
}
