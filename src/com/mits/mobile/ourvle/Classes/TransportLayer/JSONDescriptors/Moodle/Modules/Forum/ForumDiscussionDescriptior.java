/**
 *
 */
package com.mits.mobile.ourvle.Classes.TransportLayer.JSONDescriptors.Moodle.Modules.Forum;

import org.joda.time.DateTime;
import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONDecoder;
import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONEncoder;
import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONObjectDescriptor;
import org.sourceforge.ah.android.utilities.Formatters.DateFormatter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.DiscussionParent;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.ForumDiscussion;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Users.MoodleUser;
import com.mits.mobile.ourvle.Classes.TransportLayer.JSONDescriptors.Moodle.Users.MoodleUserDescriptor;

// TODO: Auto-generated Javadoc

/**
 * The Class UserSessionDescriptior.
 *
 * @author Aston Hamilton
 */
public class ForumDiscussionDescriptior extends
        JSONObjectDescriptor<ForumDiscussion> {

    private final DiscussionParent parent;

    public ForumDiscussionDescriptior(final DiscussionParent parent) {
        this.parent = parent;
    }

    @Override
    public JsonElement getJsonElement(final ForumDiscussion object) {

        final JsonObject encodedDiscussion = new JsonObject();
        encodedDiscussion.addProperty("id", object.getId());
        encodedDiscussion.addProperty("name", object.getName());
        encodedDiscussion.addProperty(
                "creator",
                JSONEncoder.getEncodedObject(new MoodleUserDescriptor(),
                                             object.getCreator()));
        encodedDiscussion.addProperty("modified",
                                      DateFormatter.getISODateString(object.getLastModified()));

        return encodedDiscussion;
    }

    @Override
    public ForumDiscussion getObjectFromJson(final JsonElement json) {
        final JsonObject jsonObject = (JsonObject) json;

        final long id = jsonObject.get("id").getAsLong();
        final String name = jsonObject.get("name").getAsString();

        final DateTime lastModified = DateFormatter
                .getDateTimeFromUnixSeconds(jsonObject.get("timemodified")
                                                      .getAsLong());

        final String creatorId = jsonObject.get("userid").getAsString();
        final String[] creatorFullNameParts = jsonObject.get("firstuserfullname").getAsString().split(" ");

        final String creatorPictureURL = jsonObject.get("firstuserpicture").getAsString();

        final String creatorFirstname = creatorFullNameParts[0];
        final String creatorLastname;

        if (creatorFullNameParts.length > 1) {
            creatorLastname = creatorFullNameParts[1];
        } else {
            creatorLastname = "";
        }

        final MoodleUser creator = new MoodleUser(creatorId, creatorFirstname, creatorLastname, creatorPictureURL);

        return new ForumDiscussion(id, name, creator, lastModified, parent);
    }
}
