/**
 * 
 */
package com.mits.mobile.ourvle.Classes.SerializableWrappers;

import org.sourceforge.ah.android.utilities.Widgets.ObjectSerializer.ObjectSerializerDescriptor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Users.MoodleUser;

/**
 * @author Aston Hamilton
 * 
 */
public class MoodleUserSerializableDescriptor extends
	ObjectSerializerDescriptor<MoodleUser> {

    @Override
    public JsonElement serialzeObject(final MoodleUser unserializedObj) {
	final JsonObject serializedUser = new JsonObject();

	serializedUser.addProperty("id", unserializedObj.getId());
	serializedUser.addProperty("fname", unserializedObj.getFirstName());
	serializedUser.addProperty("lname", unserializedObj.getLastName());
	serializedUser.addProperty("ppic", unserializedObj.getPictureUrl());

	return serializedUser;
    }

    @Override
    public MoodleUser deserialzeObject(final JsonElement serializedObject) {

	final JsonObject serializedObjectObj = (JsonObject) serializedObject;

	final String userId = serializedObjectObj.get("id").getAsString();
	final String firstName = serializedObjectObj.get("fname").getAsString();
	final String lastName = serializedObjectObj.get("lname").getAsString();
	final String picUrl = serializedObjectObj.get("ppic").getAsString();

	return new MoodleUser(userId, firstName, lastName, picUrl);
    }

}
