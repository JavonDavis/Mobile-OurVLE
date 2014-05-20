/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Classes.SerializableWrappers;

import org.sourceforge.ah.android.utilities.Widgets.ObjectSerializer.ObjectSerializerDescriptor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.SessionContext;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.UserSession;

/**
 * The Class UserSessionParcel.
 * 
 * @author Aston Hamilton
 */
public class UserSessionSerializableDescriptor extends
	ObjectSerializerDescriptor<UserSession> {
    @Override
    public JsonElement serialzeObject(final UserSession unserializedObj) {
	final JsonObject serializedUserSession = new JsonObject();

	serializedUserSession.addProperty("key",
		unserializedObj.getSessionKey());
	serializedUserSession.add("context",
		new SessionContextSerializableDescriptor()
			.serialzeObject(unserializedObj
				.getContext()));

	return serializedUserSession;
    }

    @Override
    public UserSession deserialzeObject(final JsonElement serializedObject) {
	final JsonObject serializedObjectObj = (JsonObject) serializedObject;
	final String sessionToken = serializedObjectObj.get("key")
		.getAsString();

	final SessionContext sessionContext = new SessionContextSerializableDescriptor()
		.deserialzeObject(serializedObjectObj.get("context"));

	return new UserSession(sessionToken,
		sessionContext);
    }
}
