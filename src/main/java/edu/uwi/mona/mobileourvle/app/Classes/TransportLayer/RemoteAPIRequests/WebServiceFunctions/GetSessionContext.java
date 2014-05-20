/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.RemoteAPIRequests.WebServiceFunctions;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.SessionKeyStore;

/**
 * @author Aston Hamilton
 * 
 */
public class GetSessionContext extends RemoteWebServiceFunction {

    /**
     * @param userSession
     */
    public GetSessionContext(final SessionKeyStore userSession) {
	super("core_webservice_get_site_info", userSession);
    }
}
