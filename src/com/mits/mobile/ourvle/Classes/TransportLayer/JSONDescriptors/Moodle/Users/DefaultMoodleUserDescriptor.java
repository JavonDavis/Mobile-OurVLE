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
 * 
 *         Currently I'm using the default Moodle API to fetch the participants
 *         but the Descriptor for general Moodle users different keys since they
 *         were my APIs and I used my own name for some fields. This JSON
 *         descriptor will describe my local MoodleUser model in the default
 *         Moodle API JSON specification until I properly proxy the API and
 *         strip out the unnecessary fields and align the field names with my
 *         own specification.
 */
public class DefaultMoodleUserDescriptor extends
	JSONObjectDescriptor<MoodleUser> {

    @Override
    public JsonElement getJsonElement(final MoodleUser object) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public MoodleUser getObjectFromJson(final JsonElement json) {
	final JsonObject jsonObject = (JsonObject) json;
	final String id = jsonObject.get("id").getAsString();
	final String firstName = jsonObject.get("firstname").getAsString();
	final String lastName = jsonObject.get("lastname").getAsString();
	final String pictureUrl = jsonObject.get("profileimageurl")
		.getAsString();

	return new MoodleUser(id, firstName, lastName, pictureUrl);
    }
}
