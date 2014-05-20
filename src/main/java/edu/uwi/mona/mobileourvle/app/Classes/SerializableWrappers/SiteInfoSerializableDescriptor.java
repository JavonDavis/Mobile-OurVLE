/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Classes.SerializableWrappers;

import org.sourceforge.ah.android.utilities.Widgets.ObjectSerializer.ObjectSerializerDescriptor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.SiteInfo;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.CourseForum;

/**
 * @author Aston Hamilton
 * 
 */
public class SiteInfoSerializableDescriptor extends
	ObjectSerializerDescriptor<SiteInfo> {

    @Override
    public JsonElement serialzeObject(final SiteInfo unserializedObj) {
	final JsonObject serializedSiteInfo = new JsonObject();

	serializedSiteInfo.addProperty("name", unserializedObj.getName());
	serializedSiteInfo.addProperty("fid", unserializedObj.getFrontPageId());
	serializedSiteInfo.add("nforum",
		new CourseForumSerializableDescriptor()
			.serialzeObject(unserializedObj.getNewsForum()));
	serializedSiteInfo.addProperty("url", unserializedObj.getUrl());

	return serializedSiteInfo;
    }

    @Override
    public SiteInfo deserialzeObject(final JsonElement serializedObject) {
	final JsonObject serializedObjectObj = (JsonObject) serializedObject;
	final String siteName = serializedObjectObj.get("name").getAsString();
	final int fronPageId = serializedObjectObj.get("fid").getAsInt();
	final CourseForum newsForum = new CourseForumSerializableDescriptor()
		.deserialzeObject(serializedObjectObj.get("nforum"));
	final String siteUrl = serializedObjectObj.get("url").getAsString();

	return new SiteInfo(siteName, fronPageId,
		newsForum, siteUrl);
    }
}
