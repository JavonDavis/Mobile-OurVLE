/**
 * 
 */
package com.mits.mobile.ourvle.Classes.DataLayer.Authentication.Session;

/**
 * The Class UserSession.
 * 
 * @author Aston Hamilton
 */
public class UserSession implements SessionKeyStore {

    /** The session key. */
    private final String sessionKey;

    private SessionContext context;

    /**
     * @param sessionKey
     * @param context
     */
    public UserSession(final String sessionKey, final SessionContext context) {
	super();
	this.sessionKey = sessionKey;
	this.context = context;
    }

    /**
     * @return the sessionKey
     */
    @Override
    public String getSessionKey() {
	return sessionKey;
    }

    /**
     * @return the context
     */
    public SessionContext getContext() {
	return context;
    }

    /**
     * @param context
     *            the context to set
     */
    public void setContext(final SessionContext context) {
	this.context = context;
    }

}
