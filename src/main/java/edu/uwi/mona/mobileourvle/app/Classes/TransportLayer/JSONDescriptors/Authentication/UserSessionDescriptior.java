/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.JSONDescriptors.Authentication;

import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONObjectDescriptor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.UserSession;

// TODO: Auto-generated Javadoc
/**
 * The Class UserSessionDescriptior.
 * 
 * @author Aston Hamilton
 */
public class UserSessionDescriptior extends JSONObjectDescriptor<UserSession> {

    /*
     * (non-Javadoc)
     * 
     * @see edu.uwi.mona.mobileourvle.app.Classes.CommunicationBase.JSONFactory.
     * JSONObjectDescriptor#getJsonElement(java.lang.Object)
     */
    @Override
    public JsonElement getJsonElement(final UserSession object) {
	final JsonObject json = new JsonObject();
	json.addProperty("token", object.getSessionKey());
	return json;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uwi.mona.mobileourvle.app.Classes.CommunicationBase.JSONFactory.
     * JSONObjectDescriptor#getObjectFromJson(com.google.gson.JsonElement)
     */
    @Override
    public UserSession getObjectFromJson(final JsonElement json) {
	final JsonObject jsonObject = (JsonObject) json;

	return new UserSession(jsonObject.get("token").getAsString(), null);
    }

}
