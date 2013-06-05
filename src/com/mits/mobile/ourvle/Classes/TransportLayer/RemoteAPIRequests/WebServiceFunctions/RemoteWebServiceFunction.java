/**
 * 
 */
package com.mits.mobile.ourvle.Classes.TransportLayer.RemoteAPIRequests.WebServiceFunctions;

import com.mits.mobile.ourvle.Classes.DataLayer.Authentication.Session.SessionKeyStore;
import com.mits.mobile.ourvle.Classes.TransportLayer.APIEndpoints;
import com.mits.mobile.ourvle.Classes.TransportLayer.RemoteAPIRequests.RemoteAPIRequest;

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
