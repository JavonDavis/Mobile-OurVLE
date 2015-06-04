/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Classes.SerializableWrappers;

import org.sourceforge.ah.android.utilities.Widgets.ObjectSerializer.ObjectSerializerDescriptor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.SessionContext;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.SiteInfo;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Users.MoodleUser;

/**
 * @author Aston Hamilton
 * 
 */
public class SessionContextSerializableDescriptor extends
	ObjectSerializerDescriptor<SessionContext> {

    @Override
    public JsonElement serialzeObject(final SessionContext unserializedObj) {
	final JsonObject serializedSessionContext = new JsonObject();

	final JsonElement serializedUser = new MoodleUserSerializableDescriptor()
		.serialzeObject(unserializedObj.getCurretnUser());

	final JsonElement serializedSiteInfo = new SiteInfoSerializableDescriptor()
		.serialzeObject(unserializedObj.getSiteInfo());

	serializedSessionContext.add("user", serializedUser);
	serializedSessionContext.add("site_info", serializedSiteInfo);

	return serializedSessionContext;
    }

    @Override
    public SessionContext deserialzeObject(final JsonElement serializedObject) {
	final JsonObject serializedObjectObj = (JsonObject) serializedObject;

	final MoodleUser moodleUser = new MoodleUserSerializableDescriptor()
		.deserialzeObject(serializedObjectObj.get("user"));
	final SiteInfo siteInfo = new SiteInfoSerializableDescriptor()
		.deserialzeObject(serializedObjectObj.get("site_info"));

	return new SessionContext(siteInfo, moodleUser);
    }
}
