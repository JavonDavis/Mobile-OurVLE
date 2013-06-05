/**
 * 
 */
package com.mits.mobile.ourvle.Classes.TransportLayer.JSONDescriptors.Moodle.Users;

import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONObjectDescriptor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Users.MoodleUser;

/**
 * @author Aston Hamilton
 * 
 */
public class MoodleUserDescriptor extends JSONObjectDescriptor<MoodleUser> {

    @Override
    public JsonElement getJsonElement(final MoodleUser object) {
	final JsonObject obj = new JsonObject();

	obj.addProperty("id", object.getId());
	obj.addProperty("firstname", object.getLastName());
	obj.addProperty("lastname", object.getFirstName());
	obj.addProperty("profilepicurl", object.getPictureUrl());

	return obj;
    }

    @Override
    public MoodleUser getObjectFromJson(final JsonElement json) {
	final JsonObject jsonObject = (JsonObject) json;
	final String id = jsonObject.get("id").getAsString();
	final String firstName = jsonObject.get("firstname").getAsString();
	final String lastName = jsonObject.get("lastname").getAsString();
	final String pictureUrl = jsonObject.get("profilepicurl")
		.getAsString();

	return new MoodleUser(id, firstName, lastName, pictureUrl);
    }
}
