/**
 * 
 */
package com.mits.mobile.ourvle.Classes.TransportLayer.JSONDescriptors.Authentication;

import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONDecoder;
import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONObjectDescriptor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mits.mobile.ourvle.Classes.DataLayer.Authentication.Session.SessionContext;
import com.mits.mobile.ourvle.Classes.DataLayer.Authentication.Session.SiteInfo;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Users.MoodleUser;
import com.mits.mobile.ourvle.Classes.TransportLayer.JSONDescriptors.Moodle.Users.MoodleUserDescriptor;

/**
 * @author Aston Hamilton
 * 
 */
public class SessionContextDescriptor extends
	JSONObjectDescriptor<SessionContext> {

    @Override
    public JsonElement getJsonElement(final SessionContext arg0) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public SessionContext getObjectFromJson(final JsonElement arg0) {
	final JsonObject jsonObject = (JsonObject) arg0;

	final JsonObject currentUserJson = jsonObject.get("user")
		.getAsJsonObject();
	final JsonObject siteInfoJson = jsonObject.get("site")
		.getAsJsonObject();

	final MoodleUser currentUser = (MoodleUser) JSONDecoder.getObject(
		new MoodleUserDescriptor(), currentUserJson);

	final SiteInfo siteInfo = (SiteInfo) JSONDecoder.getObject(
		new SiteInfoDescriptor(), siteInfoJson);

	return new SessionContext(siteInfo, currentUser);
    }

}
