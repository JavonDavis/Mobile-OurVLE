/**
 * 
 */
package com.mits.mobile.ourvle.Classes.TransportLayer.JSONDescriptors.Authentication;

import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONDecoder;
import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONObjectDescriptor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mits.mobile.ourvle.Classes.DataLayer.Authentication.Session.SiteInfo;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.CourseForum;
import com.mits.mobile.ourvle.Classes.TransportLayer.JSONDescriptors.Moodle.Modules.Forum.CourseForumDescriptior;

/**
 * @author Aston Hamilton
 * 
 */
public class SiteInfoDescriptor extends JSONObjectDescriptor<SiteInfo> {

    @Override
    public JsonElement getJsonElement(final SiteInfo arg0) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public SiteInfo getObjectFromJson(final JsonElement arg0) {
	final JsonObject jsonObject = (JsonObject) arg0;

	final String name = jsonObject.get("name").getAsString();
	final String url = jsonObject.get("url").getAsString();
	final int frontPageId = jsonObject.get("frontpage_id").getAsInt();
	final JsonObject newsForumJsonObject = jsonObject.get("news_forum")
		.getAsJsonObject();

	final CourseForum newsForum = (CourseForum) JSONDecoder.getObject(
		new CourseForumDescriptior(), newsForumJsonObject);
	return new SiteInfo(name, frontPageId, newsForum, url);
    }

}
