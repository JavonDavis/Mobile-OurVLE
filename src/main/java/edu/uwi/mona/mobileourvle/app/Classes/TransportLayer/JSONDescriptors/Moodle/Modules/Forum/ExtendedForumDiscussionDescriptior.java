/**
 *
 */
package edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.JSONDescriptors.Moodle.Modules.Forum;

import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONDecoder;
import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONObjectDescriptor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.DiscussionParent;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.ExtendedForumDiscussion;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.ForumDiscussion;

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
     * @see edu.uwi.mona.mobileourvle.app.Classes.CommunicationBase.JSONFactory.
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
     * @see edu.uwi.mona.mobileourvle.app.Classes.CommunicationBase.JSONFactory.
     * JSONObjectDescriptor#getObjectFromJson(com.google.gson.JsonElement)
     */
    @Override
    public ExtendedForumDiscussion getObjectFromJson(final JsonElement json) {
        final JsonObject jsonObject = (JsonObject) json;

        final ForumDiscussion discussion = JSONDecoder
                .getObject(new ForumDiscussionDescriptior(parent), jsonObject);

        final int lastPostId = jsonObject.get("lastpost").getAsInt();
        final String lastPostMessage = "New Message...";

        return new ExtendedForumDiscussion(discussion, lastPostId,
                                           lastPostMessage, parent);
    }
}
