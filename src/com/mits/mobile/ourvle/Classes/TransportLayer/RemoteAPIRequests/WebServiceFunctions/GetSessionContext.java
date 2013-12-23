/**
 * 
 */
package com.mits.mobile.ourvle.Classes.TransportLayer.RemoteAPIRequests.WebServiceFunctions;

import com.mits.mobile.ourvle.Classes.DataLayer.Authentication.Session.SessionKeyStore;

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