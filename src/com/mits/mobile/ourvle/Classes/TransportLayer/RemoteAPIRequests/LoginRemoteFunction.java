/**
 * 
 */
package com.mits.mobile.ourvle.Classes.TransportLayer.RemoteAPIRequests;

import com.mits.mobile.ourvle.Classes.TransportLayer.APIEndpoints;

/**
 * @author Aston Hamilton
 * 
 */
public class LoginRemoteFunction extends RemoteAPIRequest {
    public LoginRemoteFunction(final String username, final String password) {
	super("username", username, "password", password, "service",
		"moodle_mobile_ourvle");
    }

    @Override
    public String getEndpoint() {
	return APIEndpoints.Authentication.LOGIN_ENDPOINT;
    }
}
