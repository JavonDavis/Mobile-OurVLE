/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.RemoteAPIRequests.WebServiceFunctions;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.SessionKeyStore;
import edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.APIEndpoints;
import edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.RemoteAPIRequests.RemoteAPIRequest;

/**
 * @author Aston Hamilton
 * 
 */
public abstract class RemoteWebServiceFunction extends RemoteAPIRequest {

    /**
     * @param objects
     */
    public RemoteWebServiceFunction(final String functionName,
	    final SessionKeyStore userSession, final Object... objects) {
	super(objects);

	addRequestArg("wsfunction", functionName);
	addRequestArg("moodlewsrestformat", "json");
	addRequestArg("wstoken", userSession.getSessionKey());
    }

    @Override
    public String getEndpoint() {
	return APIEndpoints.Shared.REST_API_ENDPOINT;
    }
}
