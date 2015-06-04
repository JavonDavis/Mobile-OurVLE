/**
 *
 */
package edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.JSONDescriptors.Moodle.Modules.Forum;

import org.joda.time.DateTime;
import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONDecoder;
import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONEncoder;
import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONObjectDescriptor;
import org.sourceforge.ah.android.utilities.Formatters.DateFormatter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.DiscussionParent;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.ForumDiscussion;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Users.MoodleUser;
import edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.JSONDescriptors.Moodle.Users.MoodleUserDescriptor;

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

        encodedDiscussion.addProperty("modified",
                                      DateFormatter.getUnixSecondsFromDateTime(
                                              object.getLastModified())
                                     );
        encodedDiscussion.addProperty("userid", object.getCreator().getId());
        encodedDiscussion.addProperty("firstuserfullname", object.getCreator().getFullName());
        encodedDiscussion.addProperty("firstuserpicture", object.getCreator().getPictureUrl());

        return encodedDiscussion;
    }

    @Override
    public ForumDiscussion getObjectFromJson(final JsonElement json) {
        final JsonObject jsonObject = (JsonObject) json;

        final long id = jsonObject.get("id").getAsLong();
        final String name = jsonObject.get("name").getAsString();

        final DateTime lastModified;

        if (jsonObject.get("timemodified") != null) {
            lastModified =
                    DateFormatter
                            .getDateTimeFromUnixSeconds(jsonObject.get("timemodified").getAsLong());
        } else if (jsonObject.get("modified") != null) {
            lastModified =
                    DateFormatter
                            .getDateTimeFromUnixSeconds(jsonObject.get("modified").getAsLong());
        } else {
            // Should never happen but this is a graceful way to fail and detect the error on the frontend
            lastModified = new DateTime();
        }
        final String creatorId = jsonObject.get("userid").getAsString();
        final String[] creatorFullNameParts = jsonObject.get("firstuserfullname").getAsString()
                                                        .split(" ");

        final String creatorPictureURL = jsonObject.get("firstuserpicture").getAsString();

        final String creatorFirstname = creatorFullNameParts[0];
        final String creatorLastname;

        if (creatorFullNameParts.length > 1) {
            creatorLastname = creatorFullNameParts[1];
        } else {
            creatorLastname = "";
        }

        final MoodleUser creator = new MoodleUser(creatorId, creatorFirstname, creatorLastname,
                                                  creatorPictureURL);

        return new ForumDiscussion(id, name, creator, lastModified, parent);
    }
}
