/**
 * 
 */
package com.mits.mobile.ourvle.Classes.TransportLayer.JSONDescriptors.Moodle.Modules.Forum;

import org.joda.time.DateTime;
import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONObjectDescriptor;
import org.sourceforge.ah.android.utilities.Formatters.DateFormatter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.CourseForum;

// TODO: Auto-generated Javadoc
/**
 * The Class UserSessionDescriptior.
 * 
 * @author Aston Hamilton
 */
public class CourseForumDescriptior extends
	JSONObjectDescriptor<CourseForum> {

    @Override
    public JsonElement getJsonElement(final CourseForum object) {
	final JsonObject obj = new JsonObject();

	obj.addProperty("id", object.getForumid());
	obj.addProperty("name", object.getName());
	obj.addProperty("intro", object.getIntro());
	obj.addProperty("modified",
		DateFormatter.getISODateString(object.getLastModified()));

	return obj;
    }

    @Override
    public CourseForum getObjectFromJson(final JsonElement json) {
	final JsonObject jsonObject = (JsonObject) json;

	final long id = jsonObject.get("id").getAsLong();
	final String name = jsonObject.get("name").getAsString();
	final String intro = jsonObject.get("intro").getAsString();

	final String lastModifiedDateTimeString = jsonObject.get("modified")
		.getAsString();

	final DateTime lastModified = DateFormatter
		.getDateTimeFromISOString(lastModifiedDateTimeString);
	return new CourseForum(id, name, intro, lastModified);
    }
}
