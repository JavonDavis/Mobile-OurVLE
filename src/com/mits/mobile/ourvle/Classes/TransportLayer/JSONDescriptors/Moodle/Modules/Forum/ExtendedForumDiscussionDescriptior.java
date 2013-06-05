/**
 * 
 */
package com.mits.mobile.ourvle.Classes.TransportLayer.JSONDescriptors.Moodle.Modules.Forum;

import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONDecoder;
import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONObjectDescriptor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.DiscussionParent;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.ExtendedForumDiscussion;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.ForumDiscussion;

// TODO: Auto-generated Javadoc
/**
 * The Class UserSessionDescriptior.
 * 
 * @author Aston Hamilton
 */
public class ExtendedForumDiscussionDescriptior extends
	JSONObjectDescriptor<ExtendedForumDiscussion> {

    private final DiscussionParent parent;

    public ExtendedForumDiscussionDescriptior(final DiscussionParent parent) {
	this.parent = parent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.mits.mobile.ourvle.Classes.CommunicationBase.JSONFactory.
     * JSONObjectDescriptor#getJsonElement(java.lang.Object)
     */
    @Override
    public JsonElement getJsonElement(final ExtendedForumDiscussion object) {
	final JsonObject obj = (JsonObject) new ForumDiscussionDescriptior(
		parent).getJsonElement(object);

	final JsonObject lastPostObj = new JsonObject();

	lastPostObj.addProperty("id", object.getLastPostId());
	lastPostObj.addProperty("message", object.getLastPortMessage());

	obj.add("last_post", lastPostObj);
	return obj;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.mits.mobile.ourvle.Classes.CommunicationBase.JSONFactory.
     * JSONObjectDescriptor#getObjectFromJson(com.google.gson.JsonElement)
     */
    @Override
    public ExtendedForumDiscussion getObjectFromJson(final JsonElement json) {
	final JsonObject jsonObject = (JsonObject) json;

	final ForumDiscussion discussion = JSONDecoder
		.getObject(
			new ForumDiscussionDescriptior(parent), jsonObject);

	final JsonObject lastPostJsonObject = jsonObject.get("last_post")
		.getAsJsonObject();

	final int lastPostId = lastPostJsonObject.get("id").getAsInt();
	final String lastPostMessage = lastPostJsonObject.get("message")
		.getAsString();

	return new ExtendedForumDiscussion(discussion, lastPostId,
		lastPostMessage, parent);
    }
}
