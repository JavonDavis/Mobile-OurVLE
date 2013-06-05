/**
 * 
 */
package com.mits.mobile.ourvle.Classes.Util;

import org.sourceforge.ah.android.utilities.Widgets.ObjectSerializer.ObjectSerializer;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mits.mobile.ourvle.Classes.DataLayer.Authentication.Session.UserSession;
import com.mits.mobile.ourvle.Classes.SerializableWrappers.UserSessionSerializableDescriptor;

/**
 * @author aston
 * 
 */
public class ApplicationDataManager {

    private static final String USER_SESSION = "com.mits.mobile.ourvle.Classes.Util.ApplicationDataManager.User_Session";

    public static boolean saveLastUserSession(final Context context,
	    final UserSession session) {
	final SharedPreferences prefs = PreferenceManager
		.getDefaultSharedPreferences(context);

	final String seializedUserSession = ObjectSerializer
		.getSerializedObject(new UserSessionSerializableDescriptor(),
			session);

	return prefs
		.edit()
		.putString(ApplicationDataManager.USER_SESSION,
			seializedUserSession)
		.commit();
    }

    public static UserSession getLastUserSession(final Context context) {

	final SharedPreferences prefs = PreferenceManager
		.getDefaultSharedPreferences(context);

	final String serializedUserSession = prefs.getString(
		ApplicationDataManager.USER_SESSION, null);

	if (serializedUserSession == null)
	    return null;

	return ObjectSerializer.getObject(
		new UserSessionSerializableDescriptor(),
		serializedUserSession);
    }
}
